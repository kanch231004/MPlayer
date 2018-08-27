package com.kanch786.musicapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.kanch786.musicapp.Constants.SplashScreenTimeout
import com.kanch786.musicapp.Constants.alphaInvisible
import com.kanch786.musicapp.Constants.alphaVisisble
import com.kanch786.musicapp.Constants.etSearchAnimDuration
import com.kanch786.musicapp.extensions.d
import com.kanch786.musicapp.main.songs.MainActivity
import kotlinx.android.synthetic.main.activity_launcher.*

class LauncherActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        etSearchSong.alpha = alphaInvisible
        Handler().postDelayed( {

            etSearchSong.animate().alpha(alphaVisisble).duration = etSearchAnimDuration
            etSearchSong.visibility = View.VISIBLE

        }, SplashScreenTimeout)

        etSearchSong.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                    etSearchSong,   // Starting view
                    resources.getString(R.string.search_song_hint)    // The String
            )


            ActivityCompat.startActivity(this, intent, options.toBundle())

        }
    }



}