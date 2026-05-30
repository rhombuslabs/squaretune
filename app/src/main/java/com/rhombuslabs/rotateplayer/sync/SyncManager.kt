package com.rhombuslabs.rotateplayer.sync

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
//import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

class SyncManager(private val context: Context) {

    private val TAG = "SyncManager"
    private val LOCAL_SMB_IP = "192.168.1.50"
    private val REMOTE_SMB_HOST = "your-unraid-nas.your-tailnet.ts.net"

    suspend fun performSync() = withContext(Dispatchers.IO) {
        Log.d(TAG, "Starting sync probe...")
        
        val isLocalAvailable = checkLocalLanAccess(LOCAL_SMB_IP)
        
        if (isLocalAvailable) {
            Log.d(TAG, "Local network detected. Running high-speed sync.")
            runFileSync(LOCAL_SMB_IP)
        } else {
            Log.d(TAG, "Local network not found. Attempting Tailscale sync.")
            // Uses the Go-bound Tailscale library to map localhost:44445 to remote port 445
            try {
                tailscalecore.Tailscalecore.startPortForward("tskey-auth-XXXXXX", "rotate-player", 44445L, REMOTE_SMB_HOST, 445L)
                delay(2000) // Wait for handshake
                
                // When on Tailscale, connect to localhost using the mapped port
                runFileSync("127.0.0.1")
            } catch (e: Exception) {
                Log.e(TAG, "Tailscale connection failed", e)
            } finally {
                tailscalecore.Tailscalecore.stopTunnel()
            }
        }
    }

    private fun checkLocalLanAccess(ip: String): Boolean {
        return try {
            val socket = Socket()
            socket.connect(InetSocketAddress(ip, 445), 1000)
            socket.close()
            true
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun runFileSync(targetUrl: String) {
        Log.d(TAG, "Syncing files from $targetUrl...")
        // Implementation of file transfer (SMB/HTTP/etc)
        delay(1000) // Simulating work
        Log.d(TAG, "Sync complete.")
    }
}
