package com.kanch786.musicapp

import android.app.Application
import android.content.Context
import android.support.v4.app.Fragment
import com.kanch786.musicapp.dataManager.MPlayerDatabase
import com.kanch786.musicapp.dataManager.repo.QueryRepository
import com.kanch786.musicapp.dataManager.repo.SongsRepository

val appInstance: ApplicationGlobal by lazy {
    ApplicationGlobal.appInstance!!
}

class ApplicationGlobal : Application() {


    lateinit var queryRepo : QueryRepository
    lateinit var songsRepository: SongsRepository
    companion object {

        var appInstance: ApplicationGlobal? = null

    }

    override fun onCreate() {
        super.onCreate()
        appInstance= this
        queryRepo = QueryRepository(MPlayerDatabase.getInstance(this).queryao())
        songsRepository = SongsRepository(MPlayerDatabase.getInstance(this).songsDao())
    }


}

val Context.queryRepo: QueryRepository
    get() = (applicationContext as ApplicationGlobal).queryRepo

val Fragment.queryRepo: QueryRepository
    get() = activity!!.queryRepo

val Context.songRepo: SongsRepository
    get() = (applicationContext as ApplicationGlobal).songsRepository

val Fragment.songRepo: SongsRepository
    get() = activity!!.songRepo

