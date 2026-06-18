package com.lagradost.cloudstream3.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lagradost.cloudstream3.ui.components.*
import com.lagradost.cloudstream3.ui.theme.Background
import com.lagradost.cloudstream3.ui.theme.KINO_Red
import com.lagradost.cloudstream3.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onMediaClick: (Int) -> Unit,
    onSearchClick: () -> Unit
) {
    val trending by viewModel.trending.collectAsState()
    val popularMovies by viewModel.popularMovies.collectAsState()
    val popularTv by viewModel.popularTv.collectAsState()
    val topRated by viewModel.topRated.collectAsState()
    val upcoming by viewModel.upcoming.collectAsState()
    val indianMovies by viewModel.indianMovies.collectAsState()
    val anime by viewModel.anime.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(Background)) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = KINO_Red)
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Box {
                        HeroBanner(trending, onMediaClick)
                        HomeHeader(onSearchClick)
                    }
                }
                item {
                    CategoryPills(selectedCategory) { viewModel.setCategory(it) }
                }
                
                when (selectedCategory) {
                    "Trending" -> {
                        item { ContentRow("Trending Now", trending, onMediaClick) }
                        item { ContentRow("Top Rated", topRated, onMediaClick) }
                    }
                    "Movies" -> {
                        item { ContentRow("Popular Movies", popularMovies, onMediaClick) }
                        item { ContentRow("Upcoming", upcoming, onMediaClick) }
                    }
                    "Series" -> {
                        item { ContentRow("Popular TV Shows", popularTv, onMediaClick) }
                    }
                    "Anime" -> {
                        item { ContentRow("Latest Anime", anime, onMediaClick) }
                    }
                    "Hindi" -> {
                        item { ContentRow("Popular Indian Movies", indianMovies, onMediaClick) }
                    }
                }
                
                // Always show some generic rows if not in a specific filter
                if (selectedCategory == "Trending") {
                    item { ContentRow("Indian Hits", indianMovies, onMediaClick) }
                    item { ContentRow("Upcoming", upcoming, onMediaClick) }
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}
