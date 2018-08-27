package com.kanch786.musicapp.api

import com.kanch786.musicapp.Constants.ERROR_TYPE

sealed class Resource<out T> {


    data class Success<out T>(val data: T?): Resource<T>()

    data class Error<out T>(var errorType: ERROR_TYPE, var errorMessage: String, val data: T? = null ) : Resource<T>()


}