package com.example.q.wifitest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by q on 2017-01-07.
 */

public class ClickFollower extends AppCompatActivity{
    private int stage;
    private GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clickfollower);

        gridView = (GridView) findViewById(R.id.clickframe);
        GridView.LayoutParams params = (GridView.LayoutParams) gridView.getLayoutParams();
        params.height = params.width;
        params.
        gridView.setLayoutParams(params);




        stage = 0;


    }
}
