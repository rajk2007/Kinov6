package com.lagradost.cloudstream3.ui.screens

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.lagradost.cloudstream3.data.models.MediaItem
import com.lagradost.cloudstream3.ExtractorLink
import com.lagradost.cloudstream3.ui.components.ContentRow
import com.lagradost.cloudstream3.ui.theme.Background
import com.lagradost.cloudstream3.ui.theme.KINO_Red
import com.lagradost.cloudstream3.ui.theme.TextMuted
import com.lagradost.cloudstream3.ui.theme.TextPrimary
import com.lagradost.cloudstream3.ui.viewmodel.DetailsUiState
import com.lagradost.cloudstream3.ui.viewmodel.DetailsViewModel
import com.lagradost.cloudstream3.ui.viewmodel.WatchUiState
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    mediaId: Int,
    onBackClick: () -> Unit,
    onMediaClick: (Int) -> Unit,
    onWatchClick: (String, String) -> Unit,
    viewModel: DetailsViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val watchState by viewModel.watchState.collectAsState()
    var showSourceSheet by remember { mutableStateOf(false) }
    var selectedLinks by remember { mutableStateOf<List<ExtractorLink>>(emptyList()) }

    LaunchedEffect(mediaId) {
        viewModel.fetchDetails(mediaId)
    }

    LaunchedEffect(watchState) {
        when (val state = watchState) {
            is WatchUiState.LinksFound -> {
                if (state.links.size == 1) {
                    val encodedUrl = URLEncoder.encode(state.links.first().url, StandardCharsets.UTF_8.toString())
                    onWatchClick(encodedUrl, "Video")
                    viewModel.resetWatchState()
                } else {
                    selectedLinks = state.links
                    showSourceSheet = true
                }
            }
            is WatchUiState.NoLinksFound -> {
                Toast.makeText(context, "No sources found for this title.", Toast.LENGTH_SHORT).show()
                viewModel.resetWatchState()
            }
            is WatchUiState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                viewModel.resetWatchState()
            }
            else -> {}
        }
    }

    if (showSourceSheet) {
        ModalBottomSheet(
            onDismissRequest = { 
                showSourceSheet = false 
                viewModel.resetWatchState()
            },
            containerColor = Background,
            contentColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .navigationBarsPadding()
            ) {
                Text(
                    text = "Select Source",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                LazyColumn {
                    items(selectedLinks) { link ->
                        ListItem(
                            headlineContent = { Text("${link.type} - ${link.name}") },
                            supportingContent = { Text("${link.quality}p") },
                            modifier = Modifier.clickable {
                                val encodedUrl = URLEncoder.encode(link.url, StandardCharsets.UTF_8.toString())
                                onWatchClick(encodedUrl, link.name)
                                showSourceSheet = false
                                viewModel.resetWatchState()
                            },
                            colors = ListItemDefaults.colors(
                                containerColor = Color.Transparent,
                                headlineColor = Color.White,
                                supportingColor = TextMuted
                            )
                        )
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Background)) {
        when (val state = uiState) {
            is DetailsUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = KINO_Red)
            }
            is DetailsUiState.Success -> {
                val item = state.media
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
                                onClick = { 
                                    viewModel.watchNow(item)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = KINO_Red),
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                enabled = watchState !is WatchUiState.FetchingLinks
                            ) {
                                if (watchState is WatchUiState.FetchingLinks) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                                } else {
                                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Watch Now", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                }
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
                        ContentRow(title = "More Like This", items = state.similar, onMediaClick = onMediaClick)
                    }
                    
                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
            is DetailsUiState.Error -> {
                Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = state.message, color = Color.White)
                    Button(onClick = { viewModel.fetchDetails(mediaId) }) {
                        Text("Retry")
                    }
                }
            }
            else -> {}
        }
    }
}
