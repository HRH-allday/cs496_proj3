package com.example.q.wifitest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by q on 2017-01-08.
 */

public class ClickFollowerGameOver extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_result_pacman);

        TextView scoreText = (TextView) findViewById(R.id.score);
        TextView highScore = (TextView) findViewById(R.id.highscore);

        scoreText.setText("Score : " + Integer.toString(getIntent().getExtras().getInt("score")));
        highScore.setText("Best Score : " + Integer.toString(getIntent().getExtras().getInt("score")));

        Button btn = (Button) findViewById(R.id.restart);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
}
