package com.example.q.wifitest;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    static final int CREATED_FROM_SHOP = 109;
    static final int CREATED_FROM_CUSTOMIZE = 110;
    static final int SAVE_NEW_THEME = 111;

    private Integer[] images = { R.drawable.bg1, R.drawable.bg2, R.drawable.bg3, R.drawable.bg4, R.drawable.bg5, R.drawable.bg6 };
    private Integer[] texts = {R.string.font1, R.string.font2, R.string.font3, R.string.font4, R.string.font5, R.string.font6 };

    private FloatingActionButton goto_shop;
    private FloatingActionButton goto_customize;
    private FloatingActionButton goto_scan;
    private FloatingActionButton goto_game;
    private ImageView imageView;
    private TextView textView;
    private TextView coinView;
    static int coin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        goto_shop = (FloatingActionButton) findViewById(R.id.main_goto_shop);
        goto_customize = (FloatingActionButton) findViewById(R.id.main_goto_customize);
        goto_scan = (FloatingActionButton) findViewById(R.id.main_goto_scan);
        goto_game = (FloatingActionButton) findViewById(R.id.main_goto_game);

        goto_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ShopActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

        goto_customize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CustomizeActivity.class);
                startActivityForResult(i, SAVE_NEW_THEME);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

        goto_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ScanActivity.class);
                startActivity(i);
            }
        });

        goto_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ClickFollower.class);
                startActivity(i);
            }
        });

        imageView = (ImageView) findViewById(R.id.main_background);
        textView = (TextView) findViewById(R.id.main_test);
        coinView = (TextView) findViewById(R.id.main_coin);

        new GetUserData().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetUserData().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == SAVE_NEW_THEME) {
            if (data.getExtras().getInt("background_index") != -1)
                imageView.setImageResource(images[data.getExtras().getInt("background_index")]);
            if (data.getExtras().getInt("font_index") != -1)
                textView.setText(texts[data.getExtras().getInt("font_index")]);
        }
    }

    private class GetUserData extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... params) {
            URL url;
            StringBuffer response = null;
            JSONObject jobject = null;

            try {
                url = new URL("http://ec2-52-79-95-160.ap-northeast-2.compute.amazonaws.com:3000/app_start");
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

                String token = FirebaseInstanceId.getInstance().getToken();
                JSONObject token_jobject = new JSONObject();
                token_jobject.put("token", token);

                OutputStream out_stream = conn.getOutputStream();

                out_stream.write(token_jobject.toString().getBytes());
                out_stream.flush();
                out_stream.close();

                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + conn.getResponseCode());
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                response = new StringBuffer();
                String input_line;

                while ((input_line = in.readLine()) != null) {
                    System.out.println("input_line : " + input_line);
                    response.append(input_line);
                }
                in.close();

                jobject = new JSONObject(response.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return jobject;
        }

        @Override
        protected void onProgressUpdate(Void... params) {

        }

        @Override
        protected void onPostExecute(JSONObject jobject) {
            try {
                boolean background_isdefault = jobject.getBoolean("background_isdefault");
                String background_value = jobject.getString("background_value");
                int font_color = jobject.getInt("font_color");
                int font_size = jobject.getInt("font_size");
                int font = jobject.getInt("font");
                boolean etc_isspaced = jobject.getBoolean("etc_isspaced");
                coin = jobject.getInt("coin");


                if (background_isdefault) {
                    int background_index = Integer.parseInt(background_value);
                    if (background_index != -1)
                        imageView.setImageResource(images[background_index]);
                } else {
                    // set background by image
                }

                if (font_color != -1) {
                    // set text color
                }

                if (font_size != -1) {
                    // set text size
                }

                if (font != -1) {
                    textView.setText(texts[font]);
                }

                if (etc_isspaced) {
                    // set text spaced
                } else {
                    // set text nonspaced
                }
                if (coin != -1) {
                    // set coin
                    coinView.setText(""+coin+"원");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}