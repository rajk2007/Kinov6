package com.lagradost.cloudstream3.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    val rows by viewModel.rows.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    val filteredRows = remember(selectedCategory, rows) {
        when (selectedCategory) {
            "Trending" -> listOf(
                "Trending Now", "Popular on KINO", "New Releases",
                "Hollywood Movies", "Bollywood Movies", "Korean Drama & Movies",
                "Japanese Movies", "Anime", "TV Series", "Top Rated",
                "Action & Adventure", "Comedy", "Horror & Thriller", "Sci-Fi & Fantasy"
            )
            "Movies" -> listOf(
                "Now Playing", "Popular Movies", "Top Rated Movies", 
                "Upcoming Movies", "Action Movies", "Comedy Movies", 
                "Horror Movies", "Sci-Fi Movies"
            )
            "Series" -> listOf(
                "Popular TV", "Top Rated TV", "Airing Today", 
                "Crime TV", "Drama TV", "Sci-Fi TV"
            )
            "Anime" -> listOf(
                "Popular Anime", "Top Rated Anime", "Action Anime", 
                "Romance Anime", "Fantasy Anime"
            )
            else -> rows.keys.toList()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Background)) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = KINO_Red)
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Box {
                        HeroBanner(rows["Trending Now"] ?: emptyList(), onMediaClick)
                        HomeHeader(onSearchClick)
                    }
                }
                item {
                    CategoryPills(selectedCategory) { viewModel.setCategory(it) }
                }
                
                items(filteredRows) { rowTitle ->
                    rows[rowTitle]?.let { items ->
                        if (items.isNotEmpty()) {
                            ContentRow(title = rowTitle, items = items, onMediaClick = onMediaClick)
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}
