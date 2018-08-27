package com.kanch786.musicapp

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

class PlaySongActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private var playPause: Boolean = false
    private var intialStage = true


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_play_song)

        mediaPlayer = MediaPlayer()
        mediaPlayer.setAudioAttributes(AudioAttributes.Builder().build())

        /*flPlayPause.setOnClickListener {

            if (!playPause) {
                ivPlayPause.setBackgroundResource(R.drawable.combined_shape_2)
                if (intialStage)
                    MediaPlayer()
                            .execute("http://www.virginmegastore.me/Library/Music/CD_001214/Tracks/Track1.mp3")
                else {
                    if (!mediaPlayer.isPlaying)
                        mediaPlayer.start()
                }
                playPause = true
            } else {
                ivPlayPause.setBackgroundResource(R.drawable.button_play)
                if (mediaPlayer.isPlaying)
                    mediaPlayer.pause()
                playPause = false
            }
        }*/

    }



}