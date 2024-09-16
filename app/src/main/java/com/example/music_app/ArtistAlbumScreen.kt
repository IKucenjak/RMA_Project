package com.example.music_app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.music_app.model.Album
import com.example.music_app.viewmodel.AlbumsViewModel

@Composable
fun ArtistAlbumsScreen(
    artistId: Int,
    viewModel: AlbumsViewModel = viewModel()
) {
    val context = LocalContext.current
    val albums by viewModel.albums.collectAsState()

    LaunchedEffect(artistId) {
        viewModel.fetchAlbums(artistId)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        albums?.let { albumList ->
            items(albumList) { album ->
                AlbumItem(album, onSaveClick = {
                    viewModel.saveAlbum(album, context)
                })
            }
        }
    }
}

@Composable
fun AlbumItem(
    album: Album,
    onSaveClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(album.cover),
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .padding(end = 8.dp),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = album.title)
        }
        Icon(
            imageVector = Icons.Filled.AddCircle,
            contentDescription = "Save Album",
            modifier = Modifier
                .size(24.dp)
                .clickable { onSaveClick() }
        )
    }
}
