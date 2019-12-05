package com.android.academy.background_services

import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.android.academy.R

class HardWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context.applicationContext, workerParams) {

    companion object {
        private const val TAG = "HardWorker"
    }

    private var isWorkerStopped: Boolean = false

    override fun doWork(): Result {
        isWorkerStopped = false
        showToast(applicationContext.getString(R.string.starting_worker_msg))
        var i = 0
        while (i <= 100 && !isWorkerStopped) {
            SystemClock.sleep(100)
            val broadcastIntent = Intent(WorkerActivity.PROGRESS_UPDATE_ACTION)
            broadcastIntent.putExtra(WorkerActivity.PROGRESS_VALUE_KEY, i)
            Log.d(TAG, "progress: $i")
            applicationContext.sendBroadcast(broadcastIntent)
            i++
        }
        return if (i < 100) {
            showToast(applicationContext.getString(R.string.worker_failure_msg))
            Result.failure()
        } else {
            showToast(applicationContext.getString(R.string.finishing_worker_msg))
            Result.success()
        }
    }

    private fun showToast(msg: String) {
        val intent = Intent(WorkerActivity.PROGRESS_UPDATE_ACTION)
        intent.putExtra(WorkerActivity.SERVICE_STATUS, msg)
        applicationContext.sendBroadcast(intent)
    }

    override fun onStopped() {
        isWorkerStopped = true
        super.onStopped()
    }
}