package com.android.academy.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VideoModel(
    @PrimaryKey
    val movieId: Int,
    val id: String,
    val key: String
)