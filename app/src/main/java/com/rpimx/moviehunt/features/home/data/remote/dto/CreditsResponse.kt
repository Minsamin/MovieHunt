package com.rpimx.moviehunt.features.home.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.rpimx.moviehunt.features.cast.data.remote.dto.CastResponse

data class CreditsResponse(
    @SerializedName("cast")
    val cast: List<CastResponse>,
    @SerializedName("id")
    val id: Int
)