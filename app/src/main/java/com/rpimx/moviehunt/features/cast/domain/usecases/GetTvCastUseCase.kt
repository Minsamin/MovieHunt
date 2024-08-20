package com.rpimx.moviehunt.features.cast.domain.usecases

import com.rpimx.moviehunt.features.cast.domain.repository.CastRepository
import javax.inject.Inject

class GetTvCastUseCase @Inject constructor(
    private val repository: CastRepository,
) {
    suspend operator fun invoke(id: Int) = repository.getTvSeriesCasts(id)
}