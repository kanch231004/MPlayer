package com.kanch786.musicapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.kanch786.musicapp.extensions.d
import com.kanch786.musicapp.main.songs.MainActivity
import kotlinx.android.synthetic.main.activity_launcher.*

class LauncherActivity : AppCompatActivity() {

    private val SPLASH_TIMEOUT = 2000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        etSearchSong.alpha = 0.0f
        Handler().postDelayed( {

            etSearchSong.animate().alpha(1.0f).duration = 700
            etSearchSong.visibility = View.VISIBLE

        },SPLASH_TIMEOUT)

        etSearchSong.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                    etSearchSong,   // Starting view
                    resources.getString(R.string.search_song_hint)    // The String
            )


            ActivityCompat.startActivity(this, intent, options.toBundle())

            d("startActivity executed")
        }
    }


        //etSearchSong.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }


}