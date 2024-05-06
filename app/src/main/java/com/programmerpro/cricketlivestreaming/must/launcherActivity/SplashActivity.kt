package com.programmerpro.cricketlivestreaming.must.launcherActivity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.programmerpro.cricketlivestreaming.MainActivity
import com.programmerpro.cricketlivestreaming.R

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {

    companion object {
        private val ADMOB_PREFS_NAME = "AdMobIds"
        lateinit var sharedPreferences: SharedPreferences
        private val SHOW_ID_KEY = "show"
        private val SHOW_SCREEN = "showScreen"
        private val SHOW_SCREEN_URL = "showScreenUrl"
        private val INTERSTITIAL_ID_KEY = "interstitial"
        private val INTERSTITIAL_ID_KEY2 = "interstitial2"

        fun getInterstitialId(context: Context): String? {
            sharedPreferences = context.getSharedPreferences(ADMOB_PREFS_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(INTERSTITIAL_ID_KEY, "")
        }
        fun getInterstitialId2(context: Context): String? {
            sharedPreferences = context.getSharedPreferences(ADMOB_PREFS_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(INTERSTITIAL_ID_KEY2, "")
        }
        fun showAd(context: Context): String? {
            sharedPreferences = context.getSharedPreferences(ADMOB_PREFS_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(SHOW_ID_KEY, "")
        }
        fun showScreen(context: Context): String? {
            sharedPreferences = context.getSharedPreferences(ADMOB_PREFS_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(SHOW_SCREEN, "")
        }
        fun showScreenUrl(context: Context): String? {
            sharedPreferences = context.getSharedPreferences(ADMOB_PREFS_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(SHOW_SCREEN_URL, "")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                fetchAndSaveAdMobIds(context)
            }

            MainScreen()
        }
    }

    private fun fetchAndSaveAdMobIds(context: Context) {
        val database = FirebaseDatabase.getInstance()
        val admobIdsRef = database.getReference("Admob_ids")
        val handler = Handler()
        val timeoutMillis: Long = 5000

        val valueEventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val showAd = dataSnapshot.child(SHOW_ID_KEY).getValue(String::class.java)
                    val showScreen = dataSnapshot.child(SHOW_SCREEN).getValue(String::class.java)
                    val showScreenUrl = dataSnapshot.child(SHOW_SCREEN_URL).getValue(String::class.java)
                    val interstitialId = dataSnapshot.child(INTERSTITIAL_ID_KEY).getValue(String::class.java)
                    val interstitialId2 = dataSnapshot.child(INTERSTITIAL_ID_KEY2).getValue(String::class.java)
                    sharedPreferences =
                        context.getSharedPreferences(ADMOB_PREFS_NAME, Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString(SHOW_ID_KEY, showAd)
                    editor.putString(SHOW_SCREEN, showScreen)
                    editor.putString(SHOW_SCREEN_URL, showScreenUrl)
                    editor.putString(INTERSTITIAL_ID_KEY, interstitialId)
                    editor.putString(INTERSTITIAL_ID_KEY2, interstitialId2)
                    editor.apply()

                    admobIdsRef.removeEventListener(this)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                admobIdsRef.removeEventListener(this)
                startMainActivity()
            }
        }
        admobIdsRef.addValueEventListener(valueEventListener)
        handler.postDelayed({
            admobIdsRef.removeEventListener(valueEventListener)
            startMainActivity()
        }, timeoutMillis)
    }

    private fun startMainActivity() {

        if(showScreen(this@SplashActivity).equals("n")){
            Handler(Looper.myLooper()!!).postDelayed({
                startActivity(Intent(this@SplashActivity, WebViewActivity::class.java))
                finish()
            }, 500)
        } else{
            Handler(Looper.myLooper()!!).postDelayed({
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }, 500)
        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
    }
}

