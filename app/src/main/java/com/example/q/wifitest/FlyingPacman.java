package com.example.q.wifitest;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class FlyingPacman extends AppCompatActivity {

    private TextView scoreLabel;
    private TextView startLabel;
    private ImageView box;
    private ImageView orange;
    private ImageView pink;
    private ImageView blue;
    private ImageView black1;
    private ImageView black2;
    private ImageView black3;
    private ImageView black4;
    private ImageView black5;
    private ImageView black6;
    private ImageView[] black = new ImageView[10];

    private int frameHeight;
    private int boxSize;
    private int screenWidth;
    private int screenHeight;

    // Position
    private int boxY;
    private int orangeX;
    private int orangeY;
    private int blueX;
    private int blueY;
    private int pinkX;
    private int pinkY;
    /*
    private int black1X;
    private int black1Y;
    private int black2X;
    private int black2Y;
    private int black3X;
    private int black3Y;
    private int black4X;
    private int black4Y;
    private int black5X;
    private int black5Y;
    private int black6X;
    private int black6Y;
*/
    private int[] blackX = new int[10];
    private int[] blackY = new int[10];
    private int[] blackspeed = new int[10];


    // Initialize Class
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    // Status Check
    private boolean action_flg = false;
    private boolean start_flg = false;

    private int score = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacman);

        scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        startLabel = (TextView) findViewById(R.id.startLabel);
        box = (ImageView) findViewById(R.id.box);
        orange = (ImageView) findViewById(R.id.yellowy);
        pink = (ImageView) findViewById(R.id.redy);
        blue = (ImageView) findViewById(R.id.bluey);
        black[0] = (ImageView) findViewById(R.id.black1);
        black[1] = (ImageView) findViewById(R.id.black2);
        black[2] = (ImageView) findViewById(R.id.black3);
        black[3] = (ImageView) findViewById(R.id.black4);
        black[4] = (ImageView) findViewById(R.id.black5);
        black[5] = (ImageView) findViewById(R.id.black6);
        black[6] = (ImageView) findViewById(R.id.black7);
        black[7] = (ImageView) findViewById(R.id.black8);
        black[8] = (ImageView) findViewById(R.id.black9);
        black[9] = (ImageView) findViewById(R.id.black10);




        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        // Move to out of screen.
        orange.setX(-80);
        orange.setY(-80);
        pink.setX(-80);
        pink.setY(-80);
        blue.setX(-80);
        blue.setY(-80);
        /*
        black1.setX(-150);
        black1.setY(-150);
        black2.setX(-150);
        black2.setY(-150);
        black3.setX(-150);
        black3.setY(-150);
        black4.setX(-150);
        black4.setY(-150);
        black5.setX(-150);
        black5.setY(-150);
        black6.setX(-150);
        black6.setY(-150);
*/
        for(int i = 0; i < 10 ; i++){
            black[i].setX(-150);
            black[i].setY(-150);
            blackspeed[i] = (int) Math.floor(Math.random() * 20) + 20;
        }

        scoreLabel.setText("Score : "+score);

        boxY = 500;

    }

    @Override
    protected void onResume() {
        super.onResume();
        score = 0;
    }

    public void changePos() {

        hitCheck();

        orangeX -= 12;
        if (orangeX < 0){
            orangeX = screenWidth + 20;
            orangeY = (int) Math.floor(Math.random() * (frameHeight - orange.getHeight()));
        }
        orange.setX(orangeX);
        orange.setY(orangeY);

        blueX -= 15;
        if (blueX < 0){
            blueX = screenWidth + 20;
            blueY = (int) Math.floor(Math.random() * (frameHeight - blue.getHeight()));
        }
        blue.setX(blueX);
        blue.setY(blueY);

        pinkX -= 18;
        if (pinkX < 0){
            pinkX = screenWidth + 20;
            pinkY = (int) Math.floor(Math.random() * (frameHeight - pink.getHeight()));
        }
        pink.setX(pinkX);
        pink.setY(pinkY);

        /*
        black1X -= 14;
        if (black1X < 0){
            black1X = screenWidth + 20;
            black1Y = (int) Math.floor(Math.random() * (frameHeight - black1.getHeight()));
        }
        black1.setX(black1X);
        black1.setY(black1Y);
        */

        for(int i = 0; i < 10 ; i++){
            blackX[i] -= blackspeed[i];
            if (blackX[i] < 0){
                blackspeed[i] = (int) Math.floor(Math.random() * 20) + 20;
                blackX[i] = screenWidth + 20;
                blackY[i] = (int) Math.floor(Math.random() * (frameHeight - black[i].getHeight()));
            }
            black[i].setX(blackX[i]);
            black[i].setY(blackY[i]);
        }


        if (action_flg == true) {
            // Touching
            boxY -= 20;

        } else {
            // Releasing
            boxY += 20;
        }
        if(boxY < 0) boxY = 0;
        if(boxY > frameHeight - boxSize) boxY = frameHeight - boxSize;

        box.setY(boxY);

        scoreLabel.setText("Score : "+score);
    }

    public void hitCheck(){
        int orangeCenterX = orangeX + orange.getWidth() / 2;
        int orangeCenterY = orangeY + orange.getHeight() / 2;

        if(0 <= orangeCenterX && orangeCenterX <= boxSize && boxY <= orangeCenterY && orangeCenterY <= boxSize + boxY){
            score += 20;
            orangeX = -10;
        }

        int pinkCenterX =pinkX + pink.getWidth() / 2;
        int pinkCenterY = pinkY + pink.getHeight() / 2;

        if(0 <= pinkCenterX && pinkCenterX <= boxSize && boxY <= pinkCenterY && pinkCenterY <= boxSize + boxY){
            score += 30;
            pinkX = -10;
        }

        int blueCenterX = blueX + blue.getWidth() / 2;
        int blueCenterY = blueY + blue.getHeight() / 2;

        if(0 <= blueCenterX && blueCenterX <= boxSize && boxY <= blueCenterY && blueCenterY <= boxSize + boxY){
            score += 50;
            blueX = -10;
        }

        for(int i = 0; i < 10 ; i++){
            int blackCenterX = blackX[i] + black[i].getWidth() / 2;
            int blackCenterY = blackY[i] + black[i].getHeight() / 2;
            if(0 <= blackCenterX && blackCenterX <= boxSize && boxY <= blackCenterY && blackCenterY <= boxSize + boxY){
                timer = null;

                Intent intent = new Intent(getApplicationContext(), GameResultPacman.class);
                intent.putExtra("score", score);
                startActivity(intent);

            }
        }



    }

    public boolean onTouchEvent(MotionEvent me) {

        if (start_flg == false) {

            start_flg = true;

            FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
            frameHeight = frame.getHeight();

            boxY = (int) box.getY();
            boxSize = box.getHeight();


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
            }, 0, 20);


        } else {
            if (me.getAction() == MotionEvent.ACTION_DOWN) {
                action_flg = true;

            } else if (me.getAction() == MotionEvent.ACTION_UP) {
                action_flg = false;

            }
        }

        return true;
    }

}
