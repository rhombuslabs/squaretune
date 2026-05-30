package com.rhombuslabs.rotateplayer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rhombuslabs.rotateplayer.ui.theme.RotatePlayerTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NowPlayingScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Blurred Background (using a placeholder color for now)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
                .blur(20.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Square Thumbnail (50% of viewport approx)
            Surface(
                modifier = Modifier
                    .size(360.dp)
                    .aspectRatio(1f),
                shape = RoundedCornerShape(16.dp),
                color = Color.Gray
            ) {
                // Image would go here
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Marquee Text for Title
            Text(
                text = "Song Title That Is Very Long and Should Marquee",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier.basicMarquee()
            )

            Text(
                text = "Artist Name",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.LightGray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Transport Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* Previous */ }) {
                    Icon(
                        Icons.Default.SkipPrevious,
                        contentDescription = "Previous",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }

                FilledIconButton(
                    onClick = { /* Play/Pause */ },
                    modifier = Modifier.size(64.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        modifier = Modifier.size(48.dp)
                    )
                }

                IconButton(onClick = { /* Next */ }) {
                    Icon(
                        Icons.Default.SkipNext,
                        contentDescription = "Next",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    }
}

@Preview(widthDp = 720, heightDp = 720)
@Composable
fun NowPlayingScreenPreview() {
    RotatePlayerTheme {
        NowPlayingScreen()
    }
}
