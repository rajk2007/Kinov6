package com.rajk2007.kino

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lagradost.cloudstream3.utils.AppUtils.setContext
import java.lang.ref.WeakReference

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Set default uncaught exception handler for background crashes
        val oldHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            runOnUiThread {
                Toast.makeText(this, "Background Crash: ${throwable.message}", Toast.LENGTH_LONG).show()
            }
            oldHandler?.uncaughtException(thread, throwable)
        }

        try {
            // Initialize CloudStream context safely
            setContext(WeakReference(this))
            
            setContent {
                MaterialTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        MainScreen()
                    }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Launch Crash: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
}

@Composable
fun MainScreen() {
    Text(text = "Welcome to KINO")
}
