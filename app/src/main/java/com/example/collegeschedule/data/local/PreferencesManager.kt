package com.example.collegeschedule.data.local

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("college_schedule_prefs", Context.MODE_PRIVATE)

    private val _favoriteGroups = MutableStateFlow(getFavoriteGroups())
    val favoriteGroups = _favoriteGroups.asStateFlow()

    fun addFavoriteGroup(groupName: String) {
        val current = getFavoriteGroups().toMutableSet()
        current.add(groupName)
        sharedPreferences.edit()
            .putStringSet("favorite_groups", current)
            .apply()
        _favoriteGroups.update { current.toList() }
    }

    fun removeFavoriteGroup(groupName: String) {
        val current = getFavoriteGroups().toMutableSet()
        current.remove(groupName)
        sharedPreferences.edit()
            .putStringSet("favorite_groups", current)
            .apply()
        _favoriteGroups.update { current.toList() }
    }

    fun getFavoriteGroups(): List<String> {
        return sharedPreferences.getStringSet("favorite_groups", emptySet())?.toList() ?: emptyList()
    }

    fun isFavorite(groupName: String): Boolean {
        return getFavoriteGroups().contains(groupName)
    }

    fun saveSelectedGroup(groupName: String) {
        sharedPreferences.edit()
            .putString("selected_group", groupName)
            .apply()
    }

    fun getSelectedGroup(): String? {
        return sharedPreferences.getString("selected_group", null)
    }
}
