package com.udacity.gradle.jokes;

public class Joker {
    public String getJoke(){
        return "This is totally a funny joke";
    }

    //lets just add another function which will return a modified joke string
    public String getJokes(String jokeid) {
        return "This is totally a funny joke " + jokeid;
    }
}