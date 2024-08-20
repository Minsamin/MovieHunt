package com.rpimx.moviehunt.features.bookmark.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rpimx.moviehunt.features.bookmark.data.local.Bookmark
import com.rpimx.moviehunt.features.bookmark.domain.repository.BookmarkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val repository: BookmarkRepository
) : ViewModel() {

    val bookmarks = repository.getAllBookmarks().map {
        it
    }.stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(5_000), initialValue = emptyList()
    )

    fun deleteFromBookmark(bookmark: Bookmark) {
        viewModelScope.launch {
            repository.deleteFromBookmark(bookmark)
        }
    }

    fun deleteAllBookmarks() {
        viewModelScope.launch {
            repository.deleteAllBookmarks()
        }
    }
}