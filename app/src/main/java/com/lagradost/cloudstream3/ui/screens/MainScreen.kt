package com.lagradost.cloudstream3.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lagradost.cloudstream3.ui.theme.Background
import com.lagradost.cloudstream3.ui.theme.KINO_Red
import com.lagradost.cloudstream3.ui.theme.TextMuted
import com.lagradost.cloudstream3.ui.theme.TextPrimary

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Search : Screen("search", "Search", Icons.Default.Search)
    object Library : Screen("library", "Library", Icons.Default.List)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(Screen.Home, Screen.Search, Screen.Library, Screen.Profile)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in items.map { it.route }) {
                NavigationBar(
                    containerColor = Background.copy(alpha = 0.97f),
                    contentColor = TextMuted,
                ) {
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.label) },
                            label = { Text(screen.label) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                } else if (screen.route == Screen.Home.route) {
                                    // If already on Home, but maybe have other screens on top, pop them
                                    navController.popBackStack(Screen.Home.route, inclusive = false)
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = KINO_Red,
                                selectedTextColor = KINO_Red,
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted,
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { 
                HomeScreen(
                    onMediaClick = { mediaId -> navController.navigate("details/$mediaId") },
                    onSearchClick = { navController.navigate(Screen.Search.route) }
                ) 
            }
            composable(Screen.Search.route) { 
                SearchScreen(
                    onMediaClick = { mediaId -> navController.navigate("details/$mediaId") }
                ) 
            }
            composable(Screen.Library.route) { 
                LibraryScreen(
                    onMediaClick = { mediaId -> navController.navigate("details/$mediaId") }
                ) 
            }
            composable(Screen.Profile.route) { ProfileScreen() }
            composable(
                route = "details/{mediaId}",
                arguments = listOf(navArgument("mediaId") { type = NavType.IntType })
            ) { backStackEntry ->
                val mediaId = backStackEntry.arguments?.getInt("mediaId") ?: return@composable
                DetailsScreen(
                    mediaId = mediaId,
                    onBackClick = { navController.popBackStack() },
                    onMediaClick = { id -> navController.navigate("details/$id") }
                )
            }
        }
    }
}

@Composable
fun PlaceholderScreen(name: String) {
    Box(modifier = Modifier.fillMaxSize().background(Background), contentAlignment = Alignment.Center) {
        Text(text = "$name Screen", color = TextPrimary, style = MaterialTheme.typography.headlineLarge)
    }
}
