package com.mdi2.addit.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieTaskDao {
    @Insert
    suspend fun insert(movieTask: MovieTaskEntity)

    @Query("SELECT * FROM movie_tasks ORDER BY id ASC")
    fun observeAll(): Flow<List<MovieTaskEntity>>

    @Query("SELECT * FROM movie_tasks ORDER BY id ASC")
    suspend fun getAll(): List<MovieTaskEntity>

    @Query("UPDATE movie_tasks SET completed = :completed WHERE id = :id")
    suspend fun updateCompletedStatus(id: Int, completed: Boolean)

    @Query("DELETE FROM movie_tasks WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM movie_tasks WHERE id = :taskId")
    suspend fun getById(taskId: Int): MovieTaskEntity?

    @Query("""
    UPDATE movie_tasks
    SET title = :title,
        season = :season,
        episode = :episode,
        timestamp = :timestamp
    WHERE id = :taskId
""")
    suspend fun updateMovieTask(
        taskId: Int,
        title: String,
        season: Int,
        episode: Int,
        timestamp: String
    )
}