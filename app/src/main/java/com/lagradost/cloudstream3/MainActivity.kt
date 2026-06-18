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
