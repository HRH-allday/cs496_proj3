package com.example.q.wifitest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by q on 2017-01-09.
 */

public class GameResultBouncyBall extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_result_bouncy_ball);
        final int from = getIntent().getIntExtra("from", 0);
        int success = getIntent().getIntExtra("success", 0);

        TextView tv = (TextView) findViewById(R.id.info);
        Button btn = (Button) findViewById(R.id.restart);

        if(success == 1){
            tv.setText("Success!!");
            if(from == 1)
                btn.setText("다음 단계로");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch(from){
                        case 1:
                            startActivity(new Intent(getApplicationContext(), BouncyBallStage2.class));
                            break;
                        case 2:
                            try {
                                JSONObject jobj = new JSONObject();
                                jobj.put("coin", 100);
                                jobj.put("token", FirebaseInstanceId.getInstance().getToken());
                                PostThread p = new PostThread(jobj, "/game_result");
                                p.start();
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            break;
                        default:
                            break;

                    }
                }
            });
        }
        else {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (from) {
                        case 1:
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            break;
                        case 2:
                            try {
                                JSONObject jobj = new JSONObject();
                                jobj.put("coin", 50);
                                jobj.put("token", FirebaseInstanceId.getInstance().getToken());
                                PostThread p = new PostThread(jobj, "/game_result");
                                p.start();
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            break;
                        default:
                            break;

                    }
                }
            });
        }
    }
}