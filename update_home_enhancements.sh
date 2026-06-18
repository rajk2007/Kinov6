#!/bin/bash

# Update HomeViewModel.kt
cat <<EOF > app/src/main/java/com/lagradost/cloudstream3/ui/viewmodel/HomeViewModel.kt
package com.lagradost.cloudstream3.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lagradost.cloudstream3.data.models.MediaItem
import com.lagradost.cloudstream3.data.repository.TmdbRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val repository = TmdbRepository()

    private val _trending = MutableStateFlow<List<MediaItem>>(emptyList())
    val trending: StateFlow<List<MediaItem>> = _trending

    private val _popularMovies = MutableStateFlow<List<MediaItem>>(emptyList())
    val popularMovies: StateFlow<List<MediaItem>> = _popularMovies

    private val _popularTv = MutableStateFlow<List<MediaItem>>(emptyList())
    val popularTv: StateFlow<List<MediaItem>> = _popularTv

    private val _topRated = MutableStateFlow<List<MediaItem>>(emptyList())
    val topRated: StateFlow<List<MediaItem>> = _topRated

    private val _upcoming = MutableStateFlow<List<MediaItem>>(emptyList())
    val upcoming: StateFlow<List<MediaItem>> = _upcoming

    private val _indianMovies = MutableStateFlow<List<MediaItem>>(emptyList())
    val indianMovies: StateFlow<List<MediaItem>> = _indianMovies

    private val _anime = MutableStateFlow<List<MediaItem>>(emptyList())
    val anime: StateFlow<List<MediaItem>> = _anime

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _selectedCategory = MutableStateFlow("Trending")
    val selectedCategory: StateFlow<String> = _selectedCategory

    init {
        fetchHomeData()
    }

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    fun fetchHomeData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val trendingDef = async { repository.getTrending() }
                val moviesDef = async { repository.getPopularMovies() }
                val tvDef = async { repository.getPopularTv() }
                val topRatedDef = async { repository.getTopRatedMovies() }
                val upcomingDef = async { repository.getUpcomingMovies() }
                val indianDef = async { repository.getIndianMovies() }
                val animeDef = async { repository.getAnime() }

                _trending.value = trendingDef.await()
                _popularMovies.value = moviesDef.await()
                _popularTv.value = tvDef.await()
                _topRated.value = topRatedDef.await()
                _upcoming.value = upcomingDef.await()
                _indianMovies.value = indianDef.await()
                _anime.value = animeDef.await()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
EOF

# Update HomeComponents.kt with Pager, Scroll Effects, and Rating Fix
cat <<EOF > app/src/main/java/com/lagradost/cloudstream3/ui/components/HomeComponents.kt
package com.lagradost.cloudstream3.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.lagradost.cloudstream3.data.models.MediaItem
import com.lagradost.cloudstream3.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun HomeHeader(onSearchClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.Black.copy(alpha = 0.8f), Color.Transparent)
                )
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "KINO",
            color = KINO_Red,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Row {
            IconButton(onClick = onSearchClick) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
            }
            IconButton(onClick = { }) {
                Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.White)
            }
        }
    }
}

@Composable
fun CategoryPills(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    val categories = listOf("Trending", "Movies", "Series", "Anime", "Hindi", "English")

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 12.dp)
    ) {
        items(categories) { category ->
            val isSelected = category == selectedCategory
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onCategorySelected(category) },
                color = if (isSelected) KINO_Red else Color.Transparent,
                border = if (isSelected) null else BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
            ) {
                Text(
                    text = category,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun HeroBanner(trendingItems: List<MediaItem>, onMediaClick: (Int) -> Unit) {
    if (trendingItems.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { minOf(trendingItems.size, 5) })
    
    LaunchedEffect(Unit) {
        while(true) {
            delay(5000)
            val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Box(modifier = Modifier.fillMaxWidth().height(450.dp)) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val item = trendingItems[page]
            Box(modifier = Modifier.fillMaxSize().clickable { onMediaClick(item.id) }) {
                AsyncImage(
                    model = item.fullBackdropPath,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Background),
                                startY = 300f
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(text = item.displayTitle, color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold, lineHeight = 38.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "\${item.displayDate} • ⭐ \${String.format("%.1f", item.voteAverage)} • Action", color = TextMuted, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { onMediaClick(item.id) },
                        colors = ButtonDefaults.buttonColors(containerColor = KINO_Red),
                        shape = RoundedCornerShape(24.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Watch Now", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        
        Row(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { index ->
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (index == pagerState.currentPage) KINO_Red else Color.Gray.copy(alpha = 0.5f))
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
        }
    }
}

@Composable
fun ContentRow(title: String, items: List<MediaItem>, onMediaClick: (Int) -> Unit) {
    val listState = rememberLazyListState()
    
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = "See All", color = KINO_Red, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
        
        LazyRow(
            state = listState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items) { item ->
                MediaCard(item, onMediaClick)
            }
        }
    }
}

@Composable
fun MediaCard(item: MediaItem, onMediaClick: (Int) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f)

    Column(
        modifier = Modifier
            .width(130.dp)
            .scale(scale)
            .clickable(interactionSource = interactionSource, indication = null) { onMediaClick(item.id) }
    ) {
        Box(
            modifier = Modifier
                .height(190.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
        ) {
            AsyncImage(
                model = item.fullPosterPath,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Surface(
                modifier = Modifier.padding(8.dp).align(Alignment.TopStart),
                color = Color.Black.copy(alpha = 0.6f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(text = "HD", color = Color.White, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp), fontWeight = FontWeight.Bold)
            }
            Surface(
                modifier = Modifier.padding(8.dp).align(Alignment.TopEnd),
                color = KINO_Gold.copy(alpha = 0.9f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(text = String.format("%.1f", item.voteAverage), color = Color.Black, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp), fontWeight = FontWeight.Bold, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = item.displayTitle, color = Color.White, fontSize = 14.sp, maxLines = 1, fontWeight = FontWeight.Medium)
    }
}
EOF

# Update HomeScreen.kt with Filtering and New Sections
cat <<EOF > app/src/main/java/com/lagradost/cloudstream3/ui/screens/HomeScreen.kt
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
EOF
