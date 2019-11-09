package com.android.academy.Threads

interface IAsyncTaskEvents {

    fun createAsyncTask()
    fun startAsyncTask()
    fun cancelAsyncTask()

    fun onPreExecute()
    fun onPostExecute()
    fun onProgressUpdate(num: Int)
    fun onCancel()
}
