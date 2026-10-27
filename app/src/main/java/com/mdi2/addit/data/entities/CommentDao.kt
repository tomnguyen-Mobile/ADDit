package com.mdi2.addit.data.entities

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {

    @Insert
    suspend fun insert(comment: CommentEntity)

    @Query("""
        SELECT * FROM comments
        WHERE movieTaskId = :movieTaskId
        ORDER BY id DESC
    """)
    fun getCommentsForMovie(
        movieTaskId: Int
    ): Flow<List<CommentEntity>>
}