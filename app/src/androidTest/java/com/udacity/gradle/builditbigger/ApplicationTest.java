package com.udacity.gradle.builditbigger;

/*
    Used an excellent reference on testing asynctask from
    http://marksunghunpark.blogspot.com/2015/05/how-to-test-asynctask-in-android.html
*/

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;
import android.util.Pair;

import java.util.concurrent.CountDownLatch;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    CountDownLatch signal = null;   //set up a semiphore
    String mResult = null;

    public ApplicationTest() {
        super(Application.class);
    }

    //set starting condition (i.e. block execution until listener comes back)
    @Override
    protected void setUp() throws Exception {
        signal = new CountDownLatch(1);
    }

    //cleanup
    @Override
    protected void tearDown() throws Exception {
        signal.countDown();
    }

    //the actual test itself
    public void testEndpoint() throws InterruptedException {
        //create the task
        EndpointsAsyncTask task = new EndpointsAsyncTask(null);

        //set the listener and execute the task
        //note specifically that we are calling w/a null context on the test
        task.setListener(new EndpointsAsyncTask.TaskListener() {
                @Override
                public void onComplete(String result) {
                    mResult = result;
                    signal.countDown();
                }
        }).execute(new Pair<Context, String>(null, ""));
        //and wait for semiphore showing task complete...
        signal.await();

        //And is string empty? Fail...
        assertFalse(mResult.isEmpty());
    }
}