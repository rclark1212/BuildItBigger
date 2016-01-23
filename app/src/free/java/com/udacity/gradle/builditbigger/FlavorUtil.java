package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.util.Pair;

//need to include gms lib for interstitial ad support
import com.google.android.gms.ads.*;

/**
 * Created by rclark on 1/13/2016.
 *  * Create a utility class for different behaviors between free and paid versions
 * (don't want to replicate code so pull out functions which are different)
 * FREE FLAVORUTIL
 * This utility will insert an interstitial ad after pressing joke button
 */
public class FlavorUtil {

    private static InterstitialAd mInterstitialAd = null;
    private static Context mContext = null;

    //Instantiate class and set up ad
    public FlavorUtil(Context ctx) {
        //On create, save off context and set up new interstitial add
        mContext = ctx;
        mInterstitialAd = new InterstitialAd(ctx);
        //use the test ad id for interstitial (for production version, would substitute your admob ad id
        //and store it in a non-public git file
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
    };

    //Set up a listener for the ad
    public void setUpListener() {
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                //and when add is closed, preload the next one...
                requestNewInterstitial();
                //and launch joke task
                new EndpointsAsyncTask(MainActivity.mParentView).execute(new Pair<Context, String>(mContext, "(promise)"));
                //Note that you could optimize for free version here by loading joke
                //in background while interstitial was showing. Best way to do that though
                //is to either move the ad to the showjoke module or use a semiphore
                //and take the showjoke intent out of the postProcess task routine.
            }
        });
    }

    //Show the ad if it is loaded
    public boolean showInterstitialifLoaded() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            return true;
        } else {
            return false;
        }
    }

    //Request new ad
    public void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }
}
