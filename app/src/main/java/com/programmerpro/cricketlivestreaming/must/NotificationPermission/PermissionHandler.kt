package com.programmerpro.cricketlivestreaming.must.NotificationPermission

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat

class PermissionHandler(private val activity: ComponentActivity) {

    companion object {
        const val PERMISSION_REQUEST_CODE = 112
        private const val CHANNEL_ID = "your_channel_id"
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
            if (!isNotificationPermissionGranted()) {
                requestNotificationPermission()
            } else {
                createNotificationChannel()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun isNotificationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            PERMISSION_REQUEST_CODE
        )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Push Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager =
                activity.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}