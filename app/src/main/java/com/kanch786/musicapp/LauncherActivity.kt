package com.kanch786.musicapp

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log.d
import com.kanch786.musicapp.extensions.d
import com.kanch786.musicapp.main.MainActivity
import kotlinx.android.synthetic.main.activity_launcher.*

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

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