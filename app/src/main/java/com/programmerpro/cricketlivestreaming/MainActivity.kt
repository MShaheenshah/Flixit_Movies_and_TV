package com.programmerpro.cricketlivestreaming

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.programmerpro.cricketlivestreaming.must.NotificationPermission.PermissionHandler
import com.programmerpro.cricketlivestreaming.must.inAppUpdate.InAppUpdate
import com.programmerpro.cricketlivestreaming.presentation.App
import com.programmerpro.cricketlivestreaming.presentation.theme.JetStreamTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var inAppUpdate: InAppUpdate
    private lateinit var appUpdateManager: AppUpdateManager
    private lateinit var permissionHandler: PermissionHandler

    @SuppressLint("NewApi", "ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.N_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appUpdateManager = AppUpdateManagerFactory.create(this)
        inAppUpdate = InAppUpdate(this, appUpdateManager)
        inAppUpdate.checkForAppUpdates()
        inAppUpdate.onCreate()
        permissionHandler = PermissionHandler(this)

        window.apply {
            decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    )

            decorView.setOnSystemUiVisibilityChangeListener { visibility ->
                if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                    hideSystemUIWithDelay()
                }
            }
        }

        setContent {
            JetStreamTheme {
                Box(
                    modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)
                ) {
                    CompositionLocalProvider(
                        LocalContentColor provides MaterialTheme.colorScheme.onSurface
                    ) {
                        App(
                            onBackPressed = onBackPressedDispatcher::onBackPressed,
                        )
                    }
                }
            }
        }
    }

    private fun hideSystemUIWithDelay() {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            hideSystemUI()
        }, 3000)
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            showSystemUI()
        }
        return super.onTouchEvent(event)
    }

    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                )
    }

    @Deprecated("Deprecated in Java")
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionHandler.PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                showPermissionDeniedSnackBar()
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private fun showPermissionDeniedSnackBar() {
        val snackBar = Snackbar.make(
            findViewById(android.R.id.content),
            "Permission Denied. Allow it",
            Snackbar.LENGTH_INDEFINITE
        )
        snackBar.setAction("Allow Permission") {
            permissionHandler.checkAndRequestNotificationPermission()
            snackBar.dismiss()
        }
        snackBar.show()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        inAppUpdate.onActivityResult(requestCode, resultCode)
    }

    override fun onResume() {
        super.onResume()
        inAppUpdate.onResume()
        if (Build.VERSION.SDK_INT > 32) {
            if (!shouldShowRequestPermissionRationale("112")) {
                permissionHandler.checkAndRequestNotificationPermission()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        inAppUpdate.onDestroy()
    }
}

