package com.kanch786.musicapp.dataManager.networkResource

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.MainThread
import android.support.annotation.NonNull
import com.bumptech.glide.Glide.init

abstract class NetworkResource<ResultType,RequestType> {

    private var result = MediatorLiveData<Resource<ResultType>>()

    init {


    }

    private fun starObserving() {

        val dbSource  = loadFromDb()

        result.addSource(dbSource) {

            result.removeSource(dbSource)

            createCall()
        }


    }

    private fun makeNetworkCall() {



    }


    protected abstract fun saveCallResult(@NonNull item: RequestType?)

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @NonNull
    @MainThread
    protected abstract fun createCall(): LiveData<Resource<RequestType>>

    fun getAsLiveData () : LiveData<Resource<ResultType>> = result


}