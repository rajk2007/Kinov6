
package com.rajk2007.kino

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel()) {
    val context = LocalContext.current
    val errorMessage by homeViewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        homeViewModel.loadContent()
    }

    errorMessage?.let {
        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        homeViewModel.errorMessageShown()
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Welcome to Home Screen", style = MaterialTheme.typography.headlineLarge)
    }
}
