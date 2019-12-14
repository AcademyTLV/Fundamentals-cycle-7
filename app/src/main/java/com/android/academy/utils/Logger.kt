package com.android.academy.utils

import android.util.Log
import com.android.academy.BuildConfig

const val TAG = "MOVIESX"

fun logD(string: String) {
    if (BuildConfig.DEBUG) Log.d(TAG, string)
}

fun logE(string: String) {
    if (BuildConfig.DEBUG) Log.e(TAG, string)
}