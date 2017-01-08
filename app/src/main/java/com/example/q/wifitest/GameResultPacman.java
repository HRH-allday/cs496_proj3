package com.example.q.wifitest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by q on 2017-01-07.
 */

public class GameResultPacman extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_result_pacman);

        TextView scoreText = (TextView) findViewById(R.id.score);
        TextView highScore = (TextView) findViewById(R.id.highscore);

        int score = getIntent().getIntExtra("score", 0);
        scoreText.setText("Score : " + score);

        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        int highest = settings.getInt("HIGH_SCORE", 0);

        if(score > highest){
            highScore.setText("Best Score : "+score);

            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE", score);
            editor.commit();
        }else{
            highScore.setText("Best Score : "+highest );
        }

        try {
            JSONObject jobj = new JSONObject();
            jobj.put("coin", score);
            jobj.put("token", FirebaseInstanceId.getInstance().getToken());
            PostThread p = new PostThread(jobj, "/game_result");
            p.start();
        }catch (JSONException e){
            e.printStackTrace();
        }

        Button btn = (Button) findViewById(R.id.restart);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
}