package com.kanch786.musicapp.viewHolder

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.kanch786.musicapp.R
import com.kanch786.musicapp.main.songs.PlaySongActivity
import com.kanch786.musicapp.api.SongListResults
import com.kanch786.musicapp.extensions.d
import com.kanch786.musicapp.extensions.getHeightInDp
import kotlinx.android.synthetic.main.rv_song_items.view.*

var selectedPos = -1
class SongListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {


    fun bind(songListResults: SongListResults,position : Int,action : (Int) -> Unit) {


        with(itemView) {

            tvSongName.text = songListResults.trackName
            tvArtistName.text = "${songListResults.artistName } | ${songListResults.collectionName}"
            Glide.with(context).load(songListResults.artworkUrl100).into(ivSong)

            itemView.setOnClickListener {
                selectedPos = position
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorHighlightGray))

                if (songListResults.previewUrl !== null) {
                    val intent = Intent(context, PlaySongActivity::class.java)
                    intent.putExtra("songName", songListResults)
                    context.startActivity(intent)
                }

                else Toast.makeText(context,"This song preview url is not available",Toast.LENGTH_SHORT).show()
            }
            val displayMetrics = resources.displayMetrics
            d(" height ${cvSongItem.getHeightInDp(displayMetrics)}")

            if(selectedPos == adapterPosition)
                itemView.setBackgroundColor( ContextCompat.getColor(context, R.color.colorHighlightGray))
            else   itemView.setBackgroundColor( ContextCompat.getColor(context, R.color.colorAccent))

        }
    }
}