package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import app.com.example.android.myshowjokelib.ShowJoke;
import com.google.android.gms.ads.*;

public class MainActivity extends ActionBarActivity {

    public InterstitialAd mInterstitialAd = null;
    private View mParentView = null;                //save off parent view
    private Context mContext = null;                //save off context

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //save off context
        mContext = this;

        //load up the ad on create
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                //and when add is closed, preload the next one...
                FlavorUtil.requestNewInterstitial(mInterstitialAd);
                //and launch joke task
                new EndpointsAsyncTask(mParentView).execute(new Pair<Context, String>(mContext, "(promise)"));
                //Note that you could optimize for free version here by loading joke
                //in background while interstitial was showing. Best way to do that though
                //is to either move the ad to the showjoke module or use a semiphore
                //and take the showjoke intent out of the postProcess task routine.
            }
        });

        //load the first interstitial
        FlavorUtil.requestNewInterstitial(mInterstitialAd);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tellJoke(View view) {

        //Toast.makeText(this, myJoker.getJoke(), Toast.LENGTH_SHORT).show();

        if (view != null) {
            //save off parent view
            mParentView = view.getRootView();
        }

        //check for internet...
        if (!isOnline()) {
            Toast.makeText(this, getString(R.string.no_inet), Toast.LENGTH_SHORT).show();
        } else {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                //launch joke task
                new EndpointsAsyncTask(mParentView).execute(new Pair<Context, String>(this, "(promise)"));
            }
        }
    }


    //
    //used to launch the show joke page - now done in asynctask completion
    //
    public void launchShowJokeLib(String joke) {
        Intent myIntent = new Intent(this, ShowJoke.class);
        myIntent.putExtra(ShowJoke.JOKE_ARG, joke);
        startActivity(myIntent);
    }

    //
    //  Utility routine to check if we have internet connection. Check on start
    //
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo == null)
            return false;

        return netInfo.isConnected();
    }

}
