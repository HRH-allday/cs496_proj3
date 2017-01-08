package com.example.q.wifitest;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by q on 2017-01-07.
 */

public class ClickFrameAdapter extends RecyclerView.Adapter<ClickFrameAdapter.CustomViewHolder>{
    private ClickFollower clickFollower;
    private int level;
    private int sideLength;
    private int bright_btn;
    private boolean clickable;
    private boolean gameOver;
    private int clickNum;

    public ClickFrameAdapter(ClickFollower clickFollower, int level, int sideLength, int bright_btn, boolean clickable, boolean gameOver) {
        this.level = level;
        this.sideLength = sideLength;
        this.bright_btn = bright_btn;
        this.clickable = clickable;
        this.clickFollower = clickFollower;
        this.gameOver = gameOver;
        clickNum = 0;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        int position;

        public CustomViewHolder(View v){
            super(v);
            imageView = (ImageView) v.findViewById(R.id.tab_btn);
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.clicktab, parent, false);
        ClickFrameAdapter.CustomViewHolder vh = new ClickFrameAdapter.CustomViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position){
        holder.position = position;
        GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams)holder.itemView.getLayoutParams();
        params.width = sideLength;
        params.height = sideLength;
        holder.itemView.setLayoutParams(params);
        if (gameOver)
            holder.imageView.setImageResource(R.drawable.square_button_gameover);
        else if (bright_btn == -1 || bright_btn == position)
            holder.imageView.setImageResource(R.drawable.square_button_bright);
        else
            holder.imageView.setImageResource(R.drawable.square_button);

        if (!clickable) {
            holder.itemView.setClickable(false);
        } else {
            holder.itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN :
                            holder.imageView.setImageResource(R.drawable.square_button_bright);
                            break;
                        case MotionEvent.ACTION_UP :
                        case MotionEvent.ACTION_CANCEL :
                            holder.imageView.setImageResource(R.drawable.square_button);
                            break;
                    }

                    return false;
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickNum < level * level) {
                        clickNum++;
                        clickFollower.onClick(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount(){
        return level*level;
    }
}
