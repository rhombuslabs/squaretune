package com.rhombuslabs.rotateplayer

//import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
//import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.rhombuslabs.rotateplayer.ui.LibraryScreen
import com.rhombuslabs.rotateplayer.ui.SettingsScreen
import com.rhombuslabs.rotateplayer.ui.theme.RotatePlayerTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment

enum class Screen {
    LIBRARY, SETTINGS, NOW_PLAYING
}

class MainActivity : ComponentActivity() {
    private lateinit var audioManager: AudioManager
    private var currentScreen by mutableStateOf(Screen.LIBRARY)

    private val folderPickerLauncher = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
        if (uri != null) {
            contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            Log.d("RotatePlayer", "Selected folder: $uri")
            // Trigger MediaScanner here...
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        setContent {
            RotatePlayerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (currentScreen) {
                        Screen.LIBRARY -> {
                            Box(modifier = Modifier.fillMaxSize()) {
                                LibraryScreen()
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .padding(16.dp),
                                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
                                ) {
                                    Button(onClick = { currentScreen = Screen.NOW_PLAYING }) {
                                        Text("Now Playing")
                                    }
                                    Button(onClick = { currentScreen = Screen.SETTINGS }) {
                                        Text("Settings")
                                    }
                                }
                            }
                        }
                        Screen.NOW_PLAYING -> {
                            Box(modifier = Modifier.fillMaxSize()) {
                                com.rhombuslabs.rotateplayer.ui.NowPlayingScreen()
                                Button(
                                    onClick = { currentScreen = Screen.LIBRARY },
                                    modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
                                ) {
                                    Text("Back to Library")
                                }
                            }
                        }
                        Screen.SETTINGS -> {
                            SettingsScreen(
                                onBack = { currentScreen = Screen.LIBRARY },
                                onSelectFolder = { folderPickerLauncher.launch(null) }
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_DPAD_UP -> {
                audioManager.adjustStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_RAISE,
                    AudioManager.FLAG_SHOW_UI
                )
                true
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                audioManager.adjustStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_LOWER,
                    AudioManager.FLAG_SHOW_UI
                )
                true
            }
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                Log.d("RotatePlayer", "Track Skip Previous")
                true
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                Log.d("RotatePlayer", "Track Skip Next")
                true
            }
            KeyEvent.KEYCODE_BUTTON_L1 -> {
                Log.d("RotatePlayer", "Previous Track Button")
                true
            }
            KeyEvent.KEYCODE_BUTTON_R1 -> {
                Log.d("RotatePlayer", "Next Track Button")
                true
            }
            KeyEvent.KEYCODE_BUTTON_A -> {
                Log.d("RotatePlayer", "Confirm Selection Button")
                true
            }
            KeyEvent.KEYCODE_BUTTON_X -> {
                Log.d("RotatePlayer", "Play/Pause Button")
                true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }
}
