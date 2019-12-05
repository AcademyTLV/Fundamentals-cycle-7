package com.android.academy.threads

import android.os.AsyncTask
import android.os.SystemClock

class CounterAsyncTask(private val iAsyncTaskEvents: IAsyncTaskEvents) :
    AsyncTask<Int, Int, Void>() {

    override fun doInBackground(vararg integers: Int?): Void? {
        var length = 0
        length = if (integers.size == 1) {
            integers[0]!!
        } else {
            10
        }

        for (i in 0..length) {
            if (isCancelled) {
                return null
            }
            publishProgress(i)
            SystemClock.sleep(500)
        }
        return null
    }

    override fun onPreExecute() {
        super.onPreExecute()
        iAsyncTaskEvents.onPreExecute()
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        iAsyncTaskEvents.onPostExecute()
    }

    override fun onProgressUpdate(vararg values: Int?) {
        values[0]?.let { iAsyncTaskEvents.onProgressUpdate(it) }
    }

}
