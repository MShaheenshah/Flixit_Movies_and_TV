package com.programmerpro.cricketlivestreaming.must.inAppUpdate

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.widget.Toast
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InAppUpdate @OptIn(DelicateCoroutinesApi::class) constructor(
    activity: Activity,
    private var appUpdateManager: AppUpdateManager
) {
    private val parentActivity: Activity = activity
    var updateType = AppUpdateType.IMMEDIATE

    fun checkForAppUpdates(){
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            val isUpdateAvailable = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            val isUpdateAllowed = when(updateType){
                AppUpdateType.FLEXIBLE -> info.isFlexibleUpdateAllowed
                AppUpdateType.IMMEDIATE -> info.isImmediateUpdateAllowed
                else -> false
            }
            if (isUpdateAvailable && isUpdateAllowed){
                appUpdateManager.startUpdateFlowForResult(
                    info,
                    updateType,
                    parentActivity,
                    123
                )
            }
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int){
        if (requestCode == 123){
            if (resultCode != RESULT_OK){
                println("something went wrong updating...")
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    val installStateUpdatedListener = InstallStateUpdatedListener{ state ->
        Toast.makeText(
            parentActivity.applicationContext,
            "Downloading...",
            Toast.LENGTH_LONG
        ).show()
        GlobalScope.launch{
            delay(5000)
            appUpdateManager.completeUpdate()
        }
    }

    fun onCreate(){
        if (updateType == AppUpdateType.IMMEDIATE){
            appUpdateManager.registerListener(installStateUpdatedListener)
        }
    }

    fun onResume(){
        if (updateType == AppUpdateType.FLEXIBLE){
            appUpdateManager.appUpdateInfo.addOnSuccessListener {info ->
                if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS){
                    appUpdateManager.startUpdateFlowForResult(
                        info,
                        updateType,
                        parentActivity,
                        123
                    )
                }
            }
        }
    }

    fun onDestroy(){
        if (updateType == AppUpdateType.IMMEDIATE){
            appUpdateManager.unregisterListener(installStateUpdatedListener)
        }
    }
}

