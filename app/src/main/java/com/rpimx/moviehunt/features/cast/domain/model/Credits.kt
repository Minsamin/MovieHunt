package com.rpimx.moviehunt.features.cast.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Credits(
    val cast: List<Cast>,
    val id: Int
)