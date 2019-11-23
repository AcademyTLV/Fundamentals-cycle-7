package com.android.academy.download

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.android.academy.R
import com.android.academy.model.MovieModel


class DownloadActivity : AppCompatActivity() {

    companion object {

        const val PERMISSION: String = Manifest.permission.WRITE_EXTERNAL_STORAGE
        const val PERMISSIONS_REQUEST_CODE: Int = 42
        const val ARG_FILE_PATH: String = "Image-File-Path"
        private const val ARG_MOVIE_MODEL = "Movie-Model-Data"

        fun startActivity(context: Context, movieModel: MovieModel) {
            val intent = Intent(context, DownloadActivity::class.java)
            intent.putExtra(ARG_MOVIE_MODEL, movieModel)
            context.startActivity(intent)
        }

    }

    private lateinit var broadcastReceiver: BroadcastReceiver

    private val isPermissionGranted: Boolean
        get() = ContextCompat.checkSelfPermission(
            this,
            PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)
        broadcastReceiver = object : BroadcastReceiver() {

            override fun onReceive(context: Context, intent: Intent) {
                val filePath = intent.getStringExtra(ARG_FILE_PATH)
                Log.d("TAG", "DownloadActivity # onReceive, filePath: " + (filePath ?: return))
                if (!TextUtils.isEmpty(filePath)) {
                    showImage(filePath)
                }
            }
        }

        if (isPermissionGranted) {
            startDownloadService()
        } else {
            requestPermission()
        }
    }

    override fun onStart() {
        super.onStart()
        // register local broadcast
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadcastReceiver, IntentFilter(DownloadService.BROADCAST_ACTION))
    }

    override fun onStop() {
        // unregister local broadcast
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
        super.onStop()
    }

    internal fun showImage(filePath: String?) {
        val imageView = findViewById<ImageView>(R.id.download_iv_big_image)
        val bitmap = BitmapFactory.decodeFile(filePath)
        imageView.setImageBitmap(bitmap)
    }

    private fun requestPermission() {
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION)) {
            // Show an explanation to the user.
            // After the user sees the explanation, try again to request the permission.
            showExplainingRationaleDialog()
        } else {
            // No explanation needed, we can request the permission.
            requestWritePermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the contacts-related task you need to do.
                Log.d("TAG", "DownloadActivity # onRequestPermissionsResult, Permission granted")
                startDownloadService()
            } else {
                // permission denied, boo! Disable the functionality that depends on this permission.
                Log.d("TAG", "DownloadActivity # onRequestPermissionsResult, Permission denied")
                // no Permission - finish activity
                finishActivity()
            }
        }
    }

    private fun requestWritePermission() {
        // PERMISSIONS_REQUEST_CODE is an app-defined int constant.
        // The callback method gets the result of the request.
        ActivityCompat.requestPermissions(this, arrayOf(PERMISSION), PERMISSIONS_REQUEST_CODE)
    }

    private fun showExplainingRationaleDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.download_dialog_title)
        builder.setMessage(R.string.download_dialog_message)
        builder.setPositiveButton(R.string.download_dialog_ok) { dialogInterface, i -> requestWritePermission() }
        builder.setNegativeButton(R.string.download_dialog_cancel) { dialogInterface, i ->
            // no Permission - finish activity
            finishActivity()
        }
        builder.create().show()
    }

    private fun finishActivity() {
        this.finish()
    }

    private fun startDownloadService() {
        val movieModel = intent.getParcelableExtra<MovieModel>(ARG_MOVIE_MODEL)
        Log.d("TAG", "DownloadActivity # onCreate, movieModel: $movieModel")
        movieModel?.let {
            Log.d("TAG", "DownloadActivity # onRequestPermissionsResult, startDownloadService")
            DownloadService.startService(this, it.backImageUrl)
        }

    }


}
