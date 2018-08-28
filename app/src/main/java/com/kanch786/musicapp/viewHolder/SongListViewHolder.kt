package com.kanch786.musicapp.viewHolder

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.kanch786.musicapp.R
import com.kanch786.musicapp.api.SongListResults
import com.kanch786.musicapp.rx.RxBus
import com.kanch786.musicapp.rx.RxSongSelectEvent
import kotlinx.android.synthetic.main.rv_song_items.view.*



class SongListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    fun bind(songListResults: SongListResults,position : Int, fragmentPos : Int = -1,action : (Int) -> Unit) {

        with(itemView) {

            tvSongName.text = songListResults.trackName
            tvArtistName.text = "${songListResults.artistName} | ${songListResults.collectionName}"
            Glide.with(context).load(songListResults.artworkUrl100).into(ivSong)

           val color = ContextCompat.getColor(context, if(songListResults.isSelected) R.color.colorHighlightGray else R.color.colorAccent)
            cvSong.setCardBackgroundColor(color)
            itemView.setOnClickListener {
                RxBus.sendSticky(RxSongSelectEvent(fragmentPos,adapterPosition))

                action(adapterPosition)

            }

        }
    }
}

