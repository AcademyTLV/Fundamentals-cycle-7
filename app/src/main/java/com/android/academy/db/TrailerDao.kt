package com.android.academy.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.academy.model.TrailerModel

@Dao
interface TrailerDao {
    @Query("SELECT * FROM TrailerModel WHERE movieId = :movieId")
    fun getVideo(movieId: Int): TrailerModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(trailerModel: TrailerModel?)
}