package app.com.example.android.myshowjokelib;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
            String joke = intent.getStringExtra(JOKE_ARG);
            Toast.makeText(this, joke, Toast.LENGTH_SHORT).show();
        }
    }
}
