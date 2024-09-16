package com.example.music_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.music_app.model.Album
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LibraryViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _savedAlbums = MutableStateFlow<List<Album>>(emptyList())
    val savedAlbums: StateFlow<List<Album>> = _savedAlbums

    private val _albumCount = MutableStateFlow(0)
    val albumCount: StateFlow<Int> = _albumCount

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        viewModelScope.launch {
            fetchSavedAlbums()
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun deleteAlbum(album: Album, onSuccess: () -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId).collection("saved_albums")
            .document(album.id.toString())
            .delete()
            .addOnSuccessListener {
                fetchSavedAlbums()
                onSuccess()
            }
            .addOnFailureListener {}
    }

    fun fetchSavedAlbums() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId).collection("saved_albums")
            .get()
            .addOnSuccessListener { result ->
                val albums = result.mapNotNull { document ->
                    document.toObject(Album::class.java)
                }.sortedBy { it.title }
                _savedAlbums.value = albums
                _albumCount.value = albums.size
            }
            .addOnFailureListener {}
    }
}