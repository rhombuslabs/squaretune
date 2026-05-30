package com.rhombuslabs.rotateplayer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onSelectFolder: () -> Unit
) {
    var localSmb by remember { mutableStateOf("192.168.1.50") }
    var tailnetSmb by remember { mutableStateOf("nas.tailnet.ts.net") }
    var useTailscale by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    Button(onClick = onBack) {
                        Text("Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = localSmb,
                onValueChange = { localSmb = it },
                label = { Text("Local SMB IP") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
            )

            OutlinedTextField(
                value = tailnetSmb,
                onValueChange = { tailnetSmb = it },
                label = { Text("Tailnet SMB Host") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Enable Tailscale Fallback")
                Switch(
                    checked = useTailscale,
                    onCheckedChange = { useTailscale = it }
                )
            }

            Button(
                onClick = onSelectFolder,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Select Local Music Folder (SAF)")
            }

            Button(
                onClick = { /* Trigger Sync */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Force Remote Sync")
            }
        }
    }
}
