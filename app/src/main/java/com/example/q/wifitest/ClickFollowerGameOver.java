package com.example.q.wifitest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by q on 2017-01-08.
 */

public class ClickFollowerGameOver extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clickfollower_gameover);
        TextView textView = (TextView) findViewById(R.id.clickfollower_gameover_score);
        textView.setText(Integer.toString(getIntent().getExtras().getInt("score")));
    }
}
