package com.rpimx.moviehunt.common.utils

import androidx.datastore.preferences.core.intPreferencesKey

object Constants {
    // Const
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val IMAGE_BASE_UR = "https://image.tmdb.org/t/p/w500/"
    const val TYPE_MOVIE = "movie"
    const val TYPE_TV_SERIES = "tv"

    // Preference
    const val MOVIE_HUNT_PREFERENCES = "MOVIE_HUNT_PREFERENCES"
    val THEME_OPTIONS = intPreferencesKey(name = "theme_option")

    //Paging
    const val STARTING_PAGE_INDEX = 0
    const val PAGING_SIZE = 20

    //DB
    const val DB_NAME = "movie_hunt_fav_database"
    const val TABLE_NAME = "movie_hunt_fav_table"
}