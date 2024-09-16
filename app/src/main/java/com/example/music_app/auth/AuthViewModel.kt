package com.example.music_app.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        firebaseAuth.addAuthStateListener { auth ->
            val currentUser = auth.currentUser
            _authState.value = if (currentUser != null) {
                AuthState.Success(currentUser.uid)
            } else {
                AuthState.Unauthenticated
            }
        }
    }

    fun loginUser(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("One or more fields are left empty.")
            return
        }

        _authState.value = AuthState.Loading
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Success("Logged in successfully!")
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Login failed")
                }
            }
    }

    fun registerUser(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("One or more fields are left empty.")
            return
        }

        _authState.value = AuthState.Loading
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Success("Registered successfully!")
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Registration failed")
                }
            }
    }

    fun logoutUser() {
        viewModelScope.launch {
            firebaseAuth.signOut()
            _authState.value = AuthState.Unauthenticated
        }
    }

    val userEmail: String?
        get() = firebaseAuth.currentUser?.email
}