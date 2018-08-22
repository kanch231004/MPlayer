package com.kanch786.musicapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.kanch786.musicapp.main.MainActivity
import kotlinx.android.synthetic.main.activity_launcher.*

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        etSearchSong.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }

    }
}