package com.mdi2.addit.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdi2.addit.data.network.RetrofitInstance
import com.mdi2.addit.data.entities.CommentEntity
import com.mdi2.addit.data.network.OMDB_API_KEY
import com.mdi2.addit.data.network.OmdbMovie
import com.mdi2.addit.data.network.OmdbSearchItem
import com.mdi2.addit.models.MovieTask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MovieUiState(val movieTasks: List<MovieTask> = emptyList(),
    val errorMessage: String? = null,
    val isSaving: Boolean = false){
    val completedTaskCount: Int
        get() = movieTasks.count { it.completed }
}

class MovieViewModel(private val repository: MovieRepositoryInterface) : ViewModel(){
        private val mutableUiState = MutableStateFlow(MovieUiState())
//            movieTasks = repository.getMovieTasks()
//        ))
    val uiState: StateFlow<MovieUiState> = mutableUiState.asStateFlow()

    init {
        testOmdb()

        viewModelScope.launch{
            try{
                repository.getMovieTasks().collect{ tasks ->
                    mutableUiState.value = MovieUiState(
                        movieTasks = tasks
                    )
                }
            }catch (e: Exception){
                mutableUiState.update {
                    it.copy(errorMessage = ""+e.message)
                }
                e.printStackTrace()
            }
        }
    }

    fun toggleTaskCompletion(taskId: Int){
        viewModelScope.launch {
            try{
                repository.toggleTaskCompletetion(taskId)
                syncMovieTasks()
            }catch (e: Exception){
                mutableUiState.update {
                    it.copy(errorMessage = "fail to "+e.message)
                }
                e.printStackTrace()
            }
        }
    }

    fun addMovieTask(
        title: String,
        isMovie: Boolean,
        season: Int,
        episode: Int,
        timestamp: String? = "00:00:00",
        completed: Boolean,
        imageUrl: String? = null
    ) {
        viewModelScope.launch {
            repository.addMovieTask(
                title,
                isMovie,
                season,
                episode,
                timestamp,
                completed,
                imageUrl,
            )
        }
    }

    fun syncMovieTasks(){
        viewModelScope.launch {
            try{
                repository.getMovieTasks().collect{ tasks ->
                    mutableUiState.value = MovieUiState(
                        movieTasks = tasks
                    )
                }
            }catch (e: Exception){
                mutableUiState.update {
                    it.copy(errorMessage = "failed to sync study tasks"+e.message)
                }
                e.printStackTrace()
            }
        }
    }

    fun addComment(
        movieTaskId: Int,
        comment: String
    ) {
        viewModelScope.launch {
            repository.addComment(
                movieTaskId,
                comment
            )
        }
    }

    fun getCommentsForTask(
        taskId: Int
    ): Flow<List<CommentEntity>> {
        return repository.getCommentsForMovie(taskId)
    }
    fun updateMovieTask(

        taskId: Int,
        title: String,
        season: Int,
        episode: Int,
        timestamp: String,
        completed: Boolean
    ) {
        viewModelScope.launch {
            repository.updateMovieTask(
                taskId,
                title,
                season,
                episode,
                timestamp,
                completed
            )
        }
    }

    fun testOmdb() {
        viewModelScope.launch {
            try {
                val movie = RetrofitInstance.api.getMovie(
                    title = "Interstellar",
                    apiKey = "475ed561"
                )

                println(movie.Poster)
                println(movie.Title)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun searchMovie(
        title: String,
        onResult: (OmdbMovie?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val movie = RetrofitInstance.api.getMovie(
                    title = title,
                    apiKey = OMDB_API_KEY
                )
                onResult(movie)
            } catch (e: Exception) {
                onResult(null)
            }
        }
    }

    fun deleteMovieTask(taskId: Int) {
        viewModelScope.launch {
            repository.deleteMovieTask(taskId)
        }
    }

    fun searchMovies(
        title: String,
        onResult: (List<OmdbSearchItem>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.searchMovies(
                    search = title,
                    apiKey = OMDB_API_KEY
                )

                onResult(response.Search ?: emptyList())
            } catch (e: Exception) {
                onResult(emptyList())
            }
        }
    }

    fun searchMovieById(
        imdbId: String,
        onResult: (OmdbMovie?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val movie = RetrofitInstance.api.getMovieById(
                    imdbId = imdbId,
                    apiKey = OMDB_API_KEY
                )
                onResult(movie)
            } catch (e: Exception) {
                onResult(null)
            }
        }
    }
}

