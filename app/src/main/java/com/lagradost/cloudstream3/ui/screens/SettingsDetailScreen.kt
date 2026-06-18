package com.lagradost.cloudstream3.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lagradost.cloudstream3.ui.theme.KINO_Red
import com.lagradost.cloudstream3.ui.theme.TextMuted

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDetailScreen(title: String, onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF080808))
            )
        },
        containerColor = Color(0xFF080808)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }
            
            when (title) {
                "Playback Settings" -> {
                    item { SettingToggleItem("Auto-play next episode", true) }
                    item { SettingToggleItem("Skip intro & outro", true) }
                    item { SettingToggleItem("Hardware acceleration", true) }
                    item { SettingSelectItem("Default video quality", "1080p") }
                }
                "Language Settings" -> {
                    item { SettingSelectItem("App language", "English") }
                    item { SettingSelectItem("Audio language", "Original") }
                    item { SettingSelectItem("Subtitle language", "English") }
                }
                "Appearance" -> {
                    item { SettingToggleItem("Dynamic colors", true) }
                    item { SettingSelectItem("Theme", "AMOLED Black") }
                    item { SettingSelectItem("Poster size", "Medium") }
                }
                "Notifications" -> {
                    item { SettingToggleItem("New episode alerts", true) }
                    item { SettingToggleItem("Release reminders", true) }
                    item { SettingToggleItem("Download completed", true) }
                }
                else -> {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            Text(text = "Configuration for $title will appear here.", color = TextMuted, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingToggleItem(title: String, initialValue: Boolean) {
    var checked by remember { mutableStateOf(initialValue) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, color = Color.White, fontSize = 16.sp)
        Switch(
            checked = checked,
            onCheckedChange = { checked = it },
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = KINO_Red)
        )
    }
}

@Composable
fun SettingSelectItem(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, color = Color.White, fontSize = 16.sp)
        Text(text = value, color = KINO_Red, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}
