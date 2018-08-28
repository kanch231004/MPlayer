package com.kanch786.musicapp.main.songs

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.kanch786.musicapp.api.ApiInterface
import com.kanch786.musicapp.api.SongListResults
import com.kanch786.musicapp.dataManager.dao.SongListDao
import com.kanch786.musicapp.dataManager.repo.SongsRepository
import com.kanch786.musicapp.extensions.d
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SongsVM(private val songsRepo : SongsRepository) : ViewModel() {

    private var songListLd: LiveData<List<SongListResults>>? = null
    private var favouriteSongLd: LiveData<List<SongListResults>>? = null
    private var query = ""
    private var trackId = -1L

    fun getSongListResults(receivedQuery: String): LiveData<List<SongListResults>> {


        if (songListLd == null || receivedQuery != query) {
            query = receivedQuery


            songListLd = songsRepo.loadSongResults(query)
        }
        return songListLd!!

    }


    fun markAsFavourite(song : SongListResults ,action : (Boolean) -> Unit) {

        songsRepo.markAsFavourite(song,action)
    }




    fun getSongById(receivedTrackId: Long): LiveData<List<SongListResults>> {

        if (favouriteSongLd == null || trackId != receivedTrackId) {
            trackId = receivedTrackId
            favouriteSongLd = songsRepo.getSongById(trackId)
        }
        return favouriteSongLd!!

    }

    fun getFavouriteSongs(): LiveData<List<SongListResults>> {

        if (favouriteSongLd == null) {

            favouriteSongLd = songsRepo.getAllFavouriteSongs()
        }

        return favouriteSongLd!!

    }

    fun removeFromFavourite(song: SongListResults) {

        songsRepo.removeFavouriteSong(song)
    }
}



