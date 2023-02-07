package com.example.qsdmovies.util

object Constants {

    const val EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+"
    const val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"

    const val FIRST_NAME_MINIMUM_LENGTH = 1
    const val FIRST_NAME_MAXIMUM_LENGTH = 50
    const val LAST_NAME_MINIMUM_LENGTH = 1
    const val LAST_NAME_MAXIMUM_LENGTH = 50
    const val PASSWORD_MINIMUM_LENGTH = 8
    const val PASSWORD_MAXIMUM_LENGTH = 32

    // Realtime database
    const val FIRST_NAME_DB = "firstName"
    const val IMAGE_PATH_DB = "myImages/"
    const val USER_PATH_DB = "User"
    const val FAVORITES = "Favorites"

    // Movie details
    const val MOVIE_POSTER = "extra_movie_poster"
    const val MOVIE_TITLE = "extra_movie_title"
    const val MOVIE_RATING = "extra_movie_rating"
    const val MOVIE_OVERVIEW = "extra_movie_overview"
    const val MOVIE_ID = "extra_movie_id"

    // Crew details
    const val CREW_NAME = "extra_crew_name"

}