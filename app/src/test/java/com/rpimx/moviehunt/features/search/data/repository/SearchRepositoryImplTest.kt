package com.rpimx.moviehunt.features.search.data.repository

import androidx.paging.PagingSource
import com.rpimx.moviehunt.common.data.remote.Api
import com.rpimx.moviehunt.features.search.data.paging.SearchPagingSource
import com.rpimx.moviehunt.features.search.data.remote.dto.SearchResponse
import com.rpimx.moviehunt.features.search.domain.model.search.Search
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchRepositoryImplTest {

    @MockK
    private var api: Api = mockk<Api>()

    private lateinit var params: PagingSource.LoadParams.Refresh<Int>

    @Before
    fun setup() {
        params = PagingSource.LoadParams.Refresh(
            key = null, loadSize = 1, placeholdersEnabled = false
        )
    }

    @Test
    fun `when SearchPagingSource loads data then it returns expected LoadResult`() = runTest {
        // Given
        val queryParam = "example"
        coEvery { api.search(1, any()) } returns getSearchResponse()
        val searchPagingSource = SearchPagingSource(api = api, query = queryParam)

        val expectedSearchResults = listOf(getSearch())
        val expectedResult = PagingSource.LoadResult.Page(
            data = expectedSearchResults, prevKey = null, nextKey = 2
        )

        // When
        val actualResult = searchPagingSource.load(params)

        // Then
        assertEquals(expectedResult, actualResult)
    }

    private fun getSearch() = Search(
        adult = false,
        backdropPath = "/path/to/backdrop.jpg",
        firstAirDate = "2024-01-15",
        genreIds = listOf(18, 80),
        id = 101010,
        mediaType = "tv",
        name = "Example TV Series",
        originCountry = listOf("US"),
        originalLanguage = "en",
        originalName = "Example TV Series",
        originalTitle = null,
        overview = "An intriguing series that dives into the lives of individuals embroiled in a complex and thrilling narrative.",
        popularity = 8.7,
        posterPath = "/path/to/poster.jpg",
        releaseDate = null,
        title = null,
        video = false,
        voteAverage = 8.5,
        voteCount = 2000
    )

    private fun getSearchResponse() = SearchResponse(
        searches = listOf(getSearch()), page = 1, totalResults = 10, totalPages = 5
    )


}
