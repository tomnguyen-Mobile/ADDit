package com.mdi2.addit.home

import com.mdi2.addit.data.MovieTaskDao
import com.mdi2.addit.data.MovieTaskEntity
import com.mdi2.addit.data.entities.CommentDao
import com.mdi2.addit.data.entities.CommentEntity
import com.mdi2.addit.models.MovieTask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface MovieRepositoryInterface {
    fun getMovieTasks(): Flow<List<MovieTask>>
    fun getCommentsForMovie(movieId: Int): Flow<List<CommentEntity>>
    suspend fun getMovieTaskById(taskId: Int): MovieTask?
    suspend fun toggleTaskCompletetion(taskId: Int)
    suspend fun addMovieTask(
        title: String,
        isMovie: Boolean,
        season: Int,
        episode: Int,
        timestampOfPause: String?,
        completed: Boolean = false,
        imageUrl: String? = null
    )
    suspend fun updateMovieTask(
        taskId: Int,
        title: String,
        season: Int,
        episode: Int,
        timestamp: String,
        completed: Boolean
    )

    suspend fun addComment(
        movieTaskId: Int,
        comment: String
    )

    suspend fun deleteMovieTask(taskId: Int)
}

// VIEW -> VIEWMODEL -> REPOSITORY -> DAO -> DATABASE
class InMemoryMovieRepository(
    private val movieTaskDao: MovieTaskDao,
    private val commentDao: CommentDao
    ): MovieRepositoryInterface {
//    private val movieTasks = mutableListOf<MovieTask>()

    override fun getMovieTasks(): Flow<List<MovieTask>> {
        return movieTaskDao.observeAll().map { entities ->
            entities.map { entity ->
                MovieTask(
                    id = entity.id,
                    title = entity.title,
                    isMovie = entity.isMovie,
                    imageUrl = entity.imageUrl,
                    season = entity.season,
                    episode = entity.episode,
                    timestamp = entity.timestamp ?: "",
                    completed = entity.completed,
                )
            }
        }
    }


    override fun getCommentsForMovie(
        movieTaskId: Int
    ): Flow<List<CommentEntity>> {
        return commentDao.getCommentsForMovie(movieTaskId)
    }

    override suspend fun getMovieTaskById(taskId: Int): MovieTask? {
        return movieTaskDao.getAll()
            .find { it.id == taskId }
            ?.let { entity ->
                MovieTask(
                    id = entity.id,
                    title = entity.title,
                    isMovie = entity.isMovie,
                    imageUrl = entity.imageUrl,
                    season = entity.season,
                    episode = entity.episode,
                    timestamp = entity.timestamp ?: "",
                    completed = entity.completed,
                )
            }
    }

    override suspend fun toggleTaskCompletetion(taskId: Int) {
        val task = movieTaskDao.getAll().find {it.id == taskId }
        task?.let {
            movieTaskDao.updateCompletedStatus(taskId, !it.completed)
        }
//        movieTasks = movieTasks.map{
//            if (it.id == taskId)
//                it.copy(completed = !it.completed) else it
//        }
    }

    override suspend fun addMovieTask(
        title: String,
        isMovie: Boolean,
        season: Int,
        episode: Int,
        timestampOfPause: String?,
        completed: Boolean,
        imageUrl: String?
    ) {

        val trimmedTitle = title.trim()

        require(trimmedTitle.isNotEmpty()) {
            "Title cannot be empty"
        }

        val task = MovieTaskEntity(
            id = 0,
            title = trimmedTitle,
            isMovie = isMovie,
            imageUrl = imageUrl,
            season = season,
            episode = episode,
            timestamp = timestampOfPause,
            completed = completed,
        )

        movieTaskDao.insert(task)
    }

    override suspend fun updateMovieTask(
        taskId: Int,
        title: String,
        season: Int,
        episode: Int,
        timestamp: String,
        completed: Boolean
    ) {
        movieTaskDao.updateMovieTask(
            taskId = taskId,
            title = title,
            season = season,
            episode = episode,
            timestamp = timestamp,
            completed = completed
        )
    }

    override suspend fun addComment(
        movieTaskId: Int,
        comment: String
    ) {

        commentDao.insert(
            CommentEntity(
                movieTaskId = movieTaskId,
                comment = comment,
            )
        )
    }

    override suspend fun deleteMovieTask(taskId: Int) {
        movieTaskDao.deleteById(taskId)
    }

}