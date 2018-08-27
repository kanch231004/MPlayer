package com.kanch786.musicapp.dataManager.repo

import com.kanch786.musicapp.api.SuggestionsList
import com.kanch786.musicapp.dataManager.dao.QueryDao
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

open class QueryRepository(private val queryDao : QueryDao) {

    fun getSuggetions() = queryDao.getQueryHistoryList()

    fun insertQuery(query : String) = Observable.fromCallable { queryDao.insertItem(SuggestionsList(query) ) } .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())

}