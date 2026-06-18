#!/bin/bash
mkdir -p app/src/main/java/com/lagradost/cloudstream3/ui/screens

# SplashScreen.kt
cat <<EOF > app/src/main/java/com/lagradost/cloudstream3/ui/screens/SplashScreen.kt
package com.lagradost.cloudstream3.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lagradost.cloudstream3.ui.theme.Background
import com.lagradost.cloudstream3.ui.theme.KINO_Red
import com.lagradost.cloudstream3.ui.theme.TextDarkGray
import com.lagradost.cloudstream3.ui.theme.TextMuted
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToMain: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(3000)
        onNavigateToMain()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "KINO",
                color = KINO_Red,
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = "by Raj Karmakar",
                color = TextMuted,
                fontSize = 14.sp,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "CINEMA. REDEFINED.",
                color = TextDarkGray,
                fontSize = 10.sp,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
EOF

# MainActivity.kt
cat <<EOF > app/src/main/java/com/lagradost/cloudstream3/MainActivity.kt
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
EOF
