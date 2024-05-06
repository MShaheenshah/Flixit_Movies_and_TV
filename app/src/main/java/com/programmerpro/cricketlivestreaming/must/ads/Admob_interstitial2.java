package com.programmerpro.cricketlivestreaming.must.ads;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.media3.common.Player;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.programmerpro.cricketlivestreaming.must.launcherActivity.SplashActivity;
import com.programmerpro.cricketlivestreaming.presentation.screens.videoPlayer.VideoPlayerScreenViewModel;

import java.util.Objects;

public class Admob_interstitial2 {
    public static InterstitialAd mInterstitialAd;

    public static void loadInterstitialAd(Activity activity, Player exoplayer){
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(activity, Objects.requireNonNull(SplashActivity.Companion.getInterstitialId2(activity)), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                exoplayer.setPlayWhenReady(true);
                                exoplayer.prepare();
                                exoplayer.play();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                mInterstitialAd = null;
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });
    }

    public static void loadAd(Activity mActivity, Player exoplayer){
        if (mInterstitialAd == null){
            loadInterstitialAd(mActivity, exoplayer);
        }else {
            showAd(mActivity, exoplayer);
        }
    }

    public static void showAd(Activity mActivity, Player exoplayer){
        if (mInterstitialAd != null){
            exoplayer.pause();
            mInterstitialAd.show(mActivity);
        }
    }
}