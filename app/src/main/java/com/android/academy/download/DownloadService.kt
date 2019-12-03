package com.android.academy.download

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.IBinder

import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager

import com.android.academy.R


class DownloadService : Service() {

    companion object {

        const val URL: String = "URL"
        const val ONGOING_NOTIFICATION_ID: Int = 14000605
        private const val CHANNEL_DEFAULT_IMPORTANCE = "Channel"
        const val BROADCAST_ACTION: String = "com.academy.fundamentals.DOWNLOAD_COMPLETE"

        fun startService(activity: Activity, url: String) {
            val intent = Intent(activity, DownloadService::class.java)
            intent.putExtra(URL, url)
            activity.startService(intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        startForeground()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d("TAG", "DownloadService # onStartCommand")

        var url = intent.getStringExtra(URL)
        Log.d("TAG", "DownloadService # URL: " + url)

        url?.let {
            startDownloadThread(it)
        }
        return START_STICKY
    }

    private fun startDownloadThread(url: String) {
        DownloadThread(url, object : DownloadThread.DownloadCallBack {
            override fun onProgressUpdate(progress: Int) {
                Log.d("TAG", "DownloadService, DownloadThread, onProgressUpdate: $progress%")
                updateNotification(progress)
            }

            override fun onDownloadFinished(filePath: String) {
                Log.d("TAG", "DownloadService, DownloadThread, onDownloadFinished: $filePath")
                sendBroadcastMsgDownloadComplete(filePath)
                stopSelf()
            }

            override fun onError(error: String) {
                Log.e("TAG", "DownloadService, DownloadThread, Error: $error")
                stopSelf()
            }
        }).start()
    }

    private fun startForeground() {
        createNotificationChannel()
        startForeground(ONGOING_NOTIFICATION_ID, createNotification(0))
    }

    private fun createNotification(progress: Int): Notification {
        val notificationIntent = Intent(this, DownloadActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        val progressStr = getString(R.string.notification_message, progress)

        return NotificationCompat.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
            .setContentTitle(getText(R.string.notification_title))
            .setContentText(progressStr)
            .setSmallIcon(android.R.drawable.stat_sys_upload)
            .setProgress(100, progress, false)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun updateNotification(progress: Int) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(ONGOING_NOTIFICATION_ID, createNotification(progress))
    }

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // The user-visible name of the channel.
            val name = getString(R.string.channel_name)
            // The user-visible description of the channel.
            val description = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CHANNEL_DEFAULT_IMPORTANCE, name, importance)

            // Configure the notification channel.
            mChannel.description = description
            mChannel.enableLights(true)

            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.lightColor = Color.RED
            mChannel.enableVibration(true)
            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    private fun sendBroadcastMsgDownloadComplete(filePath: String) {
        val intent = Intent(BROADCAST_ACTION)
        intent.putExtra(DownloadActivity.ARG_FILE_PATH, filePath)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TAG", "DownloadService # onDestroy")
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

}
