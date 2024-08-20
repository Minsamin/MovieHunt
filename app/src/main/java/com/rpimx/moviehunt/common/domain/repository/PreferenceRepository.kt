package com.rpimx.moviehunt.common.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {
    suspend fun saveTheme(themeValue: Int)
    fun getTheme(): Flow<Int>
}