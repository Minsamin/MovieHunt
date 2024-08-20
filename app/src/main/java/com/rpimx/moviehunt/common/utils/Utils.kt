package com.rpimx.moviehunt.common.utils

fun String.getImageUrl(): String {
    return "${Constants.IMAGE_BASE_UR}/$this"
}