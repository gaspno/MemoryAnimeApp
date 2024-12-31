package com.shambles.ntworkenterprice.memoryanime.Resources;

import android.app.Activity;
import android.content.Context;
import android.location.GpsStatus;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.shambles.ntworkenterprice.memoryanime.Activity.CardGameActivity;
import com.shambles.ntworkenterprice.memoryanime.Activity.PhotographicMemoryActivity;

import static com.shambles.ntworkenterprice.memoryanime.Resources.MediaAux.PlayerBackgroundMusic;
import static com.shambles.ntworkenterprice.memoryanime.Resources.MediaAux.initiliazeplayerbackgroundMusic;
import static com.shambles.ntworkenterprice.memoryanime.Resources.MediaAux.pauseBackgroundMusic;

import java.lang.reflect.Method;
import java.util.Timer;

public class AdReward {

    private static RewardedAd rewarded;


    public static void AdRewardResource(Context context, Activity activity, TextView view) {

        if (rewarded != null) {

            pauseBackgroundMusic();

            Activity activityContext = activity;
            rewarded.show(activityContext, new OnUserEarnedRewardListener() {

                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Log.d("TAG", "The user earned the reward.");
                    view.setText(Integer.toString(Integer.valueOf(view.getText().toString())+rewardItem.getAmount()));

                }



            });





        } else {
            Log.d("TAG", "The");
            LoadAd(context);
        }



    }
    public static RewardedAd getReward(){
        return rewarded;
    }
    public static void LoadAd(Context context) {
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(context, "ca-app-pub-9324003669548340/5354627248",
                adRequest, new RewardedAdLoadCallback(){
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.

                        rewarded = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        rewarded = rewardedAd;

                    }

                });
    }


}
