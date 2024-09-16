package com.example.music_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.music_app.model.Album
import com.example.music_app.network.RetrofitInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.widget.Toast

class AlbumsViewModel : ViewModel() {
    private val _albums = MutableStateFlow<List<Album>?>(null)
    val albums: StateFlow<List<Album>?> = _albums

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun fetchAlbums(artistId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getArtistAlbums(artistId)
                _albums.value = response.data
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun saveAlbum(album: Album, context: android.content.Context) {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId).collection("saved_albums").document(album.id.toString())
            .set(album)
            .addOnSuccessListener {
                Toast.makeText(context, "The album has been added to the library", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }
}
