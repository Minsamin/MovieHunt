package com.rpimx.moviehunt.features.details.presentation

import com.rpimx.moviehunt.features.bookmark.data.local.Bookmark
import com.rpimx.moviehunt.features.cast.domain.model.Credits

sealed interface DetailsEvents {
    data object NavigateBack : DetailsEvents
    data class NavigateToCastsScreen(val credits: Credits) : DetailsEvents
    data class AddToBookmark(val bookmark: Bookmark) : DetailsEvents
    data class RemoveFromBookmark(val bookmark: Bookmark) : DetailsEvents
}