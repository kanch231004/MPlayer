package com.kanch786.musicapp.main.query

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.kanch786.musicapp.dataManager.MPlayerDatabase
import com.kanch786.musicapp.dataManager.repo.QueryRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class QueryVM (private val queryRepo : QueryRepository) : ViewModel(){


    private var  suggestionLd : LiveData<List<String>>? = null


  fun  insertSuggestions(query : String)  = queryRepo.insertQuery(query)


    fun getSuggestionsList() : LiveData<List<String>> {

        if (suggestionLd ==null ) {
                suggestionLd=  queryRepo.getSuggetions()
        }

        return suggestionLd!!
    }

}