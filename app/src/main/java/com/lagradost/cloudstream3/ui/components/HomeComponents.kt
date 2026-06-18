package com.lagradost.cloudstream3.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.lagradost.cloudstream3.data.models.MediaItem
import com.lagradost.cloudstream3.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun HomeHeader() {
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
            IconButton(onClick = { }) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
            }
            IconButton(onClick = { }) {
                Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.White)
            }
        }
    }
}

@Composable
fun CategoryPills() {
    val categories = listOf("Trending", "Movies", "Series", "Anime", "Hindi", "English")
    var selectedCategory by remember { mutableStateOf("Trending") }

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
                    .clickable { selectedCategory = category },
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
fun HeroBanner(trendingItems: List<MediaItem>) {
    if (trendingItems.isEmpty()) return

    var currentIndex by remember { mutableIntStateOf(0) }
    
    LaunchedEffect(Unit) {
        while(true) {
            delay(5000)
            currentIndex = (currentIndex + 1) % trendingItems.size
        }
    }

    val item = trendingItems[currentIndex]

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(450.dp)
    ) {
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
            Text(
                text = item.displayTitle,
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 38.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${item.displayDate} • ⭐ ${item.voteAverage} • Action",
                color = TextMuted,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = KINO_Red),
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Watch Now", fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Progress Dots
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(minOf(trendingItems.size, 5)) { index ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (index == currentIndex % 5) KINO_Red else Color.Gray.copy(alpha = 0.5f))
                            .padding(horizontal = 4.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }
            }
        }
    }
}

@Composable
fun ContentRow(title: String, items: List<MediaItem>) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "See All",
                color = KINO_Red,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
        
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items) { item ->
                MediaCard(item)
            }
        }
    }
}

@Composable
fun MediaCard(item: MediaItem) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f)

    Column(
        modifier = Modifier
            .width(130.dp)
            .scale(scale)
            .clickable(interactionSource = interactionSource, indication = null) { }
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
            
            // HD Badge
            Surface(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopStart),
                color = Color.Black.copy(alpha = 0.6f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "HD",
                    color = Color.White,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            // Rating Badge
            Surface(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopEnd),
                color = KINO_Gold.copy(alpha = 0.9f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = item.voteAverage.toString(),
                    color = Color.Black,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                    fontWeight = FontWeight.Bold,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = item.displayTitle,
            color = Color.White,
            fontSize = 14.sp,
            maxLines = 1,
            fontWeight = FontWeight.Medium
        )
    }
}
