package com.android.academy.Threads

import android.os.AsyncTask
import android.os.SystemClock

class CounterAsyncTask(private val mIAsyncTaskEvents: IAsyncTaskEvents?) :
    AsyncTask<Int, Int, Void>() {


    protected override fun doInBackground(vararg integers: Int?): Void? {
        var length = 0
        if (integers.size == 1) {
            length = integers[0]!!
        } else {
            length = 10
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
        mIAsyncTaskEvents?.onPreExecute()
    }

    override fun onPostExecute(result: Void) {
        super.onPostExecute(result)
        mIAsyncTaskEvents?.onPostExecute()
    }

    protected override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
        values[0]?.let { mIAsyncTaskEvents?.onProgressUpdate(it) }
    }

}
