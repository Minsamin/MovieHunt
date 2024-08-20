package com.rpimx.moviehunt.features.bookmark.data.repository


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rpimx.moviehunt.TestCoroutineRule
import com.rpimx.moviehunt.features.bookmark.data.local.Bookmark
import com.rpimx.moviehunt.features.bookmark.data.local.BookmarkDao
import com.rpimx.moviehunt.features.bookmark.data.local.BookmarkDatabase
import com.rpimx.moviehunt.features.bookmark.domain.repository.BookmarkRepository
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class BookmarkRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @RelaxedMockK
    lateinit var database: BookmarkDatabase

    @RelaxedMockK
    lateinit var dao: BookmarkDao

    private lateinit var repository: BookmarkRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { database.dao } returns dao
        repository = BookmarkRepositoryImpl(database)
    }

    @After
    fun tearDown() {
        clearMocks(database, dao)
    }

    @Test
    fun addToBookmark_adds_a_bookmark() = testCoroutineRule.testRunBlocking {
        // Arrange
        val bookmark = Bookmark(
            id = 1, title = "Movie 1", type = "Movie", image = "/path/to/image.jpg", rating = 4.5f, bookmark = true, overview = "Overview of Movie 1", releaseDate = "2023-01-01"
        )
        coEvery { dao.addToBookmark(bookmark) } just Runs
        coEvery { dao.getAllBookmarks() } returns flowOf(listOf(bookmark))

        // Act
        repository.addToBookmark(bookmark)

        // Assert
        val bookmarks = repository.getAllBookmarks().first()
        assertEquals(1, bookmarks.size)
        assertEquals(bookmark, bookmarks[0])
    }

    @Test
    fun getBookmarks_returns_list_of_bookmarks() = testCoroutineRule.testRunBlocking {
        // Arrange
        val bookmark1 = Bookmark(
            id = 1, title = "Movie 1", type = "Movie", image = "/path/to/image.jpg", rating = 4.5f, bookmark = true, overview = "Overview of Movie 1", releaseDate = "2023-01-01"
        )
        val bookmark2 = Bookmark(
            id = 2, title = "Movie 2", type = "Movie", image = "/path/to/image2.jpg", rating = 4.0f, bookmark = false, overview = "Overview of Movie 2", releaseDate = "2023-02-01"
        )
        coEvery { dao.getAllBookmarks() } returns flowOf(listOf(bookmark1, bookmark2))

        // Act
        val bookmarks = repository.getAllBookmarks().first()

        // Assert
        assertEquals(2, bookmarks.size)
        assertTrue(bookmarks.contains(bookmark1))
        assertTrue(bookmarks.contains(bookmark2))
    }

    @Test
    fun isBookmarked_returns_true_if_bookmarked() = testCoroutineRule.testRunBlocking {
        // Arrange
        val bookmark = Bookmark(
            id = 1, title = "Movie 1", type = "Movie", image = "/path/to/image.jpg", rating = 4.5f, bookmark = true, overview = "Overview of Movie 1", releaseDate = "2023-01-01"
        )
        coEvery { dao.isBookmarked(bookmark.id) } returns flowOf(true)

        // Act
        val isBookmarked = repository.isBookmarked(bookmark.id).first()

        // Assert
        assertTrue(isBookmarked)
    }

    @Test
    fun getAllBookmarks_returns_specific_bookmark() = testCoroutineRule.testRunBlocking {
        // Arrange
        val bookmark2 = Bookmark(
            id = 2, title = "Movie 2", type = "Movie", image = "/path/to/image2.jpg", rating = 4.0f, bookmark = false, overview = "Overview of Movie 2", releaseDate = "2023-02-01"
        )
        coEvery { dao.getABookmark(bookmark2.id) } returns flowOf(bookmark2)

        // Act
        val retrievedBookmark = repository.getABookmarks(bookmark2.id).first()

        // Assert
        assertEquals(bookmark2, retrievedBookmark)
    }

    @Test
    fun deleteFromBookmark_removes_a_bookmark() = testCoroutineRule.testRunBlocking {
        // Arrange
        val bookmark = Bookmark(
            id = 1, title = "Movie 1", type = "Movie", image = "/path/to/image.jpg", rating = 4.5f, bookmark = true, overview = "Overview of Movie 1", releaseDate = "2023-01-01"
        )
        coEvery { dao.getAllBookmarks() } returns flowOf(emptyList())
        coEvery { dao.deleteABookmark(bookmark) } just Runs

        // Act
        repository.deleteFromBookmark(bookmark)

        // Assert
        val bookmarks = repository.getAllBookmarks().first()
        assertTrue(bookmarks.isEmpty())
    }

    @Test
    fun deleteAllBookmarks_removes_all_bookmarks() = testCoroutineRule.testRunBlocking {
        // Arrange
        coEvery { dao.deleteAllBookmarks() } just Runs
        coEvery { dao.getAllBookmarks() } returns flowOf(emptyList())

        // Act
        repository.deleteAllBookmarks()

        // Assert
        val bookmarks = repository.getAllBookmarks().first()
        assertTrue(bookmarks.isEmpty())
    }
}