package com.programmerpro.cricketlivestreaming.must.ads;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.programmerpro.cricketlivestreaming.must.launcherActivity.SplashActivity;

import java.util.Objects;

public class Admob_interstitial {

    public static InterstitialAd mInterstitialAd;
    public static int count = 0;
    @SuppressLint("StaticFieldLeak")
    public static Activity activity;

    public static void loadInterstitialAd(Activity activity){
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(activity, Objects.requireNonNull(SplashActivity.Companion.getInterstitialId(activity)), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdDismissedFullScreenContent() {
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                count=0;
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

    public static void checkingCounter(Activity mActivity){
       activity=mActivity;
        count++;
        if (mInterstitialAd!=null){
            if (count>25){
                mInterstitialAd.show(activity);
            }
        }else {
            loadInterstitialAd(mActivity);
        }
    }
}