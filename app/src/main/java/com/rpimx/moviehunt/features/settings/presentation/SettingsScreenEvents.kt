package com.rpimx.moviehunt.features.settings.presentation

sealed interface SettingsScreenEvents {
    data object ShowThemesDialog: SettingsScreenEvents
}