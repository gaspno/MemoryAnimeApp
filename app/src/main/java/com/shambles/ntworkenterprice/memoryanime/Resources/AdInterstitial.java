package com.shambles.ntworkenterprice.memoryanime.Resources;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.shambles.ntworkenterprice.memoryanime.Activity.CardGameActivity;

import static com.google.android.gms.ads.interstitial.InterstitialAd.*;
import static com.shambles.ntworkenterprice.memoryanime.Resources.MediaAux.PlayerBackgroundMusic;
import static com.shambles.ntworkenterprice.memoryanime.Resources.MediaAux.pauseBackgroundMusic;

public class AdInterstitial {

    private static InterstitialAd AdResource;

    public static void AdInterstitialResoruce( Context context) {
        AdRequest adRequest = new AdRequest.Builder().build();

        load(context, "ca-app-pub-9324003669548340/1415924800", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull final InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                AdResource = interstitialAd;
                AdResource.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        AdResource = null;
                        PlayerBackgroundMusic();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when fullscreen content failed to show.
                        Log.d("TAG", "The ad failed to show.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        pauseBackgroundMusic();

                    }
                });

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error

                AdResource = null;
            }
        });
    }
    public static void  AdInterstitialResoruceShow(Activity activity, Context context) {
    if (AdResource!=null) {



        AdResource.show(activity);
    }else{
        AdInterstitial.AdInterstitialResoruce(context);
    }
    }

}
