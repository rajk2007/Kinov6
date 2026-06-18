#!/bin/bash

# HomeScreen.kt
cat <<EOF > app/src/main/java/com/lagradost/cloudstream3/ui/screens/HomeScreen.kt
package com.lagradost.cloudstream3.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lagradost.cloudstream3.ui.components.*
import com.lagradost.cloudstream3.ui.theme.Background
import com.lagradost.cloudstream3.ui.theme.KINO_Red
import com.lagradost.cloudstream3.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val trending by viewModel.trending.collectAsState()
    val popularMovies by viewModel.popularMovies.collectAsState()
    val popularTv by viewModel.popularTv.collectAsState()
    val anime by viewModel.anime.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(Background)) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = KINO_Red
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Box {
                        HeroBanner(trending)
                        HomeHeader()
                    }
                }
                item {
                    CategoryPills()
                }
                item {
                    ContentRow("Trending Now", trending)
                }
                item {
                    ContentRow("Popular Movies", popularMovies)
                }
                item {
                    ContentRow("TV Shows", popularTv)
                }
                item {
                    ContentRow("Anime", anime)
                }
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}
EOF

# Update MainScreen.kt to use the real HomeScreen
sed -i 's/is Screen.Home -> PlaceholderScreen("Home")/is Screen.Home -> HomeScreen()/' app/src/main/java/com/lagradost/cloudstream3/ui/screens/MainScreen.kt
EOF
