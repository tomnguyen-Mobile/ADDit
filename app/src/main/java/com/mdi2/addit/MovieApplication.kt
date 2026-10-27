package com.mdi2.addit

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mdi2.addit.data.db.MovieTaskDatabase
import com.mdi2.addit.home.InMemoryMovieRepository
import com.mdi2.addit.home.MovieRepositoryInterface

class MovieApplication : Application() {
    val database: MovieTaskDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            MovieTaskDatabase::class.java,
            "movie_task_database.db"
        ).build()
    }

    val repository: MovieRepositoryInterface by lazy {
        InMemoryMovieRepository(database.movieTaskDao(), database.commentDao())
    }

    override fun onCreate(){
        super.onCreate()

    }
}