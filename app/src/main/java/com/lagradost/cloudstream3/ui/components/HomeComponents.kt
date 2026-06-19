@file:OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
package com.lagradost.cloudstream3.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.lagradost.cloudstream3.data.models.MediaItem
import com.lagradost.cloudstream3.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.math.abs

@Composable
fun HomeHeader(onSearchClick: () -> Unit) {
    val context = LocalContext.current
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
            IconButton(onClick = { 
                Toast.makeText(context, "No new notifications", Toast.LENGTH_SHORT).show()
            }) {
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
        itemsIndexed(categories) { _, category ->
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

    val infiniteTransition = rememberInfiniteTransition(label = "KenBurns")
    val kenBurnsScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Scale"
    )

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
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(
                            scaleX = kenBurnsScale,
                            scaleY = kenBurnsScale
                        )
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
                    Text(text = "${item.displayDate} • ⭐ ${String.format("%.1f", item.voteAverage)}", color = TextMuted, fontSize = 14.sp)

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
    val configuration = LocalConfiguration.current
    val screenWidthPx = configuration.screenWidthDp * configuration.densityDpi / 160f

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
            itemsIndexed(items) { index, item ->
                val itemInfo = listState.layoutInfo.visibleItemsInfo.find { it.index == index }
                val scale = if (itemInfo != null) {
                    val center = (itemInfo.offset + itemInfo.size / 2).toFloat()
                    val distanceFromCenter = abs(screenWidthPx / 2 - center)
                    val normalizedDistance = (distanceFromCenter / (screenWidthPx / 2)).coerceIn(0f, 1f)
                    1f - (normalizedDistance * 0.15f)
                } else 1f

                val alpha = if (itemInfo != null) {
                    val center = (itemInfo.offset + itemInfo.size / 2).toFloat()
                    val distanceFromCenter = abs(screenWidthPx / 2 - center)
                    val normalizedDistance = (distanceFromCenter / (screenWidthPx / 2)).coerceIn(0f, 1f)
                    1f - (normalizedDistance * 0.6f)
                } else 1f

                MediaCard(
                    item = item, 
                    onMediaClick = onMediaClick,
                    modifier = Modifier.graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        alpha = alpha
                    )
                )
            }
        }
    }
}

@Composable
fun MediaCard(
    item: MediaItem, 
    onMediaClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressScale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "PressScale")

    Column(
        modifier = modifier
            .width(130.dp)
            .scale(pressScale)
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
