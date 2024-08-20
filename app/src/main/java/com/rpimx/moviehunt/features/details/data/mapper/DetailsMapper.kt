package com.rpimx.moviehunt.features.details.data.mapper

import com.rpimx.moviehunt.features.cast.data.remote.dto.CastResponse
import com.rpimx.moviehunt.features.cast.domain.model.Cast
import com.rpimx.moviehunt.features.cast.domain.model.Credits
import com.rpimx.moviehunt.features.home.data.remote.dto.CreditsResponse

internal fun CreditsResponse.toDomain() = Credits(id = id, cast = cast.map { it.toDomain() })

internal fun CastResponse.toDomain() = Cast(
    adult = adult,
    castId = castId,
    character = character,
    creditId = creditId,
    gender = gender,
    id = id,
    knownForDepartment = knownForDepartment,
    name = name,
    order = order,
    originalName = originalName,
    popularity = popularity,
    profilePath = profilePath,
)