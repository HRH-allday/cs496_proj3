package com.example.q.wifitest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by q on 2017-01-07.
 */

public class ClickFollower extends AppCompatActivity{
    private int level;
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private ClickFrameAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clickfollower);

        recyclerView = (RecyclerView) findViewById(R.id.clickframe);

        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) recyclerView.getLayoutParams();
        params.height = params.width;
        recyclerView.setLayoutParams(params);

        Button button = (Button) findViewById(R.id.change_col_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        level = 1;
        layoutManager = new GridLayoutManager(getApplicationContext(), level);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ClickFrameAdapter(getApplicationContext(), level);
        recyclerView.setAdapter(adapter);
    }
}
