package com.kanch786.musicapp.viewHolder

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.kanch786.musicapp.PlaySongActivity
import com.kanch786.musicapp.api.SongListResults
import com.kanch786.musicapp.extensions.d
import com.kanch786.musicapp.extensions.getHeightInDp
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.rv_song_items.view.*

class SongListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    fun bind(songListResults: SongListResults) {


        with(itemView) {

            tvSongName.text = songListResults.trackName
            tvArtistName.text = "${songListResults.artistName } | ${songListResults.collectionName}"
            Glide.with(context).load(songListResults.artworkUrl100).into(ivSong)

            itemView.setOnClickListener {
                context.startActivity(Intent(context,PlaySongActivity::class.java)) }
            val displayMetrics = resources.displayMetrics
            d(" height ${cvSongItem.getHeightInDp(displayMetrics)}")

        }
    }
}