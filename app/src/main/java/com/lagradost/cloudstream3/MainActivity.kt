package com.lagradost.cloudstream3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.lagradost.cloudstream3.ui.screens.SplashScreen
import com.lagradost.cloudstream3.ui.theme.KINOTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KINOTheme {
                var showSplash by remember { mutableStateOf(true) }
                
                if (showSplash) {
                    SplashScreen(onNavigateToMain = { showSplash = false })
                } else {
                    // Placeholder for Task 4/5
                    // MainContent()
                }
            }
        }
    }
}
