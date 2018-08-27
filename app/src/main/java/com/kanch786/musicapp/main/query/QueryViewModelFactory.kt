package com.kanch786.musicapp.main.query

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.kanch786.musicapp.dataManager.dao.SongListDao
import com.kanch786.musicapp.dataManager.repo.QueryRepository
import com.kanch786.musicapp.main.songs.SongsVM

class QueryViewModelFactory(private val repo: QueryRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QueryVM::class.java)) {
            return QueryVM(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}