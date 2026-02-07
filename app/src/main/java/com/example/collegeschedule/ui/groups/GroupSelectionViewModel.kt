package com.example.collegeschedule.ui.groups

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collegeschedule.data.dto.GroupDto
import com.example.collegeschedule.data.network.RetrofitInstance
import kotlinx.coroutines.launch

class GroupSelectionViewModel : ViewModel() {
    val groups = mutableStateOf<List<GroupDto>>(emptyList())
    val selectedGroup = mutableStateOf<GroupDto?>(null)
    val isLoading = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)

    init {
        loadGroups()
    }

    fun loadGroups() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                groups.value = RetrofitInstance.groupsApi.getAllGroups()
                error.value = null
            } catch (e: Exception) {
                error.value = "Ошибка загрузки групп: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun searchGroups(query: String) {
        viewModelScope.launch {
            if (query.isEmpty()) {
                loadGroups()
                return@launch
            }

            try {
                groups.value = RetrofitInstance.groupsApi.searchGroups(query)
                error.value = null
            } catch (e: Exception) {
                error.value = "Ошибка поиска: ${e.message}"
            }
        }
    }

    fun selectGroup(group: GroupDto) {
        selectedGroup.value = group
    }
}