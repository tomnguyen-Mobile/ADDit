package com.mdi2.addit.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_tasks")

data class MovieTaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val isMovie: Boolean,
    val season: Int = 0,
    val episode: Int = 0 ,
    val timestamp: String? = "00:00:00",
    val completed: Boolean,
)
