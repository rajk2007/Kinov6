#!/bin/bash

# MainScreen.kt
cat <<EOF > app/src/main/java/com/lagradost/cloudstream3/ui/screens/MainScreen.kt
package com.lagradost.cloudstream3.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.lagradost.cloudstream3.ui.theme.Background
import com.lagradost.cloudstream3.ui.theme.KINO_Red
import com.lagradost.cloudstream3.ui.theme.TextMuted
import com.lagradost.cloudstream3.ui.theme.TextPrimary

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Search : Screen("search", "Search", Icons.Default.Search)
    object Library : Screen("library", "Library", Icons.Default.List)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
}

@Composable
fun MainScreen() {
    var selectedScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    val items = listOf(Screen.Home, Screen.Search, Screen.Library, Screen.Profile)

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Background.copy(alpha = 0.97f),
                contentColor = TextMuted,
            ) {
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = selectedScreen == screen,
                        onClick = { selectedScreen = screen },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = KINO_Red,
                            selectedTextColor = KINO_Red,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (selectedScreen) {
                is Screen.Home -> PlaceholderScreen("Home")
                is Screen.Search -> PlaceholderScreen("Search")
                is Screen.Library -> PlaceholderScreen("Library")
                is Screen.Profile -> PlaceholderScreen("Profile")
            }
        }
    }
}

@Composable
fun PlaceholderScreen(name: String) {
    Text(text = "$name Screen", color = TextPrimary, style = MaterialTheme.typography.headlineLarge)
}
EOF

# Update MainActivity.kt to include the navigation flow
cat <<EOF > app/src/main/java/com/lagradost/cloudstream3/MainActivity.kt
package com.lagradost.cloudstream3

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.lagradost.cloudstream3.ui.screens.MainScreen
import com.lagradost.cloudstream3.ui.screens.RepoInstallerScreen
import com.lagradost.cloudstream3.ui.screens.SplashScreen
import com.lagradost.cloudstream3.ui.theme.KINOTheme

enum class AppState {
    Splash, RepoInstaller, Main
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val reposInstalled = sharedPref.getBoolean("kino_repos_installed", false)

        setContent {
            KINOTheme {
                var currentState by remember { mutableStateOf(AppState.Splash) }
                
                when (currentState) {
                    AppState.Splash -> {
                        SplashScreen(onNavigateToMain = {
                            currentState = if (reposInstalled) AppState.Main else AppState.RepoInstaller
                        })
                    }
                    AppState.RepoInstaller -> {
                        RepoInstallerScreen(onFinished = {
                            with(sharedPref.edit()) {
                                putBoolean("kino_repos_installed", true)
                                apply()
                            }
                            currentState = AppState.Main
                        })
                    }
                    AppState.Main -> {
                        MainScreen()
                    }
                }
            }
        }
    }
}
EOF
