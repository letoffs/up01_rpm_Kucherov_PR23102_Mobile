package com.example.collegeschedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.collegeschedule.data.local.PreferencesManager
import com.example.collegeschedule.ui.favorites.FavoritesScreen
import com.example.collegeschedule.ui.schedule.ScheduleScreen
import com.example.collegeschedule.ui.theme.CollegeScheduleTheme

class MainActivity : ComponentActivity() {
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferencesManager = PreferencesManager(this)

        enableEdgeToEdge()
        setContent {
            CollegeScheduleTheme {
                CollegeScheduleApp(preferencesManager = preferencesManager)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollegeScheduleApp(preferencesManager: PreferencesManager) {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Расписание групп")
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Schedule, contentDescription = "Расписание") },
                    label = { Text("Расписание") },
                    selected = true,
                    onClick = { navController.navigate("schedule") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Star, contentDescription = "Избранное") },
                    label = { Text("Избранное") },
                    selected = false,
                    onClick = { navController.navigate("favorites") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.AccountBox, contentDescription = "Профиль") },
                    label = { Text("Профиль") },
                    selected = false,
                    onClick = { navController.navigate("profile") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "schedule",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("schedule") {
                ScheduleScreen(
                    preferencesManager = preferencesManager,
                    onGroupSelected = { groupName ->
                        preferencesManager.saveSelectedGroup(groupName)
                    }
                )
            }
            composable("favorites") {
                FavoritesScreen(
                    preferencesManager = preferencesManager,
                    onGroupSelected = { groupName ->
                        preferencesManager.saveSelectedGroup(groupName)
                        navController.navigate("schedule")
                    }
                )
            }
            composable("profile") {
                ProfileScreen()
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Профиль студента",
            style = MaterialTheme.typography.titleLarge
        )
    }
}