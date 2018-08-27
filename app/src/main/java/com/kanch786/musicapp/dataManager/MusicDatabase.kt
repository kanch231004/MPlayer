package com.kanch786.musicapp.dataManager

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.kanch786.musicapp.api.SongListResults

@Database(entities = [SongListResults::class],version = 1,exportSchema = true)
abstract class MusicDatabase : RoomDatabase(){


}