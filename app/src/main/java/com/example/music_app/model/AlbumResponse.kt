package com.example.music_app.model

data class AlbumResponse(
    val data: List<Album>
)

data class Album(
    val id: Int = 0,
    val title: String = "",
    val cover: String = "",
    val tracklist: String = ""
)