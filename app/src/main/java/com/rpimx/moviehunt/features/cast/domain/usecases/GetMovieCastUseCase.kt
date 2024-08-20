package com.rpimx.moviehunt.features.cast.domain.usecases

import com.rpimx.moviehunt.common.utils.Resource
import com.rpimx.moviehunt.features.cast.domain.model.Credits
import com.rpimx.moviehunt.features.cast.domain.repository.CastRepository
import javax.inject.Inject

class GetMovieCastUseCase @Inject constructor(
    private val repository: CastRepository
) {
    suspend operator fun invoke(id: Int): Resource<Credits> = repository.getMovieCasts(id)
}