package com.example.q.wifitest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by q on 2017-01-07.
 */

public class ClickFrameAdapter extends RecyclerView.Adapter<ClickFrameAdapter.CustomViewHolder> {
    private int level;
    private Context mContext;

    public ClickFrameAdapter(Context context, int level){
        mContext = context;
        this.level = level;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        public CustomViewHolder(View v){
            super(v);
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(mContext).inflate(R.layout.custom_view,parent,false);
        CustomViewHolder vh = new CustomViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position){

    }

    @Override
    public int getItemCount(){

    }
}
