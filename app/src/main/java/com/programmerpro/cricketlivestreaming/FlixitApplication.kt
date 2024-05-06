package com.programmerpro.cricketlivestreaming

import android.app.Application
import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.google.android.gms.ads.MobileAds
import com.onesignal.OneSignal.Debug
import com.onesignal.OneSignal.initWithContext
import com.onesignal.debug.LogLevel
import com.programmerpro.cricketlivestreaming.data.api.ApiService
import com.programmerpro.cricketlivestreaming.data.repositories.MovieRepository
import com.programmerpro.cricketlivestreaming.data.repositories.MovieRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent

@HiltAndroidApp
class FlixitApplication : Application(){
    private val ONESIGNAL_APP_ID = "your one signal id"
    override fun onCreate() {
        super.onCreate()

        MobileAds.initialize(
            this
        ) { }

        Debug.logLevel = LogLevel.VERBOSE

        initWithContext(this, ONESIGNAL_APP_ID)

    }
}

@InstallIn(SingletonComponent::class)
@Module
abstract class MovieRepositoryModule {
    @Binds
    abstract fun bindMovieRepository(
        movieRepositoryImpl: MovieRepositoryImpl
    ): MovieRepository
}

@Module
@InstallIn(SingletonComponent::class)
class ApiServiceModule {
    @Provides
    fun provideApiService(): ApiService {
        return ApiService.invoke()
    }
}


@InstallIn(SingletonComponent::class)
@Module
object ContextModule {
    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }
}

//@InstallIn(SingletonComponent::class)
//@Module
//object CacheModule {
//    @OptIn(UnstableApi::class)
//    @Provides
//    @Singleton
//    fun provideCache(application: Application): Cache {
//        val cacheDirectory = application.cacheDir
//        return SimpleCache(cacheDirectory, LeastRecentlyUsedCacheEvictor(100 * 1024 * 1024))
//    }
//}

@Module
@InstallIn(ViewModelComponent::class)
object VideoPlayerModule {

    @OptIn(UnstableApi::class)
    @Provides
    @ViewModelScoped
    fun provideVideoPlayer(app: Application): Player {
        val dataSourceFactory = DefaultHttpDataSource.Factory().setUserAgent("CricketLiveStreaming")

        val mediaSourceFactory = ProgressiveMediaSource.Factory(dataSourceFactory)

        val player = ExoPlayer.Builder(app)
            .setSeekForwardIncrementMs(10)
            .setSeekBackIncrementMs(10)
            .setMediaSourceFactory(mediaSourceFactory)
            .setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
            .build()
            .apply {
                playWhenReady = true
                repeatMode = Player.REPEAT_MODE_ONE
            }

        return player
    }
}
