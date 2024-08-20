package com.rpimx.moviehunt.features.cast.domain.repository

import com.rpimx.moviehunt.common.utils.Resource
import com.rpimx.moviehunt.features.cast.domain.model.Credits

interface CastRepository {
    suspend fun getTvSeriesCasts(id: Int): Resource<Credits>
    suspend fun getMovieCasts(id: Int): Resource<Credits>
}