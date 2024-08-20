package com.rpimx.moviehunt.features.bookmark.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rpimx.moviehunt.TestCoroutineRule
import com.rpimx.moviehunt.features.bookmark.data.local.Bookmark
import com.rpimx.moviehunt.features.bookmark.domain.repository.BookmarkRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalCoroutinesApi
class BookmarksViewModelTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Inject
    lateinit var repository: BookmarkRepository

    private lateinit var viewModel: BookmarksViewModel

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        viewModel = BookmarksViewModel(repository)
    }

    @Test
    fun bookmarks_should_return_list_of_bookmarks() = testCoroutineRule.runTest {
        // Arrange
        val bookmark1 = Bookmark(
            id = 1, title = "Movie 1", type = "Movie", image = "/path/to/image.jpg", rating = 4.5f, bookmark = true, overview = "Overview of Movie 1", releaseDate = "2023-01-01"
        )
        val bookmark2 = Bookmark(
            id = 2, title = "Movie 2", type = "Movie", image = "/path/to/image2.jpg", rating = 4.0f, bookmark = false, overview = "Overview of Movie 2", releaseDate = "2023-02-01"
        )

        // Act
        repository.addToBookmark(bookmark1)
        repository.addToBookmark(bookmark2)
        val bookmarks = viewModel.bookmarks.first()

        // Assert
        assertEquals(2, bookmarks.size)
        assertTrue(bookmarks.contains(bookmark1))
        assertTrue(bookmarks.contains(bookmark2))
    }

    @Test
    fun deleteAllBookmarks_should_remove_all_bookmarks() = testCoroutineRule.runTest {
        // Arrange
        val bookmark1 = Bookmark(
            id = 1, title = "Movie 1", type = "Movie", image = "/path/to/image.jpg", rating = 4.5f, bookmark = true, overview = "Overview of Movie 1", releaseDate = "2023-01-01"
        )
        val bookmark2 = Bookmark(
            id = 2, title = "Movie 2", type = "Movie", image = "/path/to/image2.jpg", rating = 4.0f, bookmark = false, overview = "Overview of Movie 2", releaseDate = "2023-02-01"
        )
        repository.addToBookmark(bookmark1)
        repository.addToBookmark(bookmark2)

        // Act
        viewModel.deleteAllBookmarks()
        val bookmarks = viewModel.bookmarks.first()

        // Assert
        assertTrue(bookmarks.isEmpty())
    }
}