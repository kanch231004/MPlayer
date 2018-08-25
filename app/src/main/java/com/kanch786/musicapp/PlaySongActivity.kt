package com.kanch786.musicapp

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.kanch786.musicapp.api.SongListResults
import java.io.Serializable
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.kanch786.musicapp.extensions.d
import kotlinx.android.synthetic.main.activity_play_song.*
import kotlinx.android.synthetic.main.layout_music_controller_view.*


class PlaySongActivity : AppCompatActivity() , Player.EventListener{

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {


       d("time ${playbackParameters?.getMediaTimeUsForPlayoutTimeMs(songToPlay.trackTimeMillis)}")
    }

    override fun onSeekProcessed() {

    }

    override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {

    }

    override fun onPlayerError(error: ExoPlaybackException?) {

    }

    override fun onLoadingChanged(isLoading: Boolean) {

    }

    override fun onPositionDiscontinuity(reason: Int) {

    }

    override fun onRepeatModeChanged(repeatMode: Int) {

    }

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {

    }

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {

        d("timeline $timeline")

    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {

        d("playback state $playbackState")

    }

    private lateinit var songToPlay  : SongListResults
    private  var player : SimpleExoPlayer? = null
    private var playbackPosition : Long? = null
    private var currentWindow : Int? = null
    private  var playWhenReady : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_song)

        toolbar.setNavigationIcon(R.drawable.arrow_back)
        songToPlay = intent.getSerializableExtra("songName") as SongListResults
        togglePlaying()
        updateView()
    }

    private fun updateView() {

        tvSongName.text = songToPlay.trackName
        tvArtistName.text = "${songToPlay.artistName}"
        Glide.with(this).load(songToPlay.artworkUrl100).into(ivSong)

    }

    private fun togglePlaying() {

        flPlayPause.setOnClickListener {

           playWhenReady = !playWhenReady
            //ivPlayPause.setImageDrawable(if (playWhenReady) ContextCompat.getDrawable(this,R.drawable.combined_shape_2) else ContextCompat.getDrawable(this,R.drawable.triangle))
            player?.playWhenReady = playWhenReady

           }

    }

   private fun initializePlayer() {

      val bandwidthMeter = DefaultBandwidthMeter() //Provides estimates of the currently available bandwidth.
      val  trackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
      val  trackSelector = DefaultTrackSelector(trackSelectionFactory)


     player = ExoPlayerFactory.newSimpleInstance(
               DefaultRenderersFactory(this),
             trackSelector, DefaultLoadControl())
       player?.addListener(this)
       val uri = Uri.parse(songToPlay.previewUrl)
       val mediaSource = buildMediaSource(uri)
       playerView.player = player
       player?.prepare(mediaSource,true,true)
       player?.playWhenReady = true
       playerView.controllerShowTimeoutMs = 0

   }

    private fun buildMediaSource(uri: Uri): ExtractorMediaSource {

       val dataSourceFactory = DefaultDataSourceFactory(applicationContext, "MPlayer")

        return ExtractorMediaSource.Factory(
                dataSourceFactory).createMediaSource(uri)
    }

    private fun releasePlayer() {

        if (player != null) {

            playbackPosition = player?.currentPosition
            currentWindow = player?.currentWindowIndex
            playWhenReady = player?.playWhenReady!!
            player?.release()
            player = null

        }
    }

    public override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }

    public override fun onResume() {
        super.onResume()
       // hideSystemUi()
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer()
        }
    }

    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    public override fun onStop() {
        super.onStop()

        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }




}