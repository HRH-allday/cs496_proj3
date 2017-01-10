package com.example.q.wifitest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by q on 2017-01-09.
 */

public class BouncyBallStage2 extends Activity {
    private TextView scoreLabel;
    private TextView startLabel;

    private ImageView redy;
    private ImageView star;

    private int frameHeight;
    private int frameWidth;
    private int redySize;
    private int screenWidth;
    private int screenHeight;

    private float redyX;
    private float redyY;
    private float starX;
    private float starY;
    private float redyprevX;
    private float redyprevY;
    private float groundY;
    private float height = 300.0f;

    private float redyVX;
    private float redyVY;

    private float[][] blockinfo = new float[4][4];
    private ImageView[] block = new ImageView[4];
    private float block2V = 2;

    // Initialize Class
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    // Status Check
    private boolean action_flg = false;
    private boolean start_flg = false;
    private boolean up_flg_left = false;
    private boolean up_flg_right = false;
    private boolean left_flg = false;
    private boolean right_flg = false;


    private int score = 0;
    private int time = 0;
    private int count = 0;
    private int heart = 3;

    private CountDownTimer timer2;
    private TextView timerText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bouncy_ball_stage2);

        timerText = (TextView) findViewById(R.id.timerText);
        redy = (ImageView) findViewById(R.id.redy);
        block[0] = (ImageView) findViewById(R.id.block1);
        block[1] = (ImageView) findViewById(R.id.block2);
        block[2] = (ImageView) findViewById(R.id.block3);
        block[3] = (ImageView) findViewById(R.id.block4);
        startLabel = (TextView) findViewById(R.id.startLabel);
        star = (ImageView) findViewById(R.id.star);
        start_flg = false;

        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        screenWidth = size.y;
        screenHeight = size.x;

        redy.setX(-170);
        redy.setY(-170);

        redyY = 500;
        Log.i("entered","why3");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("entered","why2");
        score = 0;
    }

    public void changePos() {
        //decideArea();
        float starCenterX = starX + star.getWidth() / 2;
        float starCenterY = starY + star.getHeight() / 2;

        if( redyX <= starCenterX && starCenterX <= redyX + redySize && redyY <= starCenterY && starCenterY <= redySize + redyY){

            star.setVisibility(View.GONE);

            Log.i("entered","why");
            redy.setX(150);
            redyX = 150;
            redy.setY(groundY - height);
            redyY = groundY - height;
            timer.cancel();
            timer = null;
            timer2.cancel();
            Intent intent = new Intent(getApplicationContext(), GameResultBouncyBall.class);
            intent.putExtra("success", 1);
            intent.putExtra("from", 2);
            startActivity(intent);
            finish();

        }
        if(blockinfo[1][0] < 150){
            block2V = 2;
        }
        else if(blockinfo[1][1] > frameWidth ){
            block2V = -2;
        }
        blockinfo[1][0] += block2V;
        blockinfo[1][1] += block2V;

        block[1].setX(blockinfo[1][0]);


        if(left_flg){
            redy.setScaleX(1);
            if(up_flg_left) {
                redyVY = -1.35f - 0.005f * 5.0f;
                count = 0;
            }
            redyprevX = redyX;
            redyX -= 3;
        }
        if(right_flg){
            redy.setScaleX(-1);
            if(up_flg_right){
                redyVY = -1.35f - 0.005f * 5.0f;
                count = 0;
            }
            redyprevX = redyX;
            redyX += 3;
        }
        if(count<=0) {
            up_flg_right = false;
            up_flg_left = false;
        }
        count--;

        hitCheck();

        // Touching
        redyVY += 0.005*5.0;
        Log.i("velocity",redyVY+"");
        redyprevY = redyY;
        redyY += redyVY * 5.0;
        //Log.i("down", "y :" + redyY+" time : " + time +" velocity : " + redyVY);


        if(redyX < 0){
            redyprevX = redyX;
            redyX = 0;
        }
        if(redyX > frameWidth - redySize){
            redyprevX = redyX;
            redyX = frameWidth - redySize;
        }

        if(redyY > frameHeight){
            redy.setX(150);
            redyprevX = 150;
            redyX = 150;
            redy.setY(groundY - height);
            redyY = groundY - height;
            redyprevY = redyY;
            heart--;
            if(heart <= 0) {
                timer.cancel();
                timer = null;
                timer2.cancel();
                Intent intent = new Intent(getApplicationContext(), GameResultBouncyBall.class);
                intent.putExtra("success", 0);
                intent.putExtra("from", 2);
                startActivity(intent);
                finish();
            }
        }
        else {
            redy.setY(redyY);
            redy.setX(redyX);
        }

    }
    public void hitCheck(){
        for(int i = 0; i < 4 ; i++){
            if(redyY <= blockinfo[i][3] && redyY + redySize >= blockinfo[i][2] && redyX <= blockinfo[i][1] && redyX + redySize >= blockinfo[i][0]){
                // Log.i("info1", redyX+","+blockinfo[i][0]+","+blockinfo[i][1]);
                //Log.i("info2", redyY+","+blockinfo[i][2]+","+blockinfo[i][3]);
                if(redyprevX +redySize < blockinfo[i][0]){
                    up_flg_left = true;
                    count = 3;
                    redyprevX = redyX;
                    redyX = blockinfo[i][0] - redySize - 3;
                }else if(redyprevX > blockinfo[i][1]){
                    up_flg_right = true;
                    count = 3;
                    redyprevX = redyX;
                    redyX = blockinfo[i][1] + 3;
                }
                if(redyprevY + redySize < blockinfo[i][2])
                    redyVY = -1.35f -0.005f*5.0f;
                else if(redyprevY > blockinfo[i][3])
                    redyVY = 1.35f -0.005f*5.0f;
            }
        }
    }




    public boolean onTouchEvent(MotionEvent me) {

        float x = me.getX();
        float y = me.getY();


        if (start_flg == false) {

            start_flg = true;

            FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
            frameHeight = frame.getHeight();
            frameWidth = frame.getWidth();

            for(int i = 0; i < 4; i++){
                blockinfo[i][0] = block[i].getX();
                blockinfo[i][1] = block[i].getX() + block[i].getWidth();
                blockinfo[i][2] = block[i].getY();
                blockinfo[i][3] = block[i].getY() + block[i].getHeight();
            }

            groundY = blockinfo[0][2];

            timer2 = new CountDownTimer(40000, 1000) {

                public void onTick(long millisUntilFinished) {
                    timerText.setText("seconds remaining: " + millisUntilFinished / 1000);
                }

                public void onFinish() {
                    timer.cancel();
                    timer = null;
                    Intent intent = new Intent(getApplicationContext(), GameResultBouncyBall.class);
                    intent.putExtra("success", 0);
                    intent.putExtra("from", 2);
                    startActivity(intent);
                    finish();
                    timerText.setText("done!");
                }
            }.start();

            redyY = (int) redy.getY();
            redySize = redy.getHeight();

            starX = star.getX();
            starY = star.getY();
            redy.setX(150);
            redyX = 150;
            redy.setY(groundY - height);
            redyY = groundY - height;
            redyVY = 0;
            redyprevX = redyX;
            redyprevY = redyY;
            time = 0;
            Log.i("groundY", groundY + "");

            startLabel.setVisibility(View.GONE);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }, 0, 5);


        }
        else{
            if(x < frameWidth / 3 && me.getAction() == MotionEvent.ACTION_DOWN){
                left_flg = true;
                right_flg = false;
            }
            else if(x > frameWidth * 2/3 && me.getAction() == MotionEvent.ACTION_DOWN){
                right_flg = true;
                left_flg = false;
            }
            else if(me.getAction() == MotionEvent.ACTION_UP){
                left_flg = false;
                right_flg = false;
            }
        }

        return true;
    }

}
