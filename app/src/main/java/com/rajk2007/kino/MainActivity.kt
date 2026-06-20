
package com.rajk2007.kino

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val sharedPreferences = remember { context.getSharedPreferences("kino_prefs", Context.MODE_PRIVATE) }

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController, sharedPreferences)
        }
        composable("installer") {
            RepoInstallerScreen(navController, sharedPreferences)
        }
        composable("home") {
            HomeScreen() // Placeholder for the actual Home Screen
        }
    }
}

@Composable
fun SplashScreen(navController: NavController, sharedPreferences: SharedPreferences) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        delay(3000) // Show splash screen for 3 seconds
        val reposInstalled = sharedPreferences.getBoolean("kino_repos_installed", false)
        if (reposInstalled) {
            navController.navigate("home") { popUpTo("splash") { inclusive = true } }
        } else {
            navController.navigate("installer") { popUpTo("splash") { inclusive = true } }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Welcome to KINO", style = MaterialTheme.typography.headlineLarge)
    }
}

@Composable
fun RepoInstallerScreen(navController: NavController, sharedPreferences: SharedPreferences) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }
    var hasError by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    fun installRepos() {
        isLoading = true
        hasError = false
        scope.launch {
            try {
                // Simulate network call
                delay(2000) // Simulate installation time
                val success = (0..1).random() == 1 // Simulate success/failure

                if (success) {
                    sharedPreferences.edit().putBoolean("kino_repos_installed", true).apply()
                    navController.navigate("home") { popUpTo("installer") { inclusive = true } }
                } else {
                    hasError = true
                    Toast.makeText(context, "Network error, please retry", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                hasError = true
                Toast.makeText(context, "Network error, please retry", Toast.LENGTH_LONG).show()
            } finally {
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            CircularProgressIndicator()
            Text("Installing repositories...")
        } else if (hasError) {
            Text("Installation failed.")
            Button(onClick = { installRepos() }) {
                Text("Retry")
            }
            Button(onClick = { navController.navigate("home") { popUpTo("installer") { inclusive = true } } }) {
                Text("Skip to Home")
            }
        } else {
            Text("Press to install KINO repositories")
            Button(onClick = { installRepos() }) {
                Text("Install Repos")
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Welcome to Home Screen", style = MaterialTheme.typography.headlineLarge)
    }
}
