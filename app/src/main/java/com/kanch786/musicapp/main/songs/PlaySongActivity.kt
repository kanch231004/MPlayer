package com.kanch786.musicapp.main.songs

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.kanch786.musicapp.api.SongListResults
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.kanch786.musicapp.R
import com.kanch786.musicapp.dataManager.MPlayerDatabase
import com.kanch786.musicapp.dataManager.dao.SongListDao
import com.kanch786.musicapp.extensions.d
import com.kanch786.musicapp.main.SongsViewModelFactory
import com.kanch786.musicapp.main.favorites.FavoriteListActivity
import com.kanch786.musicapp.songRepo
import kotlinx.android.synthetic.main.activity_play_song.*
import kotlinx.android.synthetic.main.layout_music_controller_view.*


class PlaySongActivity : AppCompatActivity() , Player.EventListener{

    private lateinit var songToPlay  : SongListResults
    private lateinit var songVM : SongsVM
    private  var player : SimpleExoPlayer? = null
    private var playbackPosition : Long? = null
    private var currentWindow : Int? = null
    private  var playWhenReady : Boolean = false
    private lateinit var mPlayerDbInstance  : MPlayerDatabase
    private lateinit var songListDao : SongListDao
    private lateinit var songsViewModelFactory: SongsViewModelFactory

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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_song)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mPlayerDbInstance = MPlayerDatabase.getInstance(applicationContext)
        songsViewModelFactory = SongsViewModelFactory(songRepo)
        songListDao= mPlayerDbInstance.songsDao()
        songVM = ViewModelProviders.of(this,songsViewModelFactory).get(SongsVM::class.java)
        setUpClickListeners()

        toolbar.setNavigationIcon(R.drawable.arrow_back)
        songToPlay = intent.getSerializableExtra("songName") as SongListResults

      //  togglePlaying()


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.play_song_menu,menu)

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {

            android.R.id.home-> finish()
            R.id.favourite -> {
                startActivity(Intent(this,FavoriteListActivity::class.java))
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun setUpClickListeners() {

        ivFavourite.setOnClickListener {

            if(!songToPlay.isFavorite)
                 markAsFavourite (songToPlay)
        }

        ivShuffle.setOnClickListener { finish() }

    }

    private fun getSongById () {

        d("track id ${songToPlay.trackId}")
        var trackId = songToPlay.trackId
        songVM.getSongById(songToPlay.trackId).observe(this, Observer {

            Log.d("PlaySong","result $it")


            songToPlay.isFavorite = !(it == null || it.isEmpty())
            updateView(if(it == null) songToPlay else it[0])
        })
    }

    private fun updateView(song: SongListResults?) {

        song?.let {

            tvSongName.text = it.trackName
            tvArtistName.text = "${it.artistName}"
            Glide.with(this).load(it.artworkUrl100).into(ivSong)
            ivFavourite.setImageDrawable(if (it.isFavorite) ContextCompat.getDrawable(this, R.drawable.shape_heart_red)
            else ContextCompat.getDrawable(this, R.drawable.shape_heart))


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

    //toggle favourite was not the requirement , so only marking facility is there
    private fun markAsFavourite(favouriteSong : SongListResults) {

        d("mark As favourite called")

        songVM.markAsFavourite(favouriteSong).observe(this, Observer {

         when(it) {

             true -> ivFavourite.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.shape_heart_red))
             false -> ivFavourite.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.shape_heart))
         }

        })
    }


    public override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }

    public override fun onResume() {
        super.onResume()
        getSongById()
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