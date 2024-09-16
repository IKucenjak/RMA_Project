package com.example.music_app

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.music_app.auth.AuthViewModel
import com.example.music_app.model.Artist

@Composable
fun HomeScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel(),
    homeViewModel: HomeViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val artists by homeViewModel.artists.collectAsState()
    val userEmail by remember { mutableStateOf(authViewModel.userEmail) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Search Artists",
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge
            )

            Row {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "Library",
                    modifier = Modifier
                        .size(24.dp)
                        .offset(x = (-16).dp, y = 10.dp)
                        .clickable {
                            navController.navigate("library")
                        }
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(onClick = { authViewModel.logoutUser() }) {
                    Text(text = "Logout")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = userEmail ?: "User",
            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = searchQuery,
            onValueChange = { newQuery ->
                searchQuery = newQuery
                if (newQuery.text.isNotEmpty()) {
                    homeViewModel.searchArtists(newQuery.text)
                }
            },
            label = { Text("Enter artist name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(artists) { artist ->
                ArtistItem(artist = artist, onArtistClick = {
                    navController.navigate("albums/${artist.id}")
                })
            }
        }
    }
}

@Composable
fun ArtistItem(artist: Artist, onArtistClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onArtistClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(artist.picture),
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = artist.name,
                style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Albums: ${artist.nb_album}",
                style = androidx.compose.material3.MaterialTheme.typography.bodySmall
            )
        }
    }
}