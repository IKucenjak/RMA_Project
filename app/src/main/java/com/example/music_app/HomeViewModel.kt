package com.example.music_app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.music_app.model.Artist
import com.example.music_app.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _artists = MutableStateFlow<List<Artist>>(emptyList())
    val artists: StateFlow<List<Artist>> = _artists

    fun searchArtists(query: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.searchArtists(query)
                _artists.value = response.data
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
