package com.android.academy.background_services

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Process
import android.os.SystemClock
import android.util.Log

import com.android.academy.R

open class HardJobService : Service() {

    companion object {
        private const val TAG = "HardJobService"
    }

    private lateinit var serviceHandler: ServiceHandler
    private var isDestroyed = false

    override fun onCreate() {
        // To avoid cpu-blocking, we create a background handler to run our service
        val thread = HandlerThread(TAG, Process.THREAD_PRIORITY_BACKGROUND)
        // start the new handler thread
        thread.start()

        // start the service using the background handler
        serviceHandler = ServiceHandler(thread.looper)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        isDestroyed = false
        showToast(getString(R.string.starting_hardjob_service_msg))

        // call a new service handler. The service ID can be used to identify the service
        val message = serviceHandler.obtainMessage()
        message.arg1 = startId
        serviceHandler.sendMessage(message)

        return START_STICKY
    }

    protected fun showToast(msg: String) {
        val intent = Intent(BGServiceActivity.PROGRESS_UPDATE_ACTION)
        intent.putExtra(BGServiceActivity.SERVICE_STATUS, msg)
        sendBroadcast(intent)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    // Object responsible for count till 100 and sendBroadcast per step
    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            // Well calling serviceHandler.sendMessage(message);
            // from onStartCommand this method will be called.

            // Add your cpu-blocking activity here
            var i = 0
            while (i <= 100 && !isDestroyed) {
                SystemClock.sleep(100)
                val intent =
                    Intent(BGServiceActivity.PROGRESS_UPDATE_ACTION)
                intent.putExtra(BGServiceActivity.PROGRESS_VALUE_KEY, i)
                Log.d(TAG, "progress: $i")
                sendBroadcast(intent)
                i++
            }
            showToast(getString(R.string.finishing_hardjob_service_msg, msg.arg1))
            // the msg.arg1 is the startId used in the onStartCommand,
            // so we can track the running service here.
            stopSelf(msg.arg1)
        }
    }

    override fun onDestroy() {
        isDestroyed = true
        super.onDestroy()
    }
}
