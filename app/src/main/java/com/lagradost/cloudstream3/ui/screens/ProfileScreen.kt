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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lagradost.cloudstream3.ui.theme.KINO_Gold
import com.lagradost.cloudstream3.ui.theme.KINO_Red
import com.lagradost.cloudstream3.ui.theme.TextMuted

val AMOLED_Black = Color(0xFF080808)

@Composable
fun ProfileScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AMOLED_Black)
    ) {
        item { ProfileHeader() }
        
        item { SectionTitle("Account") }
        item { 
            SettingsCard {
                SettingItem(Icons.Default.Edit, "Edit Profile")
                HorizontalDivider(color = Color.White.copy(alpha = 0.05f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
                SettingItem(Icons.Default.Star, "Membership")
                HorizontalDivider(color = Color.White.copy(alpha = 0.05f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
                SettingItem(Icons.Default.AccountBox, "Profiles")
            }
        }

        item { SectionTitle("Playback Settings") }
        item { 
            SettingsCard {
                SettingItem(Icons.Default.Settings, "Default video quality")
                HorizontalDivider(color = Color.White.copy(alpha = 0.05f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
                SettingItem(Icons.Default.Info, "Preferred audio language")
                HorizontalDivider(color = Color.White.copy(alpha = 0.05f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
                SettingItem(Icons.Default.Info, "Preferred subtitle language")
                HorizontalDivider(color = Color.White.copy(alpha = 0.05f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
                SettingItem(Icons.Default.PlayArrow, "Auto-play next episode")
                HorizontalDivider(color = Color.White.copy(alpha = 0.05f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
                SettingItem(Icons.Default.Forward10, "Skip intro")
            }
        }

        item { SectionTitle("Language Settings") }
        item { 
            SettingsCard {
                SettingItem(Icons.Default.Language, "App language")
                HorizontalDivider(color = Color.White.copy(alpha = 0.05f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
                SettingItem(Icons.Default.Audiotrack, "Audio language")
                HorizontalDivider(color = Color.White.copy(alpha = 0.05f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
                SettingItem(Icons.Default.Subtitles, "Subtitle language")
            }
        }

        item { SectionTitle("Appearance") }
        item { ThemeGrid() }
        item { 
            SettingsCard {
                SettingItem(Icons.Default.Palette, "Dynamic colors toggle")
                HorizontalDivider(color = Color.White.copy(alpha = 0.05f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
                SettingItem(Icons.Default.AspectRatio, "Poster size (Small/Medium/Large)")
            }
        }

        item { SectionTitle("Notifications") }
        item { 
            SettingsCard {
                SettingItem(Icons.Default.Notifications, "New episode alerts")
                HorizontalDivider(color = Color.White.copy(alpha = 0.05f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
                SettingItem(Icons.Default.NotificationsActive, "Release reminders")
                HorizontalDivider(color = Color.White.copy(alpha = 0.05f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
                SettingItem(Icons.Default.Download, "Download completed")
            }
        }

        item { SectionTitle("Extensions Manager") }
        item { 
            SettingsCard {
                SettingItem(Icons.Default.Extension, "Installed extensions")
                HorizontalDivider(color = Color.White.copy(alpha = 0.05f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
                SettingItem(Icons.Default.CheckCircle, "Enable/disable sources")
                HorizontalDivider(color = Color.White.copy(alpha = 0.05f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
                SettingItem(Icons.Default.Update, "Update plugins")
                HorizontalDivider(color = Color.White.copy(alpha = 0.05f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
                SettingItem(Icons.Default.Add, "Add repository URL")
            }
        }

        item { SectionTitle("Privacy & Security") }
        item { 
            SettingsCard {
                SettingItem(Icons.Default.Lock, "App lock (PIN/Fingerprint)")
                HorizontalDivider(color = Color.White.copy(alpha = 0.05f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
                SettingItem(Icons.Default.History, "Clear watch history")
                HorizontalDivider(color = Color.White.copy(alpha = 0.05f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
                SettingItem(Icons.Default.ExitToApp, "Sign out", tint = KINO_Red)
            }
        }

        item { SectionTitle("Support") }
        item { 
            SettingsCard {
                SettingItem(Icons.Default.Help, "Help Center")
                HorizontalDivider(color = Color.White.copy(alpha = 0.05f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
                SettingItem(Icons.Default.Report, "Report a problem")
                HorizontalDivider(color = Color.White.copy(alpha = 0.05f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
                SettingItem(Icons.Default.Feedback, "Request a feature")
            }
        }

        item { SectionTitle("About Kino") }
        item { 
            SettingsCard {
                SettingItem(Icons.Default.Info, "Version info")
                HorizontalDivider(color = Color.White.copy(alpha = 0.05f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
                SettingItem(Icons.Default.Person, "Developer info")
                HorizontalDivider(color = Color.White.copy(alpha = 0.05f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
                SettingItem(Icons.Default.NewReleases, "What's New")
                HorizontalDivider(color = Color.White.copy(alpha = 0.05f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
                SettingItem(Icons.Default.PrivacyTip, "Privacy Policy")
            }
        }

        item { Spacer(modifier = Modifier.height(100.dp)) }
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
fun SettingItem(icon: ImageVector, title: String, tint: Color = Color.White) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
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
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.03f))
            .border(0.5.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
            .clickable { }
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
