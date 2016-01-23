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
        //put up a loading indicator
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

            //Okay - we can either go to the cloud to get joke data *or*
            //use the local dev server. For testing, lets only go to the local
            //server (see udacityTest task done for extra credit). Note that I am
            // assuming that udacity wants connectedTest running against local
            // GCE server since instructions are to start/stop it for test. How do we
            //distinguish a real operating request from a test harness request?
            //Look at the context. If null, assume we are using test harness

            //if you want to force androidTest to use real GCE, either pass in a non-null context from
            //the test or else change the if statement below to always evaluate as true.
            if (context != null) {
                //Set this to real provider in cloud
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://udacitybuilditbiggerbackend.appspot.com/_ah/api/");
                myApiService = builder.build();
            } else {
                //We are running the test harness - go local for gce server
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
                        });
                // end options for devappserver
                myApiService = builder.build();
            }
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

        //hide the loading indicator
        if (mView != null) {
            ProgressBar progress = (ProgressBar) mView.findViewById(R.id.progress_bar);
            if (progress != null) progress.setVisibility(View.GONE);
        }

        //first check if we are testing...
        if (mListener != null) {
            //test!
            mListener.onComplete(result);
            //mListener.onComplete("");       //test the tests (uncomment to force fail the tests)
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