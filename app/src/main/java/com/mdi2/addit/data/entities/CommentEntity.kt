package com.mdi2.addit.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index
import com.mdi2.addit.data.MovieTaskEntity

@Entity(
    tableName = "comments",
    foreignKeys = [
        ForeignKey(
            entity = MovieTaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["movieTaskId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("movieTaskId")]
)
data class CommentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val movieTaskId: Int,
    val comment: String
)