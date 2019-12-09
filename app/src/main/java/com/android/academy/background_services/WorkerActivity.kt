package com.android.academy.background_services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.android.academy.R
import kotlinx.android.synthetic.main.activity_bgservice.*
import kotlinx.android.synthetic.main.activity_worker.*
import java.util.*

class WorkerActivity : AppCompatActivity() {

    private val backgroundProgressReceiver = WorkerBackgroundProgressReceiver()
    private var workId: UUID? = null

    companion object {
        const val TAG = "WorkerActivity"

        const val PROGRESS_UPDATE_ACTION: String = "WORKER_PROGRESS_UPDATE_ACTION"
        const val PROGRESS_VALUE_KEY: String = "WORKER_PROGRESS_VALUE_KEY"
        const val SERVICE_STATUS: String = "WORKER_SERVICE_STATUS"

        fun open(context: Context) {
            context.startActivity(Intent(context, WorkerActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker)

        worker_btn_start.setOnClickListener {
            if (workId == null) {
                // build the constraints
                val constraints = buildConstraints()

                // build the work request
                val workRequest = OneTimeWorkRequest
                    .Builder(HardWorker::class.java)
                    .setConstraints(constraints)
                    .build()

                workId = workRequest.id
                // add the request to the work queue
                WorkManager.getInstance(this).enqueue(workRequest)
                Log.d(TAG, "Work $workId enqueued")
            }
        }
        worker_btn_stop.setOnClickListener {
            workId?.let {
                WorkManager.getInstance(this).cancelWorkById(it)
                Log.d(TAG, "Work $it canceled")
                workId = null
            }
        }
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(backgroundProgressReceiver, IntentFilter(PROGRESS_UPDATE_ACTION))
    }

    override fun onStop() {
        unregisterReceiver(backgroundProgressReceiver)
        super.onStop()
    }

    private fun buildConstraints(): Constraints {
        val requiredNetwork = worker_switch_network.isChecked
        val networkType = if (requiredNetwork) {
            NetworkType.CONNECTED
        } else {
            NetworkType.NOT_REQUIRED
        }
        return Constraints.Builder()
            .setRequiredNetworkType(networkType)
            .setRequiresBatteryNotLow(worker_switch_battery.isChecked)
            .setRequiresCharging(worker_switch_charging.isChecked)
            .build()
    }

    inner class WorkerBackgroundProgressReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val progress = intent.getIntExtra(PROGRESS_VALUE_KEY, -1)
            if (progress >= 0) {
                val text: String
                if (progress > 99) {
                    text = context.getString(R.string.worker_work_done)
                    workId = null
                } else {
                    text = String.format(Locale.getDefault(), "%d%%", progress)
                }
                worker_tv_progress_value.text = text
            }

            intent.getStringExtra(SERVICE_STATUS)?.let { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

}
