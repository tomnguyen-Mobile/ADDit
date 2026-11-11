package com.mdi2.addit.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mdi2.addit.data.db.MovieTaskDatabase
import org.junit.Assert.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieTaskDaoTest {

    private lateinit var database: MovieTaskDatabase
    private lateinit var movieTaskDao: MovieTaskDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovieTaskDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        movieTaskDao = database.movieTaskDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insert_addsMovieToDatabase() = runTest {
        movieTaskDao.insert(
            MovieTaskEntity(
                title = "Alien",
                isMovie = true,
                completed = false
            )
        )
        val movies = movieTaskDao.getAll()
        assertEquals(1, movies.size)
        assertEquals("Alien", movies[0].title)
    }
}


