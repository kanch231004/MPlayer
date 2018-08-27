package com.kanch786.musicapp.main

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.kanch786.musicapp.dataManager.dao.SongListDao
import com.kanch786.musicapp.dataManager.repo.SongsRepository
import com.kanch786.musicapp.main.songs.SongsVM

class SongsViewModelFactory(private val dataSource: SongsRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SongsVM::class.java)) {
            return SongsVM(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}