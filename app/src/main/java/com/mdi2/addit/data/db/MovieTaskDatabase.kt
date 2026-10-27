package com.mdi2.addit.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mdi2.addit.data.MovieTaskDao
import com.mdi2.addit.data.MovieTaskEntity
import com.mdi2.addit.data.entities.CommentDao
import com.mdi2.addit.data.entities.CommentEntity

@Database(entities = [MovieTaskEntity::class, CommentEntity::class], version = 3 )
abstract class MovieTaskDatabase : RoomDatabase() {
    abstract fun movieTaskDao(): MovieTaskDao
    abstract fun commentDao(): CommentDao
}