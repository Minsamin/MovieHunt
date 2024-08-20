package com.rpimx.moviehunt.features.details.data.repository

import com.rpimx.moviehunt.common.data.remote.Api
import com.rpimx.moviehunt.common.utils.Resource
import com.rpimx.moviehunt.features.home.data.remote.dto.MovieDetails
import com.rpimx.moviehunt.features.home.data.remote.dto.TvSeriesDetails
import jakarta.inject.Inject

class DetailsRepository @Inject constructor(private val api: Api) {

    suspend fun getMoviesDetails(movieId: Int): Resource<MovieDetails> {
        val response = try {
            api.getMovieDetails(movieId)
        } catch (e: Exception) {
            return Resource.Error("Something went wrong!")
        }
        return Resource.Success(response)
    }

    suspend fun getTvSeriesDetails(tvId: Int): Resource<TvSeriesDetails> {
        val response = try {
            api.getTvSeriesDetails(tvId)
        } catch (e: Exception) {
            return Resource.Error("Something went wrong!")
        }
        return Resource.Success(response)
    }
}