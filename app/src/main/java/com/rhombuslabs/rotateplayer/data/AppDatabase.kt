package com.rhombuslabs.rotateplayer.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Dao
interface TrackDao {
    @Query("SELECT * FROM tracks ORDER BY artist, album, title")
    fun getAllTracks(): List<TrackEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(tracks: List<TrackEntity>)
    
    @Query("DELETE FROM tracks")
    fun deleteAll()
}

@Database(entities = [TrackEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rotate_player_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
