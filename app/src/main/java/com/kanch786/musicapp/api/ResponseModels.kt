package com.kanch786.musicapp.api
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable


@Entity
data class SongListResults(

    @PrimaryKey @SerializedName("trackId") var trackId : Long,
    @SerializedName("artistId") var artistId: Int,
    @SerializedName("collectionId") var collectionId: Int,
    @SerializedName("amgArtistId") var amgArtistId: Int,
    @SerializedName("artistName") var artistName: String?,
    @SerializedName("collectionName") var collectionName: String?,
    @SerializedName("trackName") var trackName : String?,
    @SerializedName("artistViewUrl") var artistViewUrl: String?,
    @SerializedName("collectionViewUrl") var collectionViewUrl: String?,
    @SerializedName("trackViewUrl") var trackViewUrl : String?,
    @SerializedName("artworkUrl60") var artworkUrl60: String?,
    @SerializedName("artworkUrl100") var artworkUrl100: String?,
    @SerializedName("trackCount") var trackCount: Int,
    @SerializedName("trackTimeMillis") var trackTimeMillis : Long,
    @SerializedName("primaryGenreName") var primaryGenreName: String?,
    @SerializedName("previewUrl") var previewUrl: String?,
    var isFavorite : Boolean = false,
    @Ignore var isSelected : Boolean = false
) : Serializable {

    constructor() : this(-1,-1,-1,-1,null, null,null,null,null,null,null,null,-1,0,null,null,false)
}

@Entity
data class SuggestionsList( @PrimaryKey var suggestions : String)

data class SongListResponse(
        @SerializedName("resultCount") val resultCount: Int,
        @SerializedName("results") val results: List<SongListResults>)