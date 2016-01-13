package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rclark.myapplication.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import app.com.example.android.myshowjokelib.ShowJoke;

import java.io.IOException;

/**
 * Created by rclark on 1/10/2016.
 */

class EndpointsAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
    private static MyApi myApiService = null;
    private Context context = null;
    private View mView = null;
    private TaskListener mListener = null;      //listener for testing asynctask

    public EndpointsAsyncTask(View view) {
        //save off a copy of the view
        mView = view;
    }

    @Override
    protected void onPreExecute() {
        //either put up an interstitial ad or a loading indicator
        //for paid, loading indicator. for free, you get an ad...
        if (mView != null) {
            ProgressBar progress = (ProgressBar) mView.findViewById(R.id.progress_bar);
            if (progress != null) progress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected String doInBackground(Pair<Context, String>... params) {

        //grab params first
        context = params[0].first;
        String name = params[0].second;

        if(myApiService == null) {  // Only do this once
            //Set this to real provider in cloud
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://udacitybuilditbiggerbackend.appspot.com/_ah/api/");
/*
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    }); */
            // end options for devappserver
            myApiService = builder.build();
        }

        try {
            //return myApiService.sayHi(name).execute().getData();
            //get the joke from the endpoint (new api call)
            return myApiService.getJokes(name).execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {

        //okay, hide the spinner if it exists...
        //hide the progress bar
        if (mView != null) {
            ProgressBar progress = (ProgressBar) mView.findViewById(R.id.progress_bar);
            if (progress != null) progress.setVisibility(View.GONE);
        }

        //first check if we are testing...
        if (mListener != null) {
            //test!
            mListener.onComplete(result);
            //mListener.onComplete("");       //test the tests
        } else {
            //call joke display directly on postexecute
            Intent myIntent = new Intent(context, ShowJoke.class);
            myIntent.putExtra(ShowJoke.JOKE_ARG, result);
            context.startActivity(myIntent);
        }
        //Toast.makeText(context, result, Toast.LENGTH_LONG).show();
    }

    //Listener code for testing async task
    //define interface
    public static interface TaskListener {
        public void onComplete(String result);
    }

    //set listener
    public EndpointsAsyncTask setListener(TaskListener listener) {
        mListener = listener;
        return this;
    }
}