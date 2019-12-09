package com.android.academy.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.academy.model.VideoModel

@Dao
interface VideoDao {
    @Query("SELECT * FROM VideoModel WHERE movieId = :movieId")
    fun getVideo(movieId: Int): VideoModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(videoModel: VideoModel?)
}