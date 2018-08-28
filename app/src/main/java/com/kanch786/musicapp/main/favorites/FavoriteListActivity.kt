package com.kanch786.musicapp.main.favorites

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.kanch786.musicapp.R
import com.kanch786.musicapp.api.SongListResults
import com.kanch786.musicapp.base.BaseRvAdapter
import com.kanch786.musicapp.base.RecyclerItemTouchHelper
import com.kanch786.musicapp.dataManager.MPlayerDatabase
import com.kanch786.musicapp.dataManager.repo.SongsRepository
import com.kanch786.musicapp.extensions.d
import com.kanch786.musicapp.main.songs.SongsVM
import com.kanch786.musicapp.main.SongsViewModelFactory
import com.kanch786.musicapp.main.songs.PlaySongActivity
import com.kanch786.musicapp.rx.RxBus
import com.kanch786.musicapp.rx.RxSongSelectEvent
import com.kanch786.musicapp.rx.registerInBus
import com.kanch786.musicapp.viewHolder.SongListViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_favourite_songs.*

class FavoriteListActivity : AppCompatActivity(), RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {


    private  var songsList = ArrayList<SongListResults>()
    private lateinit var songListAdapter : BaseRvAdapter<SongListResults, SongListViewHolder>
    private lateinit var songsVM: SongsVM
    private lateinit var mPlayerDbInstance  : MPlayerDatabase
    private lateinit var songsViewModelFactory: SongsViewModelFactory
    private var currentFragmentPos = -1
    private var oldSeletectedEvent : RxSongSelectEvent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_songs)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Favourite"

        mPlayerDbInstance = MPlayerDatabase.getInstance(this)
        songsViewModelFactory = SongsViewModelFactory(SongsRepository(mPlayerDbInstance.songsDao()))
        songsVM = ViewModelProviders.of(this,songsViewModelFactory).get(SongsVM::class.java)

        val layoutManager = LinearLayoutManager(this)
        rvSongs.layoutManager = layoutManager


        val itemTouchCallback = RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this)
        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(rvSongs)

        getFavouriteList()


    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {

        songsVM.removeFromFavourite(songListAdapter.getItemAtPosition(viewHolder.adapterPosition))
        songListAdapter.removeItemAtPosition(viewHolder.adapterPosition)
        songListAdapter.notifyItemRemoved(viewHolder.adapterPosition)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {

            android.R.id.home-> finish()
            R.id.favourite -> {
                startActivity(Intent(this, FavoriteListActivity::class.java))
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

        override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun getFavouriteList() {

        songsVM.getFavouriteSongs().observe(this, Observer {

            displayFavouriteList(it?.toMutableList())
        })
    }

    private fun displayFavouriteList(songListResults: MutableList<SongListResults>?) {


        songListResults?.let {  tvCount.text = if (songListResults.isNotEmpty()) "All Songs ${songListResults?.size}" else "No Favorite Song"}

        songListResults?.let {

            songsList = ArrayList(it)
            songListAdapter = BaseRvAdapter(this, R.layout.rv_song_items, {

                SongListViewHolder(it)
            }, it.toMutableList(), { holder, item, position ->

                holder.bind(item,position) {


                    songListAdapter.notifyDataSetChanged()

                }
            })

            rvSongs.adapter = songListAdapter
            songListAdapter.notifyDataSetChanged()

        }
    }

    override fun onResume() {
        super.onResume()

        RxBus.observeSticky<RxSongSelectEvent>()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe({

                    d("event received ")

                    if (oldSeletectedEvent != null){
                        if (oldSeletectedEvent!!.fragmentPosition == currentFragmentPos ){

                            songsList[oldSeletectedEvent!!.adapterPosition].isSelected = false
                            songListAdapter.notifyItemChanged(oldSeletectedEvent!!.adapterPosition)
                        }
                    }

                    if( it.fragmentPosition == currentFragmentPos){

                        songsList[it.adapterPosition].isSelected = true
                        songListAdapter.notifyItemChanged(it.adapterPosition)
                        if (songsList[it.adapterPosition].previewUrl !== null) {
                            val intent = Intent(this, PlaySongActivity::class.java)
                            intent.putExtra("songName", songsList[it.adapterPosition])
                            startActivity(intent)
                        } else Toast.makeText(this, getString(R.string.song_not_available), Toast.LENGTH_SHORT).show()

                    }

                    oldSeletectedEvent = it


                    RxBus.sendSticky("")
                },{

                }).registerInBus(this)
    }


    override fun onPause() {
        super.onPause()
        RxBus.unregister(this)
    }

}