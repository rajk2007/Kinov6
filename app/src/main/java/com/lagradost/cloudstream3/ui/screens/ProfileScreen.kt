package com.lagradost.cloudstream3.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import android.widget.Toast
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lagradost.cloudstream3.ui.theme.KINO_Gold
import com.lagradost.cloudstream3.ui.theme.KINO_Red
import com.lagradost.cloudstream3.ui.theme.TextMuted

val AMOLED_Black = Color(0xFF080808)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onSettingClick: (String) -> Unit) {
    var showAboutSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AMOLED_Black)
    ) {
        item { ProfileHeader() }
        
        item { SectionTitle("Account") }
        item { 
            SettingsCard {
                SettingItem(Icons.Default.Edit, "Edit Profile", onClick = { onSettingClick("Edit Profile") })
                HorizontalDivider(color = Color.White.copy(alpha = 0.05f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
                SettingItem(Icons.Default.Star, "Membership", onClick = { onSettingClick("Membership") })
                HorizontalDivider(color = Color.White.copy(alpha = 0.05f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
                SettingItem(Icons.Default.AccountBox, "Profiles", onClick = { onSettingClick("Profiles") })
            }
        }

        item { SectionTitle("Playback Settings") }
        item { 
            SettingsCard {
                SettingItem(Icons.Default.Settings, "Playback Settings", onClick = { onSettingClick("Playback Settings") })
            }
        }

        item { SectionTitle("Language Settings") }
        item { 
            SettingsCard {
                SettingItem(Icons.Default.Language, "Language Settings", onClick = { onSettingClick("Language Settings") })
            }
        }

        item { SectionTitle("Appearance") }
        item { ThemeGrid() }
        item { 
            SettingsCard {
                SettingItem(Icons.Default.Palette, "Appearance", onClick = { onSettingClick("Appearance") })
            }
        }

        item { SectionTitle("Notifications") }
        item { 
            SettingsCard {
                SettingItem(Icons.Default.Notifications, "Notifications", onClick = { onSettingClick("Notifications") })
            }
        }

        item { SectionTitle("Extensions Manager") }
        item { 
            SettingsCard {
                SettingItem(Icons.Default.Extension, "Extensions Manager", onClick = { onSettingClick("Extensions Manager") })
            }
        }

        item { SectionTitle("Privacy & Security") }
        item { 
            SettingsCard {
                SettingItem(Icons.Default.Lock, "Privacy & Security", onClick = { onSettingClick("Privacy & Security") })
            }
        }

        item { SectionTitle("Support") }
        item { 
            SettingsCard {
                SettingItem(Icons.Default.Help, "Support", onClick = { onSettingClick("Support") })
            }
        }

        item { SectionTitle("About Kino") }
        item { 
            SettingsCard {
                SettingItem(Icons.Default.Info, "About Kino", onClick = { showAboutSheet = true })
            }
        }

        item { Spacer(modifier = Modifier.height(100.dp)) }
    }

    if (showAboutSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAboutSheet = false },
            sheetState = sheetState,
            containerColor = Color(0xFF121212),
            contentColor = Color.White
        ) {
            AboutKinoContent()
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
            fontWeight = FontWeight.Bold,
            letterSpacing = 4.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Cinema. Redefined.",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Version 1.0.0",
            color = TextMuted,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "by Raj Karmakar",
            color = KINO_Gold,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ProfileHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(KINO_Red.copy(alpha = 0.8f), AMOLED_Black),
                    startY = 0f,
                    endY = 800f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.1f))
                    .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("RK", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Raj Karmakar", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                color = KINO_Gold.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(0.5.dp, KINO_Gold.copy(alpha = 0.5f))
            ) {
                Text(
                    text = "⭐ Premium Member", 
                    color = KINO_Gold, 
                    fontSize = 12.sp, 
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title.uppercase(),
        color = TextMuted,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(start = 24.dp, top = 24.dp, bottom = 8.dp)
    )
}

@Composable
fun SettingsCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.03f)),
        border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column(content = content)
    }
}

@Composable
fun SettingItem(icon: ImageVector, title: String, tint: Color = Color.White, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, color = tint, fontSize = 15.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
    }
}

@Composable
fun ThemeGrid() {
    val themes = listOf(
        "AMOLED Black" to AMOLED_Black,
        "Cinematic Red" to KINO_Red,
        "Purple Glow" to Color(0xFF7B2FBE),
        "Midnight Blue" to Color(0xFF1A237E)
    )
    
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            themes.take(2).forEach { (name, color) ->
                ThemeTile(name, color, Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            themes.drop(2).forEach { (name, color) ->
                ThemeTile(name, color, Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun ThemeTile(name: String, color: Color, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.03f))
            .border(0.5.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
            .clickable { 
                Toast.makeText(context, "$name theme selected!", Toast.LENGTH_SHORT).show()
            }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(color)
                .border(1.dp, Color.White.copy(alpha = 0.1f), CircleShape)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(name, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium)
    }
}
