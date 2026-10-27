package com.mdi2.addit.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class CommentEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val movieTaskId: Int,

    val comment: String
)
