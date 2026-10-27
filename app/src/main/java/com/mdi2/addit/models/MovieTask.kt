package com.mdi2.addit.models

data class MovieTask (
    val id: Int,
    val title: String,
    val isMovie: Boolean,
    val season: Int = 0,
    val episode: Int = 0 ,
    val timestamp: String,
    val completed: Boolean,
    val comments: String? = null,
)
