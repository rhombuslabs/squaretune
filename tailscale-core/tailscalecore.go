package tailscalecore

import (
	"context"
	"fmt"
	"io"
	"log"
	"net"

	"tailscale.com/tsnet"
)

var s *tsnet.Server
var listener net.Listener

// StartPortForward starts a user-space Tailscale node and proxies a local port to a remote host/port on the tailnet.
func StartPortForward(authKey, hostname string, localPort int, remoteIP string, remotePort int) error {
	if s != nil {
		return nil
	}

	s = &tsnet.Server{
		AuthKey:  authKey,
		Hostname: hostname,
	}

	if err := s.Start(); err != nil {
		return err
	}

	// Start local listener
	var err error
	listener, err = net.Listen("tcp", fmt.Sprintf("127.0.0.1:%d", localPort))
	if err != nil {
		s.Close()
		s = nil
		return err
	}

	go func() {
		for {
			localConn, err := listener.Accept()
			if err != nil {
				log.Printf("listener error: %v", err)
				return // Listener closed
			}

			go handleConnection(localConn, remoteIP, remotePort)
		}
	}()

	return nil
}

func handleConnection(localConn net.Conn, remoteIP string, remotePort int) {
	defer localConn.Close()

	// Dial out via tsnet to the target tailnet device
	remoteConn, err := s.Dial(context.Background(), "tcp", fmt.Sprintf("%s:%d", remoteIP, remotePort))
	if err != nil {
		log.Printf("failed to connect to tailnet target: %v", err)
		return
	}
	defer remoteConn.Close()

	// Bidirectional copy
	go io.Copy(localConn, remoteConn)
	io.Copy(remoteConn, localConn)
}

// StopTunnel stops the user-space Tailscale node and listener.
func StopTunnel() error {
	if listener != nil {
		listener.Close()
		listener = nil
	}
	if s == nil {
		return nil
	}
	err := s.Close()
	s = nil
	return err
}

func main() {
	// gomobile entry point
}
