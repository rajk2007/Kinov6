package com.lagradost.cloudstream3.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lagradost.cloudstream3.ui.theme.Background
import com.lagradost.cloudstream3.ui.theme.KINO_Gold
import com.lagradost.cloudstream3.ui.theme.KINO_Red
import com.lagradost.cloudstream3.ui.theme.TextMuted

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    var showAboutModal by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Background)
    ) {
        item { ProfileHeader() }
        
        item { SectionTitle("Settings") }
        item { SettingItem(Icons.Default.PlayArrow, "Playback Settings", "Quality, Subtitles, etc.") }
        item { SettingItem(Icons.Default.Settings, "Language Settings", "App language and audio") }
        
        item { SectionTitle("Theme") }
        item { ThemeGrid() }
        
        item { SectionTitle("System") }
        item { SettingItem(Icons.Default.Build, "Extensions Manager", "Manage plugins and sources") }
        item { SettingItem(Icons.Default.Info, "About Kino", "Version info and developer", onClick = { showAboutModal = true }) }
        
        item { Spacer(modifier = Modifier.height(100.dp)) }
    }

    if (showAboutModal) {
        ModalBottomSheet(
            onDismissRequest = { showAboutModal = false },
            sheetState = sheetState,
            containerColor = Background,
            dragHandle = { BottomSheetDefaults.DragHandle(color = Color.Gray) }
        ) {
            AboutKinoContent()
        }
    }
}

@Composable
fun ProfileHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(KINO_Red, Color(0xFF7B2FBE), Background)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
                    .border(2.dp, Color.White.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("RK", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("Raj Karmakar", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("⭐ Premium Member", color = KINO_Gold, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        color = KINO_Red,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
    )
}

@Composable
fun SettingItem(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Text(subtitle, color = TextMuted, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = TextMuted)
    }
}

@Composable
fun ThemeGrid() {
    val themes = listOf(
        "AMOLED Black" to Background,
        "Cinematic Red" to KINO_Red,
        "Purple Glow" to Color(0xFF7B2FBE),
        "Midnight Blue" to Color(0xFF1A237E)
    )
    
    Row(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        themes.forEach { (name, color) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(color)
                        .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(name, color = TextMuted, fontSize = 10.sp, maxLines = 1)
            }
        }
    }
}

@Composable
fun AboutKinoContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "KINO",
            color = KINO_Red,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold
        )
        Text("by Raj Karmakar", color = TextMuted, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(24.dp))
        Text("Version 1.0.0", color = Color.White, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Cinema. Redefined.", color = Color.White, fontWeight = FontWeight.Light, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(48.dp))
    }
}
