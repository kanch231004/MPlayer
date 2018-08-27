package com.kanch786.musicapp.dataManager

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.kanch786.musicapp.api.SongListResults
import com.kanch786.musicapp.api.SuggestionsList
import com.kanch786.musicapp.dataManager.dao.QueryDao
import com.kanch786.musicapp.dataManager.dao.SongListDao

@Database(entities = [SongListResults::class,SuggestionsList::class],version = 1,exportSchema = true)
abstract class MPlayerDatabase  : RoomDatabase() {

    abstract fun songsDao() : SongListDao
    abstract fun queryao() : QueryDao

    companion object {

      @Volatile private var INSTANCE: MPlayerDatabase? = null

        fun getInstance(context: Context): MPlayerDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        MPlayerDatabase::class.java, "MusicPlayer.db")
                        .build()
    }
}
