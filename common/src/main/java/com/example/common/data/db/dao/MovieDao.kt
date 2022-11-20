package com.example.common.data.db.dao

import androidx.room.*
import com.example.common.data.db.entities.MovieEntity
import com.example.common.domain.models.FeedItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
abstract class MovieDao {
    @Query("SELECT * FROM MovieEntity")
    abstract fun getAll(): Flow<List<MovieEntity>>?

    @Query("SELECT * FROM MovieEntity WHERE id = :id ")
    abstract fun getMovieById(id: String): Flow<MovieEntity>
//    @Query(“SELECT userId, firstName, lastName FROM Users)

    @Query("SELECT id FROM MovieEntity")
    abstract fun getAllMovieIds(): Flow<List<String>>?

    fun getMovieByIdDistinctUntilChanged(id: String) =
        getMovieById(id).distinctUntilChanged()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(feedItem: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertList(feedItems: List<MovieEntity>)

    @Delete
    abstract suspend fun delete(feedItem: MovieEntity)

    @Query("DELETE FROM MovieEntity")
    abstract suspend fun deleteAll()
}