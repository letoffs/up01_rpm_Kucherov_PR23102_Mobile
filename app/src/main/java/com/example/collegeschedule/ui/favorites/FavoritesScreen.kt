package com.example.collegeschedule.ui.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.dto.GroupDto
import com.example.collegeschedule.data.local.PreferencesManager
import com.example.collegeschedule.data.network.RetrofitInstance

@Composable
fun FavoritesScreen(
    preferencesManager: PreferencesManager,
    onGroupSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val favoriteGroups by preferencesManager.favoriteGroups.collectAsState()
    var allGroups by remember { mutableStateOf<List<GroupDto>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            // Загружаем все группы для получения информации
            allGroups = RetrofitInstance.groupsApi.getAllGroups()
        } catch (e: Exception) {
            // Можно добавить обработку ошибок, например, показать Snackbar
            println("Ошибка при загрузке групп: ${e.message}")
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        if (favoriteGroups.isEmpty()) {
            EmptyFavorites()
        } else {
            FavoriteGroupsList(
                favoriteGroups = favoriteGroups,
                allGroups = allGroups,
                preferencesManager = preferencesManager,
                onGroupSelected = onGroupSelected
            )
        }
    }
}

@Composable
private fun FavoriteGroupsList(
    favoriteGroups: List<String>,
    allGroups: List<GroupDto>,
    preferencesManager: PreferencesManager,
    onGroupSelected: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(favoriteGroups) { groupName ->
            val group = allGroups.find { it.groupName == groupName }

            FavoriteGroupCard(
                groupName = groupName,
                groupInfo = group,
                onFavoriteClick = {
                    preferencesManager.removeFavoriteGroup(groupName)
                },
                onClick = {
                    onGroupSelected(groupName)
                }
            )
        }
    }
}

@Composable
private fun FavoriteGroupCard(
    groupName: String,
    groupInfo: GroupDto?,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = groupName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                groupInfo?.let {
                    Text(
                        text = "${it.course} курс • ${it.specialtyName}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Удалить из избранного",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun EmptyFavorites() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.StarBorder,
                contentDescription = "Нет избранных",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Нет избранных групп",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Добавьте группы в избранное на главном экране",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}