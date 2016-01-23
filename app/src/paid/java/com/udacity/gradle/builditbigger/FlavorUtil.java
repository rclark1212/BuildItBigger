package com.udacity.gradle.builditbigger;

import android.content.Context;


/**
 * Created by rclark on 1/13/2016.
 * Create a utility class for different behaviors between free and paid versions
 * (don't want to replicate code so pull out functions which are different)
 * PAID FLAVOR UTIL
 * pretty much all stub functions since no ads.
 */
public class FlavorUtil {

    public FlavorUtil(Context ctx) {
        //nothing to set up in paid version
    }

    public void requestNewInterstitial() {
        //return - don't load any ads. Paid version.
    }

    public void setUpListener() {
        //nothing to do for paid version
    }

    public boolean showInterstitialifLoaded() {
        //mostly stub - always return that ad is not loaded
        return false;
    }
}