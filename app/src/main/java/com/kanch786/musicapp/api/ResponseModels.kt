package com.kanch786.musicapp.api
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable


@Entity
data class SongListResults(

    @PrimaryKey @SerializedName("trackId") val trackId : Long,
    @SerializedName("wrapperType") val wrapperType: String?,
    @SerializedName("artistId") val artistId: Int,
    @SerializedName("collectionId") val collectionId: Int,
    @SerializedName("amgArtistId") val amgArtistId: Int,
    @SerializedName("artistName") val artistName: String?,
    @SerializedName("collectionName") val collectionName: String?,
    @SerializedName("trackName") val trackName : String?,
    @SerializedName("collectionCensoredName") val collectionCensoredName: String?,
    @SerializedName("artistViewUrl") val artistViewUrl: String?,
    @SerializedName("collectionViewUrl") val collectionViewUrl: String?,
    @SerializedName("trackViewUrl") val trackViewUrl : String?,
    @SerializedName("artworkUrl60") val artworkUrl60: String?,
    @SerializedName("artworkUrl100") val artworkUrl100: String?,
    @SerializedName("collectionPrice") val collectionPrice: Double,
    @SerializedName("collectionExplicitness") val collectionExplicitness: String?,
    @SerializedName("trackCount") val trackCount: Int,
    @SerializedName("trackTimeMillis") val trackTimeMillis : Long,
    @SerializedName("copyright") val copyright: String?,
    @SerializedName("country") val country: String?,
    @SerializedName("currency") val currency: String?,
    @SerializedName("releaseDate") val releaseDate: String?,
    @SerializedName("primaryGenreName") val primaryGenreName: String?,
    @SerializedName("previewUrl") val previewUrl: String?,
    @SerializedName("description") val description: String?,
    var isFavorite : Boolean = false
) : Serializable

@Entity
data class SuggestionsList( @PrimaryKey var suggestions : String)

data class SongListResponse(
        @SerializedName("resultCount") val resultCount: Int,
        @SerializedName("results") val results: List<SongListResults>)