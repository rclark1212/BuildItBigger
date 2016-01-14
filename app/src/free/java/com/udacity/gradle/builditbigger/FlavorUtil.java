package com.udacity.gradle.builditbigger;

import com.google.android.gms.ads.*;

/**
 * Created by rclark on 1/13/2016.
 *  * Create a utility class for different behaviors between free and paid versions
 * (don't want to replicate code so pull out functions which are different)
 */
public class FlavorUtil {

    private FlavorUtil() {};

    public static void requestNewInterstitial(InterstitialAd interstitial) {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        interstitial.loadAd(adRequest);
    }
}
