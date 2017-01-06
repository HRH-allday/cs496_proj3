package com.example.q.wifitest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    static final int CREATED_FROM_SHOP = 109;
    static final int CREATED_FROM_CUSTOMIZE = 110;
    static final int SAVE_NEW_THEME = 111;

    private Integer[] images = { R.drawable.bg1, R.drawable.bg2, R.drawable.bg3, R.drawable.bg4, R.drawable.bg5, R.drawable.bg6 };
    private Integer[] texts = {R.string.font1, R.string.font2, R.string.font3, R.string.font4, R.string.font5, R.string.font6 };

    private FloatingActionButton goto_shop;
    private FloatingActionButton goto_customize;
    private ImageView imageView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        goto_shop = (FloatingActionButton) findViewById(R.id.main_goto_shop);
        goto_customize = (FloatingActionButton) findViewById(R.id.main_goto_customize);

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

        imageView = (ImageView) findViewById(R.id.main_background);
        textView = (TextView) findViewById(R.id.main_test);
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
}