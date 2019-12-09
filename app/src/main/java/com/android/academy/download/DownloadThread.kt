package com.android.academy.download

import android.os.Environment
import android.util.Log

import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DownloadThread(private val imageUrl: String, private val downloadCallBack: DownloadCallBack) :
    Thread() {
    private var progress = 0
    private var lastUpdateTime: Long = 0

    override fun run() {
        Log.d("TAG", "DownloadThread # run")

        val file = createFile()
        if (file == null) {
            downloadCallBack.onError("Can't create file")
            return
        }

        var connection: HttpURLConnection? = null
        var inputStream: InputStream? = null
        var fos: FileOutputStream? = null

        try {
            val url = URL(imageUrl)
            connection = url.openConnection() as HttpURLConnection
            connection.connect()

            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                downloadCallBack.onError(
                    "Server returned HTTP response code: "
                            + connection.responseCode + " - " + connection.responseMessage
                )
            }
            val fileLength = connection.contentLength
            Log.d("TAG", "File size: " + fileLength / 1024 + " KB")

            // Input stream (Downloading file)
            inputStream = BufferedInputStream(url.openStream(), 8192)

            // Output stream (Saving file)
            fos = FileOutputStream(file.path)

            var next: Int
            val data = ByteArray(1024)
            while (inputStream.read(data).let {next = it; it != -1 }){
                fos.write(data, 0, next)

                updateProgress(fos, fileLength)
            }

            downloadCallBack.onDownloadFinished(file.path)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            downloadCallBack.onError("MalformedURLException: " + e.message)
        } catch (e: IOException) {
            e.printStackTrace()
            downloadCallBack.onError("IOException: " + e.message)
        } finally {
            try {
                if (fos != null) {
                    fos.flush()
                    fos.close()
                }
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            connection?.disconnect()
        }
    }

    @Throws(IOException::class)
    private fun updateProgress(fos: FileOutputStream, fileLength: Int) {
        if (lastUpdateTime == 0L || System.currentTimeMillis() > lastUpdateTime + 500) {
            val count = fos.channel.size().toInt() * 100 / fileLength
            if (count > progress) {
                progress = count
                lastUpdateTime = System.currentTimeMillis()
                downloadCallBack.onProgressUpdate(progress)
            }
        }
    }

    private fun createFile(): File? {
        val mediaStorageDirectory = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + File.separator
        )
        // Create the storage directory if it does not exist
        if (!mediaStorageDirectory.exists()) {
            if (!mediaStorageDirectory.mkdirs()) {
                return null
            }
        }
        // Create a media file name
        val imageName = createImageFileName() + ".jpg"
        return File(mediaStorageDirectory.path + File.separator + imageName)
    }

    private fun createImageFileName(): String {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        return "FILE_$timeStamp"
    }

    interface DownloadCallBack {
        fun onProgressUpdate(percent: Int)

        fun onDownloadFinished(filePath: String)

        fun onError(error: String)
    }
}
