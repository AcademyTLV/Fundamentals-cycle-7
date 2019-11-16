package com.android.academy.threads

import android.os.Handler
import android.os.Looper
import android.os.SystemClock

const val THREAD_NAME = "Handler_executor_thread"

class MySimpleAsyncTask(private val iAsyncTaskEvents: IAsyncTaskEvents) {

    @Volatile
    var isCancelled = false
        private set
    private var backgroundThread: Thread? = null

    /**
     * Runs on the UI thread before [.doInBackground].
     */
    private fun onPreExecute() {
        iAsyncTaskEvents.onPreExecute()
    }

    /**
     * Runs on new thread after [.onPreExecute] and before [.onPostExecute].
     */
    private fun doInBackground() {
        val end = 10
        for (i in 0..end) {
            if (isCancelled) {
                return
            }
            publishProgress(i)
            SystemClock.sleep(500)
        }
    }

    /**
     * Runs on the UI thread after [.doInBackground]
     */
    private fun onPostExecute() {
        iAsyncTaskEvents.onPostExecute()
    }

    private fun onProgressUpdate(progress: Int) {
        iAsyncTaskEvents.onProgressUpdate(progress)
    }


    fun execute() {
        runOnUiThread(Runnable {
            onPreExecute()
            backgroundThread = Thread({
                doInBackground()
                runOnUiThread(Runnable { onPostExecute() })
            }, THREAD_NAME).also {
                it.start()
            }
        })
    }


    private fun runOnUiThread(runnable: Runnable) {
        Handler(Looper.getMainLooper()).post(runnable)
    }

    private fun publishProgress(progress: Int) {
        runOnUiThread(Runnable { onProgressUpdate(progress) })
    }

    fun cancel() {
        isCancelled = true
        backgroundThread?.interrupt()
    }
}
