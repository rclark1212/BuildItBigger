/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.example.rclark.myapplication.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.udacity.gradle.jokes.Joker;

import javax.inject.Named;

/** An endpoint class we are exposing */
@Api(
  name = "myApi",
  version = "v1",
  namespace = @ApiNamespace(
    ownerDomain = "backend.myapplication.rclark.example.com",
    ownerName = "backend.myapplication.rclark.example.com",
    packagePath=""
  )
)
public class MyEndpoint {

    /** A simple endpoint method that takes a name and says Hi back */
    @ApiMethod(name = "sayHi")
    public MyBean sayHi(@Named("name") String name) {

        MyBean response = new MyBean();
        response.setData("Hi, " + name);

        return response;
    }

    @ApiMethod(name = "getJoke")
    public MyBean getJoke() {
        Joker myJoker = new Joker();

        MyBean response = new MyBean();
        response.setData(myJoker.getJoke());

        return response;
    }

    @ApiMethod(name = "getJokes")
    public MyBean getJokes(@Named("jokeid") String jokeid) {
        Joker myJoker = new Joker();

        MyBean response = new MyBean();
        response.setData(myJoker.getJokes(jokeid));

        return response;
    }

}
