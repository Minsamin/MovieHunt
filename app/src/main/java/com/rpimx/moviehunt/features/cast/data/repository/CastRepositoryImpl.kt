package com.rpimx.moviehunt.features.cast.data.repository

import com.rpimx.moviehunt.common.data.remote.Api
import com.rpimx.moviehunt.common.utils.Resource
import com.rpimx.moviehunt.features.cast.domain.model.Credits
import com.rpimx.moviehunt.features.cast.domain.repository.CastRepository
import com.rpimx.moviehunt.features.details.data.mapper.toDomain
import com.rpimx.moviehunt.features.home.data.remote.dto.CreditsResponse
import javax.inject.Inject

class CastRepositoryImpl @Inject constructor(
    private val api: Api,
) : CastRepository {
    override suspend fun getTvSeriesCasts(id: Int): Resource<Credits> {
        val response: CreditsResponse = try {
            api.getTvSeriesCredits(id)
        } catch (e: Exception) {
            return Resource.Error("Something went wrong!")
        }
        return Resource.Success(response.toDomain())
    }

    override suspend fun getMovieCasts(id: Int): Resource<Credits> {
        val response: CreditsResponse = try {
            api.getMovieCredits(id)
        } catch (e: Exception) {
            return Resource.Error("Something went wrong!")
        }
        return Resource.Success(response.toDomain())
    }
}