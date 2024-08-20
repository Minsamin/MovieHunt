package com.rpimx.moviehunt.features.search.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.rpimx.moviehunt.features.search.domain.model.search.Search

data class SearchResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val searches: List<Search>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)