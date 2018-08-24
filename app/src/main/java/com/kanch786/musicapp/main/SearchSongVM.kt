package com.kanch786.musicapp.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.kanch786.musicapp.api.ApiInterface
import com.kanch786.musicapp.api.SongListResults
import com.kanch786.musicapp.extensions.d
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchSongVM : ViewModel(){

    private val songListLd = MutableLiveData<List<SongListResults>>()

    fun getSongListResults() =

        object :LiveData<List<SongListResults>>() {

            override fun onActive() {
                super.onActive()


                Log.d("view model","onActive called")
                ApiInterface.create().getSongResultResponse("jack",17)

                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {

                                    if (it.isSuccessful) {
                                        Log.d("view model","response code ${it.code()}")
                                        Log.d("view model","success called ${it.body()?.results} result count ${it.body()?.resultCount}")
                                        value = it.body()?.results
                                    }
                                    else {

                                        Log.d("view model","error msg ${it.errorBody().toString()}")
                                    }
                                }, {


                            it.printStackTrace()
                            d("error message ${it.localizedMessage}")
                        })
            }

    }
}