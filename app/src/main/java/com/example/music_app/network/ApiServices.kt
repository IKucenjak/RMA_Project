package com.example.music_app.network

import com.example.music_app.model.AlbumResponse
import com.example.music_app.model.ArtistResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("chart/0/artists") // Example endpoint for popular artists
    suspend fun getPopularArtists(): ArtistResponse

    @GET("search/artist")
    suspend fun searchArtists(
        @Query("q") query: String // For searching artists
    ): ArtistResponse

    @GET("artist/{artist_id}/albums")
    suspend fun getArtistAlbums(
        @Path("artist_id") artistId: Int
    ): AlbumResponse
}
