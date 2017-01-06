package com.example.q.wifitest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by q on 2017-01-05.
 */

public class CustomizeActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CustomizeAdapter adapter;
    private ImageView imageView;
    private TextView textView;
    private RelativeLayout relativeLayout;
    private FrameLayout frameLayout;
    private Button save_btn;
    private int background_index;
    private int font_index;
    private int etc_index;


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
        relativeLayout = (RelativeLayout) findViewById(R.id.customize_preview_texts);
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

                //imageView.setLayoutParams(new FrameLayout.LayoutParams((int)(((float) screenWidth / screenHeight) * imageHeight), imageHeight));
                //relativeLayout.setLayoutParams(new FrameLayout.LayoutParams((int)(((float) screenWidth / screenHeight) * imageHeight), imageHeight));

                ViewGroup.LayoutParams params = frameLayout.getLayoutParams();
                params.width = (int)(((float) screenWidth / screenHeight) * imageHeight);

                frameLayout.setLayoutParams(params);
            }
        });

        textView = (TextView) findViewById(R.id.customize_preview_test);

        save_btn = (Button) findViewById(R.id.customize_save_btn);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder saveDialog = new AlertDialog.Builder(CustomizeActivity.this);
                saveDialog.setTitle("Save");
                saveDialog.setMessage("이 UI로 저장하시겠습니까?");
                saveDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent();
                        i.putExtra("background_index", background_index);
                        i.putExtra("font_index", font_index);
                        i.putExtra("etc_index", etc_index);
                        setResult(MainActivity.SAVE_NEW_THEME, i);
                        finish();
                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
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

        background_index = -1;
        font_index = -1;
        etc_index = -1;
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

    public void setPreviewBackground(int id, int index) {
        imageView.setImageResource(id);
        background_index = index;
    }

    public void setPreviewFont(int id, int index) {
        textView.setText(id);
        font_index = index;
    }

    public void setPreviewEtc(int id, int index) {
        etc_index = index;
    }

}
