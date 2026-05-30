# RotatePlayer

A dedicated music player for the **Anbernic RG Rotate**, optimized for its unique 720x720 square display.

## Features
- **720x720 UI**: Optimized layout for 1:1 aspect ratio.
- **Hybrid Sync**: High-speed local SMB synchronization with Tailscale VPN fallback.
- **Hardware Tactile Controls**: Full physical button mapping for eyes-free operation.

## Hardware Button Mappings

| Physical Input | Android KeyEvent | Function |
| :--- | :--- | :--- |
| **D-Pad Up/Down** | `KEYCODE_DPAD_UP/DOWN` | System Audio Volume |
| **D-Pad Left/Right** | `KEYCODE_DPAD_LEFT/RIGHT`| Selection Navigation / Track Skip |
| **L1 / R1 Triggers** | `KEYCODE_BUTTON_L1/R1` | Previous / Next Track |
| **Face Button A** | `KEYCODE_BUTTON_A` | Confirm / Execute Selection |
| **Face Button X** | `KEYCODE_BUTTON_X` | Play / Pause Toggle |

## Development

### Requirements
- Android SDK (API 34)
- Go 1.21+ (for `tsnet` backend)
- `gomobile` for Go-Android binding.

### Build
1. Build the Go backend:
   ```bash
   cd backend
   gomobile bind -target=android -o ../android/app/libs/tailscalecore.aar .
   ```
2. Build the Android app:
   ```bash
   cd android
   ./gradlew assembleDebug
   ```

## License
Apache 2.0
