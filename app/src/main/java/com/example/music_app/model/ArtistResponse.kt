package com.example.music_app.model

data class ArtistResponse(
    val data: List<Artist>
)

data class Artist(
    val id: Int = 0,
    val name: String = "",
    val picture: String = "",
    val nb_album: Int = 0
)
