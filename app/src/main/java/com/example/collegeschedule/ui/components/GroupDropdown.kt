package com.example.collegeschedule.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.dto.GroupDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDropdown(
    groups: List<GroupDto>,
    selectedGroup: GroupDto?,
    onGroupSelected: (GroupDto) -> Unit,
    onSearchChanged: (String) -> Unit,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf(selectedGroup?.groupName ?: "") }

    Column(modifier = modifier) {
        // Поле поиска с выпадающим списком
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { newValue ->
                    searchQuery = newValue
                    onSearchChanged(newValue)
                    if (newValue.isNotEmpty()) {
                        expanded = true
                    }
                },
                label = { Text("Поиск группы") },
                placeholder = { Text("Начните вводить название") },
                trailingIcon = {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp))
                    } else {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                singleLine = true
            )

            // Выпадающий список
            if (groups.isNotEmpty() && expanded) {
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.heightIn(max = 400.dp) // Увеличиваем высоту
                ) {
                    // Показываем все найденные группы
                    if (groups.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("Группы не найдены") },
                            onClick = { }
                        )
                    } else {
                        groups.forEach { group ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(
                                            text = group.groupName,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Text(
                                            text = "${group.specialtyName}, ${group.course} курс",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                },
                                onClick = {
                                    onGroupSelected(group)
                                    searchQuery = group.groupName
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Показываем выбранную группу
        selectedGroup?.let { group ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Выбрана группа:",
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            text = "${group.groupName} (${group.specialtyName})",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
