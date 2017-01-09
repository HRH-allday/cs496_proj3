package com.example.q.wifitest;

import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by q on 2017-01-07.
 */

public class ClickFollower extends AppCompatActivity {
    private int level;
    private int stage;
    private int score;
    private RecyclerView recyclerView;
    private TextView textView;
    private GridLayoutManager layoutManager;
    private ClickFrameAdapter adapter;
    private int screenWidth;
    private ArrayList<Integer> btnOrder;
    private int ith;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clickfollower);

        recyclerView = (RecyclerView) findViewById(R.id.clickframe);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;

        params.width = screenWidth;
        params.height = screenWidth;
        System.out.println("wtf : " + recyclerView.getWidth());
        recyclerView.setLayoutParams(params);

        Button button = (Button) findViewById(R.id.change_col_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShowProblem().execute(level);
            }
        });

        level = 2;
        layoutManager = new GridLayoutManager(getApplicationContext(), level);

        recyclerView.setLayoutManager(layoutManager);

        adapter = new ClickFrameAdapter(this, level, screenWidth / level, -2, false, 0);
        recyclerView.setAdapter(adapter);

        score = 0;
        textView = (TextView) findViewById(R.id.clickfollower_score);
        textView.setText(Integer.toString(score));

        stage = 1;
    }

    private class ShowProblem extends AsyncTask<Integer, Void, Void> {
        @Override
        public Void doInBackground(Integer... params) {
            ith = 0;
            btnOrder = new ArrayList<>();
            for (int i = 0; i < level*level; i++)
                btnOrder.add(i);
            Collections.shuffle(btnOrder, new Random(System.nanoTime()));

            try {
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }


            for (int i = 0; i < level*level; i++) {
                adapter = new ClickFrameAdapter(ClickFollower.this, level, screenWidth / level, btnOrder.get(i), false, 0);
                publishProgress();
                try {
                    Thread.sleep(300);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            adapter = new ClickFrameAdapter(ClickFollower.this, level, screenWidth / level, -2, false, 0);
            publishProgress();
            try {
                Thread.sleep(300);
            } catch (Exception e) {
                e.printStackTrace();
            }

            adapter = new ClickFrameAdapter(ClickFollower.this, level, screenWidth / level, -1, false, 0);
            publishProgress();
            try {
                Thread.sleep(800);
            } catch (Exception e) {
                e.printStackTrace();
            }

            adapter = new ClickFrameAdapter(ClickFollower.this, level, screenWidth / level, -2, true, 0);
            publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... params) {
            recyclerView.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void param) {

        }
    }

    private class ProblemSolved extends AsyncTask<Void, Void, Void> {
        @Override
        public Void doInBackground(Void... params) {
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
            }

            adapter = new ClickFrameAdapter(ClickFollower.this, level, screenWidth / level, -1, false, 1);
            publishProgress();
            try {
                Thread.sleep(600);
            } catch (Exception e) {
                e.printStackTrace();
            }

            adapter = new ClickFrameAdapter(ClickFollower.this, level, screenWidth / level, -2, false, 0);
            publishProgress();
            try {
                Thread.sleep(600);
            } catch (Exception e) {
                e.printStackTrace();
            }

            adapter = new ClickFrameAdapter(ClickFollower.this, level, screenWidth / level, -1, false, 1);
            publishProgress();
            try {
                Thread.sleep(600);
            } catch (Exception e) {
                e.printStackTrace();
            }

            adapter = new ClickFrameAdapter(ClickFollower.this, level, screenWidth / level, -2, false, 0);
            publishProgress();
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... params) {
            recyclerView.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void param) {
            score += (level*level*10/2);
            textView.setText(Integer.toString(score));
            stage++;
            if (stage == level) {
                stage =1;
                level++;
            }
            layoutManager = new GridLayoutManager(getApplicationContext(), level);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new ClickFrameAdapter(ClickFollower.this, level, screenWidth / level, -2, false, 0);
            recyclerView.setAdapter(adapter);
        }
    }

    private class GameOver extends AsyncTask<Integer, Void, Void> {
        @Override
        public Void doInBackground(Integer... params) {
            adapter = new ClickFrameAdapter(ClickFollower.this, level, screenWidth / level, params[0], false, 2);
            publishProgress();
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... params) {
            recyclerView.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void param) {
            Intent i = new Intent(ClickFollower.this, ClickFollowerGameOver.class);
            i.putExtra("score", score);
            startActivity(i);
            finish();
        }
    }

    public void onClick(int position) {
        if (position != btnOrder.get(ith))
            new GameOver().execute(position);

        ith++;

        if (ith == level*level)
            new ProblemSolved().execute();
    }
}
