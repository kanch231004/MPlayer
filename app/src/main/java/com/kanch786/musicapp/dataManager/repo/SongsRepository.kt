package com.kanch786.musicapp.dataManager.repo

import android.arch.lifecycle.LiveData
import android.util.Log
import com.kanch786.musicapp.api.ApiInterface
import com.kanch786.musicapp.api.SongListResults
import com.kanch786.musicapp.dataManager.dao.SongListDao
import com.kanch786.musicapp.extensions.d
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SongsRepository(private val songDao : SongListDao) {

    fun getAllFavouriteSongs() = songDao.getFavouriteList()

    fun getSongById(trackId : Long)  = songDao.getSongById(trackId )

    fun removeFavouriteSong(song: SongListResults ) {


        Observable.fromCallable {

            val deletedRow = songDao.deleteAsFavourite(song)
            Log.d("deleted row","${deletedRow}")
        }.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe({
                    }, {it.printStackTrace()})
    }

    fun markAsFavourite(song : SongListResults, action : (Boolean) -> Unit) {


              Observable.fromCallable {
                  songDao.addToFavoriteList(song)
                  /*   else
              songListDao.deleteAsFavourite(song.trackId)*/
              }.subscribeOn(Schedulers.newThread())
                      .observeOn(AndroidSchedulers.mainThread())
                      .subscribe({

                          action(true)


                      }, {
                          action(false)
                          it.printStackTrace()

                      })




    }

    fun loadSongResults(query : String) : LiveData<List<SongListResults>>{

        return object : LiveData<List<SongListResults>>() {

            override fun onActive() {
                super.onActive()

                Log.d("view model", "onActive called")
                ApiInterface.create().getSongResultResponse(query,50)

                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {

                                    value = if (it.isSuccessful && it.code() == 200) {it.body()?.results} else { null }

                                }, {


                            it.printStackTrace()

                        })
            }
        }


    }
}