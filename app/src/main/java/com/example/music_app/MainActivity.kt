package com.example.music_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import com.example.music_app.auth.AuthScreen
import com.example.music_app.auth.AuthState
import com.example.music_app.auth.AuthViewModel
import com.example.music_app.ui.ArtistAlbumsScreen
import com.example.music_app.ui.LibraryScreen
import com.example.music_app.ui.theme.Music_appTheme
import com.example.music_app.viewmodel.LibraryViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Music_appTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val authViewModel: AuthViewModel = viewModel()
                    val authState by authViewModel.authState.collectAsState()

                    val navController = rememberNavController()

                    when (authState) {
                        is AuthState.Success -> {
                            AppNavigation(navController = navController, authViewModel = authViewModel)
                        }
                        else -> {
                            AuthScreen(authViewModel = authViewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController, authViewModel: AuthViewModel) {
    val libraryViewModel: LibraryViewModel = viewModel()

    NavHost(navController = navController, startDestination = "artist_list") {
        composable("artist_list") {
            HomeScreen(navController = navController, authViewModel = authViewModel)
        }
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("albums/{artistId}") { backStackEntry ->
            val artistId = backStackEntry.arguments?.getString("artistId")?.toIntOrNull()
            artistId?.let {
                ArtistAlbumsScreen(artistId = it)
            }
        }
        composable("library") {
            LibraryScreen(navController = navController, libraryViewModel = libraryViewModel)
        }
    }
}
