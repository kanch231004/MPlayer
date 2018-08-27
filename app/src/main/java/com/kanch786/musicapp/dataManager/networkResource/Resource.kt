package com.kanch786.musicapp.dataManager.networkResource

sealed class Resource<out T> {

    data class Success<out T> (val data : T?) : Resource<T>()
    data class Error<out T>(val  errorMessage : String, val data : T? = null) : Resource<T>()
    data class Loading<out T>(val data : T?) : Resource<T>()
}