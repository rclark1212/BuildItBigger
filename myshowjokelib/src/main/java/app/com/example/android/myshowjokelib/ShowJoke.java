package app.com.example.android.myshowjokelib;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by rclark on 1/9/2016.
 * Shows a joke when invoked with an intent...
 */

public class ShowJoke extends ActionBarActivity {
    final public static String JOKE_ARG = "JOKE_ARG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_joke);

        Intent intent = getIntent();
        if (intent != null) {
            //get the joke from the intent
            String joke = intent.getStringExtra(JOKE_ARG);
            TextView text = (TextView) findViewById(R.id.joke_text);
            if (text != null) {
                text.setText(joke.toCharArray(), 0, joke.length());
            }
            //Toast.makeText(this, joke, Toast.LENGTH_SHORT).show();
        }
    }
}
