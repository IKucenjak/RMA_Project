package com.example.music_app.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.music_app.model.Album
import com.example.music_app.viewmodel.LibraryViewModel

@Composable
fun LibraryScreen(
    navController: NavHostController,
    libraryViewModel: LibraryViewModel = viewModel()
) {
    val context = LocalContext.current
    val savedAlbums by libraryViewModel.savedAlbums.collectAsState()
    val albumCount by libraryViewModel.albumCount.collectAsState()
    val searchQuery by libraryViewModel.searchQuery.collectAsState()

    LaunchedEffect(Unit) {
        libraryViewModel.fetchSavedAlbums()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Your Library",
            style = androidx.compose.material3.MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Search Albums",
            style = androidx.compose.material3.MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = searchQuery,
            onValueChange = { libraryViewModel.setSearchQuery(it) },
            label = { Text("Search") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Saved Albums ($albumCount)",
            style = androidx.compose.material3.MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(savedAlbums.filter {
                it.title.contains(
                    searchQuery,
                    ignoreCase = true
                )
            }) { album ->
                LibraryAlbumItem(
                    album = album,
                    onAlbumClick = {
                    },
                    onDeleteClick = {
                        libraryViewModel.deleteAlbum(album) {
                            Toast.makeText(
                                context,
                                "The album has been removed from the library",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun LibraryAlbumItem(
    album: Album,
    onAlbumClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onAlbumClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(album.cover),
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = album.title,
                style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
            )
        }

        IconButton(onClick = onDeleteClick) {
            Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete Album")
        }
    }
}
