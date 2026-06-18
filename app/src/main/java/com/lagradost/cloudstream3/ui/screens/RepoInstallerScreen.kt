package com.lagradost.cloudstream3.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lagradost.cloudstream3.ui.theme.Background
import com.lagradost.cloudstream3.ui.theme.KINO_Red
import com.lagradost.cloudstream3.ui.theme.TextPrimary
import kotlinx.coroutines.delay

data class Repo(val name: String, val url: String, var progress: Float = 0f, var isDone: Boolean = false)

@Composable
fun RepoInstallerScreen(onFinished: () -> Unit) {
    val TAG = "KINO_Repo"
    val repos = remember {
        mutableStateListOf(
            Repo("MegaRepo", "https://raw.githubusercontent.com/self-similarity/MegaRepo/builds/repo.json"),
            Repo("CloudStream Providers", "https://raw.githubusercontent.com/recloudstream/extensions/master/repo.json"),
            Repo("Phisher Repo", "https://raw.githubusercontent.com/phisher98/cloudstream-extensions-phisher/refs/heads/builds/repo.json"),
            Repo("Megix Repo", "https://raw.githubusercontent.com/SaurabhKaperwan/CSX/builds/CS.json")
        )
    }
    var currentRepoIndex by remember { mutableIntStateOf(0) }
    var allDone by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        Log.d(TAG, "Starting repository initialization")
        for (i in repos.indices) {
            currentRepoIndex = i
            val repo = repos[i]
            Log.d(TAG, "Processing repo: ${repo.name} at ${repo.url}")
            
            try {
                // In a real app, this would be an actual network call
                // Log.d(TAG, "Downloading repo JSON from ${repo.url}")
                
                // Simulate downloading and loading
                for (p in 1..10) {
                    delay(200)
                    repos[i] = repos[i].copy(progress = p / 10f)
                }
                Log.d(TAG, "Successfully processed repo: ${repo.name}")
                repos[i] = repos[i].copy(isDone = true)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to process repo: ${repo.name}", e)
            }
        }
        Log.d(TAG, "All repositories processed")
        allDone = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Initializing Repositories",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(32.dp))
        
        repos.forEach { repo ->
            RepoProgressItem(repo)
            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.height(48.dp))
        
        if (allDone) {
            Button(
                onClick = onFinished,
                colors = ButtonDefaults.buttonColors(containerColor = KINO_Red)
            ) {
                Text("Start Watching", color = Color.White)
            }
        }
    }
}

@Composable
fun RepoProgressItem(repo: Repo) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = repo.name, color = TextPrimary)
            if (repo.isDone) {
                Text(text = "Done", color = KINO_Red)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = repo.progress,
            modifier = Modifier.fillMaxWidth(),
            color = KINO_Red,
            trackColor = Color.DarkGray
        )
    }
}
