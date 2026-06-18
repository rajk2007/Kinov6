package com.lagradost.cloudstream3.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.lagradost.cloudstream3.data.models.MediaItem
import com.lagradost.cloudstream3.data.repository.TmdbRepository
import com.lagradost.cloudstream3.ui.components.ContentRow
import com.lagradost.cloudstream3.ui.theme.Background
import com.lagradost.cloudstream3.ui.theme.KINO_Red
import com.lagradost.cloudstream3.ui.theme.TextMuted
import com.lagradost.cloudstream3.ui.theme.TextPrimary

@Composable
fun DetailsScreen(
    mediaId: Int,
    onBackClick: () -> Unit,
    onMediaClick: (Int) -> Unit
) {
    val repository = remember { TmdbRepository() }
    var mediaItem by remember { mutableStateOf<MediaItem?>(null) }
    var similarItems by remember { mutableStateOf<List<MediaItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(mediaId) {
        isLoading = true
        try {
            mediaItem = repository.getMovieDetails(mediaId)
            similarItems = repository.getSimilarMovies(mediaId)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Background)) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = KINO_Red)
        } else {
            mediaItem?.let { item ->
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
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
                                            startY = 100f
                                        )
                                    )
                            )
                            IconButton(
                                onClick = onBackClick,
                                modifier = Modifier.padding(16.dp).align(Alignment.TopStart).background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(50))
                            ) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                            }
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp).offset(y = (-60).dp),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            AsyncImage(
                                model = item.fullPosterPath,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.width(120.dp).height(180.dp).clip(RoundedCornerShape(12.dp)).border(2.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.padding(bottom = 8.dp)) {
                                Text(text = item.displayTitle, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${item.displayDate} • ⭐ ${String.format("%.1f", item.voteAverage)} • 124 min",
                                    color = TextMuted,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    item {
                        Column(modifier = Modifier.padding(horizontal = 16.dp).offset(y = (-40).dp)) {
                            Button(
                                onClick = { },
                                colors = ButtonDefaults.buttonColors(containerColor = KINO_Red),
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier.fillMaxWidth().height(50.dp)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Watch Now", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            var isExpanded by remember { mutableStateOf(false) }
                            Text(
                                text = item.overview ?: "No description available.",
                                color = TextPrimary,
                                fontSize = 15.sp,
                                lineHeight = 22.sp,
                                maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.clickable { isExpanded = !isExpanded }
                            )
                            if (!isExpanded && (item.overview?.length ?: 0) > 100) {
                                Text(text = "Read more", color = KINO_Red, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { isExpanded = true })
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        ContentRow(title = "More Like This", items = similarItems, onMediaClick = onMediaClick)
                    }
                    
                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}
