package com.android.academy.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class MovieModel(
    @PrimaryKey
    val movieId: Int,
    val name: String,
    val imageUrl: String,
    val overview: String?,
    val backImageUrl: String,
    val releaseDate: String,
    val popularity: Double
) : Parcelable