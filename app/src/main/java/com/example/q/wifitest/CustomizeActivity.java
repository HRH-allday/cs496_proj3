package com.example.q.wifitest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static com.example.q.wifitest.MainActivity.coin;

/**
 * Created by q on 2017-01-05.
 */

public class CustomizeActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CustomizeAdapter adapter;
    private ImageView imageView;
    private FrameLayout frameLayout;
    private FloatingActionButton selectAllBtn;
    private Button save_btn;
    private int background_index;
    private Integer[] font_index = new Integer[numViews];
    private String[] color_index = new String[numViews];
    private Integer[] size_index = new Integer[numViews];
    private Boolean[] space_index = new Boolean[numViews];
    private TextView[] textViews = new TextView[numViews];
    private boolean[] isTextViewSelected = { false, false, false };
    static final Integer[] texts = { R.string.font0, R.string.font1, R.string.font2, R.string.font3, R.string.font4, R.string.font5, R.string.font6 };
    static final String[] fontNames = { "fonts/goongseo.TTF" };
    static final Integer[] spacedTexts = {R.string.text_spaced0, R.string.text_spaced1, R.string.text_spaced2};
    static final Integer[] unspacedTexts = {R.string.text_unspaced0, R.string.text_unspaced1, R.string.text_unspaced2};
    static final int numViews = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_customize);

        tabLayout = (TabLayout) findViewById(R.id.customize_tablayout);

        tabLayout.addTab(tabLayout.newTab().setText("배경"));
        tabLayout.addTab(tabLayout.newTab().setText("폰트"));
        tabLayout.addTab(tabLayout.newTab().setText("기타"));

        viewPager = (ViewPager) findViewById(R.id.customize_viewpager);

        adapter = new CustomizeAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(this);

        imageView = (ImageView) findViewById(R.id.customize_preview_background);
        frameLayout = (FrameLayout) findViewById(R.id.customize_framelayout);

        frameLayout.post(new Runnable() {
            @Override
            public void run() {
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int screenWidth = size.x;
                int screenHeight = size.y;

                int imageHeight = frameLayout.getHeight();

                ViewGroup.LayoutParams params = frameLayout.getLayoutParams();
                params.width = (int)(((float) screenWidth / screenHeight) * imageHeight);

                frameLayout.setLayoutParams(params);

                Log.i("Test", "width = " + params.width);
                Log.i("Test", "height = " + frameLayout.getHeight());
                initializePreviewUI(params.width, frameLayout.getHeight());
            }
        });

        textViews = new TextView[] {
                (TextView) findViewById(R.id.customize_preview_ui),
                (TextView) findViewById(R.id.customize_preview_title),
                (TextView) findViewById(R.id.customize_preview_coin)
        };

        for (int i = 0; i < numViews; i++) {
            final int index = i;
            textViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isTextViewSelected[index]) {
                        isTextViewSelected[index] = false;
                        textViews[index].setBackgroundResource(0);
                    } else {
                        isTextViewSelected[index] = true;
                        textViews[index].setBackgroundResource(R.drawable.text_border);
                   }
                }
            });
        }

        selectAllBtn = (FloatingActionButton) findViewById(R.id.customize_select_all_btn);
        selectAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < numViews; i++) {
                    if (!isTextViewSelected[i]) {
                        for (int j = 0; j < numViews; j++) {
                            isTextViewSelected[j] = true;
                            textViews[j].setBackgroundResource(R.drawable.text_border);
                        }
                        return;
                    }
                }

                for (int i = 0; i < numViews; i++) {
                    isTextViewSelected[i] = false;
                    textViews[i].setBackgroundResource(0);
                }
            }
        });


        save_btn = (Button) findViewById(R.id.customize_save_btn);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder saveDialog = new AlertDialog.Builder(CustomizeActivity.this);
                saveDialog.setTitle("Save");
                saveDialog.setMessage("이 UI로 변경하시겠습니까?");
                saveDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        new SaveCustomizedUI().execute();
                    }
                });
                saveDialog.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();


                    }
                });
                saveDialog.show();
            }
        });
    }

    private void initializePreviewUI(int width, int height) {
        Intent intent = getIntent();

        background_index = Integer.parseInt(intent.getExtras().getString("background_value"));
        imageView.setImageResource(MainActivity.images[background_index]);
        scaleImage(imageView, width, height);

        font_index = Arrays.copyOf(intent.getExtras().getIntegerArrayList("font").toArray(),
                intent.getExtras().getIntegerArrayList("font").toArray().length,
                Integer[].class);
        for(int i = 0 ; i < numViews ; i++)
            textViews[i].setTypeface(Typeface.createFromAsset(getAssets(), fontNames[font_index[i]]));

        color_index = Arrays.copyOf(intent.getExtras().getStringArrayList("font_color").toArray(),
                intent.getExtras().getStringArrayList("font_color").toArray().length,
                String[].class);
        for(int i = 0 ; i < numViews ; i++)
            if (!color_index[i].equals("-1")) {
                textViews[i].setTextColor(Color.parseColor(color_index[i]));
            }

        size_index = Arrays.copyOf(intent.getExtras().getIntegerArrayList("font_size").toArray(),
                intent.getExtras().getIntegerArrayList("font_size").toArray().length,
                Integer[].class);
        for(int i = 0 ; i < numViews ; i++)
            if (size_index[i] != -1) {
                textViews[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, size_index[i]);
            }

        space_index = Arrays.copyOf(((ArrayList<Boolean>)intent.getExtras().get("etc_isspaced")).toArray(),
                ((ArrayList<Boolean>)intent.getExtras().get("etc_isspaced")).toArray().length,
                Boolean[].class);
        for(int i = 0 ; i < numViews ; i++){
            if(space_index[i]) {
                if (i == 2)
                    textViews[i].setText(getResources().getString(spacedTexts[i]) + coin + "원");
                else
                    textViews[i].setText(spacedTexts[i]);
            } else {
                if (i == 2)
                    textViews[i].setText(getResources().getString(unspacedTexts[i]) + coin + "원");
                else
                    textViews[i].setText(unspacedTexts[i]);
            }
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }

    public void setPreviewBackground(int index) {
        imageView.setImageResource(MainActivity.images[index]);
        imageView.post(new Runnable() {
            @Override
            public void run() {
                scaleImage(imageView);
            }
        });
        background_index = index;
    }

    public void setPreviewFont(int index) {
        for(int i = 0 ; i < numViews ; i++){
            if(isTextViewSelected[i]) {
                textViews[i].setTypeface(Typeface.createFromAsset(getAssets(), fontNames[index]));
                font_index[i] = index;
            }
        }
    }

    public void setPreviewTextColor(String colorCode) {
        for(int i = 0 ; i < numViews ; i++){
            if(isTextViewSelected[i]) {
                textViews[i].setTextColor(Color.parseColor("#"+colorCode));
                color_index[i] = "#"+colorCode;
            }
        }
    }

    public void setPreviewTextSize(int size) {
        for(int i = 0 ; i < numViews ; i++){
            if(isTextViewSelected[i]) {
                textViews[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, ((float)size * 5 / 8));
                size_index[i] = size;
            }
        }
    }

    public void setPreviewSpace(){
        for(int i = 0 ; i < numViews ; i++){
            if(isTextViewSelected[i]) {
                if(space_index[i]) {
                    if (i == 2)
                        textViews[i].setText(getResources().getString(unspacedTexts[i]) + coin + "원");
                    else
                        textViews[i].setText(unspacedTexts[i]);
                    space_index[i] = false;
                } else {
                    if (i == 2)
                        textViews[i].setText(getResources().getString(spacedTexts[i]) + coin + "원");
                    else
                        textViews[i].setText(spacedTexts[i]);
                    space_index[i] = true;
                }
            }
        }
    }

    private class SaveCustomizedUI extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... params) {
            URL url;
            StringBuffer response = null;
            JSONObject jobject = null;

            try {
                url = new URL("http://ec2-52-79-95-160.ap-northeast-2.compute.amazonaws.com:3000/save_ui");
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

                String token = FirebaseInstanceId.getInstance().getToken();

                jobject = new JSONObject();
                jobject.put("token", token);
                jobject.put("background_index", background_index);
                JSONArray font_jarr = new JSONArray();
                for(int i = 0; i < numViews ; i++){
                    font_jarr.put(font_index[i]);
                }
                jobject.put("font_index", font_jarr);

                JSONArray color_jarr = new JSONArray();
                for(int i = 0; i < numViews ; i++){
                    color_jarr.put(color_index[i]);
                }
                jobject.put("color_index", color_jarr);

                JSONArray size_jarr = new JSONArray();
                for(int i = 0; i < numViews ; i++){
                    size_jarr.put(size_index[i]);
                }
                jobject.put("size_index", size_jarr);

                JSONArray space_jarr = new JSONArray();
                for(int i = 0; i < numViews ; i++){
                    space_jarr.put(space_index[i]);
                }
                jobject.put("space_index", space_jarr);


                OutputStream out_stream = conn.getOutputStream();

                out_stream.write(jobject.toString().getBytes());
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
                if (jobject.getString("result").compareTo("error") == 0) {
                    Toast.makeText(CustomizeActivity.this, "알 수 없는 오류로 인해 UI 변경에 실패했습니다.", Toast.LENGTH_LONG);
                } else {
                    Intent i = new Intent();
                    i.putExtra("background_index", background_index);
                    i.putExtra("font_index", font_index);
                    //i.putExtra("etc_index", etc_index);
                    setResult(MainActivity.SAVE_NEW_THEME, i);
                    finish();
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
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
        int height = 0;

        try {
            width = bitmap.getWidth();
            height = bitmap.getHeight();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Can't find bitmap on given view/drawable");
        }

        int boundingWidth = view.getWidth();
        int boundingHeight = view.getHeight();
        Log.i("Test", "original width = " + Integer.toString(width));
        Log.i("Test", "original height = " + Integer.toString(height));
        Log.i("Test", "bounding width = " + Integer.toString(boundingWidth));
        Log.i("Test", "bounding height = " + Integer.toString(boundingHeight));

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) boundingWidth) / width;
        float yScale = ((float) boundingHeight) / height;
        Log.i("Test", "xScale = " + Float.toString(xScale));
        Log.i("Test", "yScale = " + Float.toString(yScale));

        Bitmap scaledBitmap;
        if (xScale > yScale) {
            int newHeight = (int) (boundingHeight / xScale);
            Log.i("Test", "new height = " + newHeight);
            scaledBitmap = Bitmap.createBitmap(bitmap, 0, (height - newHeight) / 2 , width, newHeight, new Matrix(), true);
        } else {
            int newWidth = (int) (boundingWidth / yScale);
            Log.i("Test", "new width = " + newWidth);
            scaledBitmap = Bitmap.createBitmap(bitmap, (width - newWidth) / 2, 0, newWidth, height, new Matrix(), true);
        }

        // Create a new bitmap and convert it to a format understood by the ImageView
        width = scaledBitmap.getWidth(); // re-use
        height = scaledBitmap.getHeight(); // re-use
        Log.i("Test", "scaled width = " + Integer.toString(width));
        Log.i("Test", "scaled height = " + Integer.toString(height));

        // Apply the scaled bitmap
        view.setImageBitmap(scaledBitmap);
    }

    private void scaleImage(ImageView view, int boundingWidth, int boundingHeight) throws NoSuchElementException {
        // Get bitmap from the the ImageView.
        Bitmap bitmap = null;

        Drawable drawing = view.getDrawable();
        bitmap = ((BitmapDrawable) drawing).getBitmap();

        // Get current dimensions AND the desired bounding box
        int width = 0;
        int height = 0;

        try {
            width = bitmap.getWidth();
            height = bitmap.getHeight();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Can't find bitmap on given view/drawable");
        }

        Log.i("Test", "original width = " + Integer.toString(width));
        Log.i("Test", "original height = " + Integer.toString(height));
        Log.i("Test", "bounding width = " + Integer.toString(boundingWidth));
        Log.i("Test", "bounding height = " + Integer.toString(boundingHeight));

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) boundingWidth) / width;
        float yScale = ((float) boundingHeight) / height;
        Log.i("Test", "xScale = " + Float.toString(xScale));
        Log.i("Test", "yScale = " + Float.toString(yScale));

        Bitmap scaledBitmap;
        if (xScale > yScale) {
            int newHeight = (int) (boundingHeight / xScale);
            Log.i("Test", "new height = " + newHeight);
            scaledBitmap = Bitmap.createBitmap(bitmap, 0, (height - newHeight) / 2 , width, newHeight, new Matrix(), true);
        } else {
            int newWidth = (int) (boundingWidth / yScale);
            Log.i("Test", "new width = " + newWidth);
            scaledBitmap = Bitmap.createBitmap(bitmap, (width - newWidth) / 2, 0, newWidth, height, new Matrix(), true);
        }

        // Create a new bitmap and convert it to a format understood by the ImageView
        width = scaledBitmap.getWidth(); // re-use
        height = scaledBitmap.getHeight(); // re-use
        Log.i("Test", "scaled width = " + Integer.toString(width));
        Log.i("Test", "scaled height = " + Integer.toString(height));

        // Apply the scaled bitmap
        view.setImageBitmap(scaledBitmap);
    }
}