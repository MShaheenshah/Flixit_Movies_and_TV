package com.programmerpro.cricketlivestreaming.must.launcherActivity

import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.programmerpro.cricketlivestreaming.must.launcherActivity.ui.theme.FlixitMoviesTVTheme

class WebViewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlixitMoviesTVTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WebViewContainer()
                }
            }
        }
    }
}

@Composable
fun WebViewContainer(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                            // Handle URL loading here
                            return false
                        }
                    }

                    // Enable JavaScript
                    settings.javaScriptEnabled = true

                    // Enable zooming
                    settings.builtInZoomControls = true
                    settings.displayZoomControls = false

                    // Enable remote access to file URLs
                    settings.allowFileAccess = true

                    // Enable DOM storage
                    settings.domStorageEnabled = true

                    // Set user agent string
                    settings.userAgentString = System.getProperty("http.agent")

                    loadUrl(SplashActivity.showScreenUrl(context)!!)
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}