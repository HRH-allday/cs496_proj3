package com.example.q.wifitest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.NoSuchElementException;

import static com.example.q.wifitest.CustomizeActivity.numViews;

public class MainActivity extends AppCompatActivity {
    static final int CREATED_FROM_SHOP = 109;
    static final int CREATED_FROM_CUSTOMIZE = 110;
    static final int SAVE_NEW_THEME = 111;

    static final Integer[] images = { R.drawable.fucking_bonobono, R.drawable.bg1, R.drawable.bg2, R.drawable.bg3, R.drawable.bg4, R.drawable.bg5, R.drawable.bg6 };
    static final Integer[] texts = { R.string.font0, R.string.font1, R.string.font2, R.string.font3, R.string.font4, R.string.font5, R.string.font6 };
    static final String[] fontNames = { "fonts/goongseo.TTF" };
    static final Integer[] spacedTexts = {R.string.text_spaced0, R.string.text_spaced1, R.string.text_spaced2};
    static final Integer[] unspacedTexts = {R.string.text_unspaced0, R.string.text_unspaced1, R.string.text_unspaced2};


    private TextView[] textViews = new TextView[numViews];

    private FloatingActionButton goto_shop;
    private FloatingActionButton goto_customize;
    private FloatingActionButton goto_scan;
    private FloatingActionButton goto_game;
    private ImageView imageView;
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
                //Intent i = new Intent(getApplicationContext(), ClickFollower.class);
                Intent i = new Intent(getApplicationContext(),  BouncyBallStage1.class);
                startActivity(i);
            }
        });

        imageView = (ImageView) findViewById(R.id.main_background);
        coinView = (TextView) findViewById(R.id.main_coin);

        new GetUserData().execute();
    }

    @Override
    public void setContentView(int view) {
        super.setContentView(view);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "fonts/goongseo.TTF");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
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
                Log.i("how is it", jobject.toString());
                boolean background_isdefault = jobject.getBoolean("background_isdefault");
                String background_value = jobject.getString("background_value");
                JSONArray font_color = jobject.getJSONArray("font_color");
                JSONArray font_size = jobject.getJSONArray("font_size");
                JSONArray font = jobject.getJSONArray("font");
                JSONArray etc_isspaced = jobject.getJSONArray("etc_isspaced");
                coin = jobject.getInt("coin");


                if (background_isdefault) {
                    int background_index = Integer.parseInt(background_value);
                    if (background_index != -1)
                        imageView.setImageResource(images[background_index]);
                } else {
                    // set background by image
                }
                for (int i = 0; i < font_color.length(); i++) {
                    String colorCode = font_color.getString(i);
                    int size = font_size.getInt(i);
                    int font_ind = font.getInt(i);
                    boolean spaced = etc_isspaced.getBoolean(i);
                    if (!colorCode.equals("-1")) {
                        textViews[i].setTextColor(Color.parseColor(colorCode));

                    }
                    if (size != -1) {
                        textViews[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
                    }
                    textViews[i].setTypeface(Typeface.createFromAsset(getAssets(), fontNames[font_ind]));
                    if (spaced) {
                        textViews[i].setText(unspacedTexts[i]);
                    }else{
                        textViews[i].setText(spacedTexts[i]);
                    }

                }

                if (coin != -1) {
                    // set coin
                    coinView.setText("자산 : "+coin+"원");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void scaleImage(ImageView view) throws NoSuchElementException {
        // Get bitmap from the the ImageView.
        Bitmap bitmap = null;

        Drawable drawing = view.getDrawable();
        bitmap = ((BitmapDrawable) drawing).getBitmap();

        // Get current dimensions AND the desired bounding box
        int width = 0;

        try {
            width = bitmap.getWidth();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Can't find bitmap on given view/drawable");
        }

        int height = bitmap.getHeight();
        int bounding = dpToPx(250);
        Log.i("Test", "original width = " + Integer.toString(width));
        Log.i("Test", "original height = " + Integer.toString(height));
        Log.i("Test", "bounding = " + Integer.toString(bounding));

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;
        Log.i("Test", "xScale = " + Float.toString(xScale));
        Log.i("Test", "yScale = " + Float.toString(yScale));
        Log.i("Test", "scale = " + Float.toString(scale));

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        width = scaledBitmap.getWidth(); // re-use
        height = scaledBitmap.getHeight(); // re-use
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        Log.i("Test", "scaled width = " + Integer.toString(width));
        Log.i("Test", "scaled height = " + Integer.toString(height));

        // Apply the scaled bitmap
        view.setImageDrawable(result);

        // Now change ImageView's dimensions to match the scaled image
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);

        Log.i("Test", "done");
    }

    private int dpToPx(int dp) {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }
}