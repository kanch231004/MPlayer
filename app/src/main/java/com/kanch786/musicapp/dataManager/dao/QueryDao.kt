package com.kanch786.musicapp.dataManager.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.kanch786.musicapp.api.SuggestionsList

@Dao
abstract class QueryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertItem(query : SuggestionsList) : Long

    @Query("Select * From SuggestionsList")
    abstract fun getQueryHistoryList() : LiveData<List<String>>
}