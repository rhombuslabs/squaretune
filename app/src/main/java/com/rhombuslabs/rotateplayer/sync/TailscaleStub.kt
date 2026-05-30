package com.rhombuslabs.rotateplayer.sync

// This stub mocks the generated gomobile bindings for the Tailscale Go library
object tailscalecore {
    object Tailscalecore {
        @Throws(Exception::class)
        fun startPortForward(authKey: String, hostname: String, localPort: Long, remoteIP: String, remotePort: Long) {
            // Stub implementation
            println("Stub: Tailscale forwarding port $localPort to $remoteIP:$remotePort")
        }

        @Throws(Exception::class)
        fun stopTunnel() {
            // Stub implementation
            println("Stub: Tailscale stopped")
        }
    }
}
