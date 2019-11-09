package com.android.academy.Threads

import android.os.Handler
import android.os.Looper
import android.os.SystemClock

class MySimpleAsyncTask(private val mIAsyncTaskEvents: IAsyncTaskEvents) {

    @Volatile
    var isCancelled = false
        private set
    private var mBackgroundThread: Thread? = null

    /**
     * Runs on the UI thread before [.doInBackground].
     */
    protected fun onPreExecute() {
        mIAsyncTaskEvents.onPreExecute()
    }

    /**
     * Runs on new thread after [.onPreExecute] and before [.onPostExecute].
     */
    protected fun doInBackground() {
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
    protected fun onPostExecute() {
        mIAsyncTaskEvents.onPostExecute()
    }

    protected fun onProgressUpdate(progress: Int) {
        mIAsyncTaskEvents.onProgressUpdate(progress)
    }


    fun execute() {
        runOnUiThread(Runnable {
            onPreExecute()
            mBackgroundThread = Thread({
                doInBackground()
                runOnUiThread(Runnable { onPostExecute() })
            },"Handler_executor_thread").also {
                it.start()
            }
        })
    }


    private fun runOnUiThread(runnable: Runnable) {
        Handler(Looper.getMainLooper()).post(runnable)
    }

    protected fun publishProgress(progress: Int) {
        runOnUiThread(Runnable { onProgressUpdate(progress) })
    }

    fun cancel() {
        isCancelled = true
        mBackgroundThread?.interrupt()
    }
}
