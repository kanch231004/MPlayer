package com.kanch786.musicapp.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kanch786.musicapp.R
import com.kanch786.musicapp.api.SongListResults
import com.kanch786.musicapp.base.BaseRvAdapter
import com.kanch786.musicapp.extensions.d
import com.kanch786.musicapp.viewHolder.SongListViewHolder
import kotlinx.android.synthetic.main.layout_songs_list.*
import kotlinx.android.synthetic.main.layout_songs_list.view.*

class NewFragmentInstanceList : Fragment() {

    private  var songsList = ArrayList<SongListResults>()
    private lateinit var songListAdapter : BaseRvAdapter<SongListResults, SongListViewHolder>

    companion object Factory {

        fun create(argument : Bundle) : Fragment {

            val newFragmentInstanceList = NewFragmentInstanceList()
            newFragmentInstanceList.arguments = argument
            return newFragmentInstanceList
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        songsList.clear()

        d("song list in fragment $songsList")
        arguments?.let { songsList = arguments?.getSerializable("songsList") as ArrayList<SongListResults> }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       return inflater.inflate(R.layout.layout_songs_list,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        rvSongs.layoutManager = layoutManager
        songsList?.let { displaySongList(it)}
    }

    private fun displaySongList(songList : ArrayList<SongListResults>) {


        songListAdapter = BaseRvAdapter(context!!, R.layout.rv_song_items,
                {
                    SongListViewHolder(it)
                },songList, {

            holder, songModel, position -> holder.bind(songModel)

        })

        rvSongs.adapter = songListAdapter
        songListAdapter.notifyDataSetChanged()


    }


}