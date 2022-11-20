package com.example.common.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.common.data.db.dao.MovieDao
import com.example.common.data.db.entities.MovieEntity

@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}