package com.kanch786.musicapp.dataManager.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.kanch786.musicapp.api.SongListResults
import com.kanch786.musicapp.extensions.d

@Dao
abstract class SongListDao {

    @Query("Select * From SongListResults")
    abstract fun getFavouriteList() : LiveData<List<SongListResults>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addToFavoriteList(songListResults: SongListResults) : Long


  /*  @Query("Update SongListResults Set isFavorite = :isFavourite")
    abstract fun toggleAsFavourite(isFavourite : Boolean) : Long*/

    @Query("Select* From SongListResults where trackId = :trackId")
    abstract fun getSongById(trackId: Long) : LiveData<List<SongListResults>>


    @Delete
    abstract fun deleteAsFavourite(song: SongListResults) : Int

    //if toggling is required
   /* fun updateFavourite(songListResults: SongListResults ) : Boolean {

        d("toggle favourite called with ${songListResults.isFavorite}")
        songListResults.isFavorite = !songListResults.isFavorite
        return (toggleAsFavourite(songListResults.isFavorite) != -1L)

    }*/
}