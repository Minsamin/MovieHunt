package com.rpimx.moviehunt.common.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.rpimx.moviehunt.common.domain.repository.PreferenceRepository
import com.rpimx.moviehunt.common.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferenceRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) : PreferenceRepository {
    override suspend fun saveTheme(themeValue: Int) {
        dataStore.edit { preferences: MutablePreferences ->
            preferences[Constants.THEME_OPTIONS] = themeValue
        }
    }

    override fun getTheme(): Flow<Int> {
        return dataStore.data.map { preferences: Preferences ->
            preferences[Constants.THEME_OPTIONS] ?: -1
        }
    }
}