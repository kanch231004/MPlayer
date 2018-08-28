package com.kanch786.musicapp.main.songs

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.kanch786.musicapp.R
import com.kanch786.musicapp.api.SongListResults
import com.kanch786.musicapp.base.BaseRvAdapter
import com.kanch786.musicapp.extensions.d
import com.kanch786.musicapp.rx.RxBus
import com.kanch786.musicapp.rx.RxSongSelectEvent
import com.kanch786.musicapp.rx.registerInBus
import com.kanch786.musicapp.viewHolder.SongListViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_songs_list.*

class NewFragmentInstanceList : Fragment() {

    private  var songsList = ArrayList<SongListResults>()
    private  var songListAdapter : BaseRvAdapter<SongListResults, SongListViewHolder>? = null
    private var currentFragmentPos = -1
    private var oldSeletectedEvent : RxSongSelectEvent? = null


    companion object Factory {

        fun create(fragmentPos : Int, songList : ArrayList<SongListResults>) : Fragment {

            val newFragmentInstanceList = NewFragmentInstanceList()
            val argument = Bundle()
            argument.putSerializable("songsList",songList)
            argument.putInt("fragmentPos",fragmentPos)
            newFragmentInstanceList.arguments = argument
            return newFragmentInstanceList
        }

    }

    override fun getUserVisibleHint(): Boolean {
        super.getUserVisibleHint()

        d("called")
        songListAdapter?.clearItems()
        songListAdapter?.notifyDataSetChanged()
       return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        arguments?.let { songsList = arguments?.getSerializable("songsList") as ArrayList<SongListResults> }
        currentFragmentPos = arguments?.getInt("fragmentPos") ?: -1

        d("song list in fragment $songsList")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val view =  inflater.inflate(R.layout.layout_songs_list,container,false)

        return  view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        d("onActivity Created Called $songsList")

        val layoutManager = LinearLayoutManager(context)
        rvSongs.layoutManager = layoutManager
        songListAdapter = rvSongs.adapter as? BaseRvAdapter<SongListResults, SongListViewHolder>
        songListAdapter?.clearItems()

        songsList?.let {

            displaySongList(it)
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
                            songListAdapter?.notifyItemChanged(oldSeletectedEvent!!.adapterPosition)
                        }
                    }

                    if( it.fragmentPosition == currentFragmentPos){

                        songsList[it.adapterPosition].isSelected = true
                        songListAdapter?.notifyItemChanged(it.adapterPosition)
                        if (songsList[it.adapterPosition].previewUrl !== null) {
                            val intent = Intent(context, PlaySongActivity::class.java)
                            intent.putExtra("songName", songsList[it.adapterPosition])
                            context?.startActivity(intent)
                        } else Toast.makeText(context, "This song preview url is not available", Toast.LENGTH_SHORT).show()

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

    private fun displaySongList(songList : ArrayList<SongListResults>) {


        if (songList.isNotEmpty()) {
            tvEmptyText.visibility = View.GONE
            songListAdapter = BaseRvAdapter(context!!, R.layout.rv_song_items,
                    {
                        SongListViewHolder(it)
                    }, songList, {

                holder, songModel, position ->
                holder.bind(songModel,position,currentFragmentPos) {

                    songListAdapter?.notifyDataSetChanged()
                    d("called position $position")

                }

            })


            rvSongs.adapter = songListAdapter
            songListAdapter?.notifyDataSetChanged()
        }

        else {

            tvEmptyText.visibility = View.VISIBLE
            tvEmptyText.text = getString(R.string.no_results)
        }


    }

}