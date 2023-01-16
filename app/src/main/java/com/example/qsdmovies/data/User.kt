package com.example.qsdmovies.data

import de.hdodenhof.circleimageview.CircleImageView

data class User(
    val firstName: String? = null,
    val lastName: String? = null,
    val profileImage: CircleImageView
)