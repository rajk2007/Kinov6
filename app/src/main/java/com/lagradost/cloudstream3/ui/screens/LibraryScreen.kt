package com.lagradost.cloudstream3.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.lagradost.cloudstream3.data.models.MediaItem
import com.lagradost.cloudstream3.ui.components.MediaCard
import com.lagradost.cloudstream3.ui.theme.Background
import com.lagradost.cloudstream3.ui.theme.KINO_Red
import com.lagradost.cloudstream3.ui.theme.TextMuted
import com.lagradost.cloudstream3.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(onMediaClick: (Int) -> Unit) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Downloads", "Continue Watching", "Watchlist", "Favorites", "History")

    Column(modifier = Modifier.fillMaxSize().background(Background)) {
        Text(
            text = "My Library",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Background,
            contentColor = KINO_Red,
            edgePadding = 16.dp,
            indicator = { tabPositions ->
                Box(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .height(3.dp)
                        .background(Color.Red)
                )
            },
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            color = if (selectedTabIndex == index) Color.White else TextMuted,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> DownloadsTab()
            1 -> ContinueWatchingTab(onMediaClick)
            else -> MediaGridTab(onMediaClick)
        }
    }
}

@Composable
fun DownloadsTab() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("No downloads yet", color = TextMuted)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = KINO_Red),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Find Something to Download")
            }
        }
    }
}

@Composable
fun ContinueWatchingTab(onMediaClick: (Int) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(5) {
            LandscapeProgressCard()
        }
    }
}

@Composable
fun MediaGridTab(onMediaClick: (Int) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        // Dummy data for grid
        items(10) {
            MediaCard(
                item = MediaItem(
                    id = 1,
                    title = "Sample Movie",
                    name = "Sample Movie",
                    posterPath = "/kuf6evRcwEkH8sn30Sbp9L9B446.jpg",
                    backdropPath = "/kuf6evRcwEkH8sn30Sbp9L9B446.jpg",
                    releaseDate = "2024",
                    firstAirDate = "2024",
                    voteAverage = 8.5,
                    genreIds = emptyList(),
                    overview = "Sample Overview",
                    popularity = 0.0
                ),
                onMediaClick = onMediaClick
            )
        }
    }
}

@Composable
fun LandscapeProgressCard() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w780/kuf6evRcwEkH8sn30Sbp9L9B446.jpg",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(Color.Gray.copy(alpha = 0.5f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .fillMaxHeight()
                        .background(KINO_Red)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Wednesday",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "S1:E5 • 24m left",
            color = TextMuted,
            fontSize = 12.sp
        )
    }
}
