package com.android.academy.threads

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors


object AppExecutors {
    private const val T_COUNT: Int = 3
    val diskIO: Executor = Executors.newSingleThreadExecutor()
    val networkIO: Executor = Executors.newFixedThreadPool(T_COUNT)
    val mainThread: Executor = MainThreadExecutor
}

object MainThreadExecutor : Executor {
    private val mainThreadHandler: Handler = Handler(Looper.getMainLooper())
    override fun execute(command: Runnable) {
        mainThreadHandler.post(command)
    }
}
