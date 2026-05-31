package com.rhombuslabs.squaretune.media

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import com.rhombuslabs.squaretune.data.AppDatabase
import com.rhombuslabs.squaretune.data.TrackEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jaudiotagger.audio.AudioFileIO
import java.io.File
import java.io.FileOutputStream
import org.jaudiotagger.tag.FieldKey

class MediaScanner(private val context: Context) {
    private val TAG = "MediaScanner"
    private val db = AppDatabase.getDatabase(context)

    suspend fun scanLocalTree(treeUri: Uri) = withContext(Dispatchers.IO) {
        val rootDoc = DocumentFile.fromTreeUri(context, treeUri)
        if (rootDoc == null) {
            Log.e(TAG, "Cannot read document tree at $treeUri")
            return@withContext
        }

        val tracks = mutableListOf<TrackEntity>()
        scanDirectory(rootDoc, tracks)
        
        Log.d(TAG, "Found ${tracks.size} tracks. Updating database...")
        db.trackDao().deleteAll()
        db.trackDao().insertAll(tracks)
    }

    private fun scanDirectory(dir: DocumentFile, trackList: MutableList<TrackEntity>) {
        val files = dir.listFiles()
        for (file in files) {
            if (file.isDirectory) {
                scanDirectory(file, trackList)
            } else if (file.name?.endsWith(".mp3", ignoreCase = true) == true ||
                       file.name?.endsWith(".flac", ignoreCase = true) == true) {
                
                try {
                    val track = TrackEntity(
                        id = file.uri.toString(),
                        title = file.name ?: "Unknown Title",
                        artist = "Unknown Artist",
                        album = "Unknown Album",
                        durationMs = 0L,
                        filePath = file.uri.toString()
                    )
                    trackList.add(track)
                    
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing media file: ${file.name}", e)
                }
            }
        }
    }
}
