package com.rpimx.moviehunt.common.data.remote

import com.rpimx.moviehunt.common.utils.Constants.STARTING_PAGE_INDEX
import com.rpimx.moviehunt.features.home.data.remote.dto.CreditsResponse
import com.rpimx.moviehunt.features.home.data.remote.dto.MovieDetails
import com.rpimx.moviehunt.features.home.data.remote.dto.MoviesResponse
import com.rpimx.moviehunt.features.home.data.remote.dto.TvSeriesDetails
import com.rpimx.moviehunt.features.home.data.remote.dto.TvSeriesResponse
import com.rpimx.moviehunt.features.search.data.remote.dto.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("trending/movie/day")
    suspend fun getTrendingTodayMovies(
        @Query("page") page: Int = STARTING_PAGE_INDEX, @Query("language") language: String = "en"
    ): MoviesResponse

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = STARTING_PAGE_INDEX, @Query("language") language: String = "en"
    ): MoviesResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("page") page: Int = STARTING_PAGE_INDEX, @Query("language") language: String = "en"
    ): MoviesResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int = STARTING_PAGE_INDEX, @Query("language") language: String = "en"
    ): MoviesResponse

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int = STARTING_PAGE_INDEX, @Query("language") language: String = "en"
    ): MoviesResponse

    @GET("trending/tv/day")
    suspend fun getTrendingTvSeries(
        @Query("page") page: Int = STARTING_PAGE_INDEX, @Query("language") language: String = "en"
    ): TvSeriesResponse

    @GET("tv/top_rated")
    suspend fun getTopRatedTvSeries(
        @Query("page") page: Int = STARTING_PAGE_INDEX, @Query("language") language: String = "en"
    ): TvSeriesResponse

    @GET("tv/on_the_air")
    suspend fun getOnTheAirTvSeries(
        @Query("page") page: Int = STARTING_PAGE_INDEX, @Query("language") language: String = "en"
    ): TvSeriesResponse

    @GET("tv/popular")
    suspend fun getPopularTvSeries(
        @Query("page") page: Int = STARTING_PAGE_INDEX, @Query("language") language: String = "en"
    ): TvSeriesResponse

    @GET("tv/airing_today")
    suspend fun getAiringTodayTvSeries(
        @Query("page") page: Int = STARTING_PAGE_INDEX, @Query("language") language: String = "en"
    ): TvSeriesResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int, @Query("language") language: String = "en"
    ): MovieDetails

    @GET("tv/{tv_id}")
    suspend fun getTvSeriesDetails(
        @Path("tv_id") tvSeriesId: Int, @Query("language") language: String = "en"
    ): TvSeriesDetails

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int, @Query("language") language: String = "en"
    ): CreditsResponse

    @GET("tv/{tv_id}/credits")
    suspend fun getTvSeriesCredits(
        @Path("tv_id") tvSeriesId: Int, @Query("language") language: String = "en"
    ): CreditsResponse

    @GET("search/multi")
    suspend fun search(
        @Query("page") page: Int = STARTING_PAGE_INDEX,
        @Query("query") query: String,
    ): SearchResponse
}