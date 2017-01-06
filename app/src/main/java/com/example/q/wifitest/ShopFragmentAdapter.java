package com.example.q.wifitest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by q on 2017-01-05.
 */

public class ShopFragmentAdapter extends RecyclerView.Adapter<ShopFragmentAdapter.CustomViewHolder>  {
    private Integer[] images = { R.drawable.bg1, R.drawable.bg2, R.drawable.bg3, R.drawable.bg4, R.drawable.bg5, R.drawable.bg6 };
    private Integer[] texts = {R.string.font1, R.string.font2, R.string.font3, R.string.font4, R.string.font5, R.string.font6 };
    private int from;
    private Context mContext;

    public ShopFragmentAdapter(Context context, int from) {
        mContext = context;
        this.from = from;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        switch (from) {
            case 0 :
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.background_item, viewGroup, false);
                break;
            case 1 :
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.font_item, viewGroup, false);
                break;
            case 2 :
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.etc_item, viewGroup, false);
                break;
            default :
                view = null;
                break;
        }
        ShopFragmentAdapter.CustomViewHolder viewHolder = new ShopFragmentAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int i) {
        switch (from) {
            case 0 :
                customViewHolder.imageView.setImageResource(images[i]);
                break;
            case 1 :
                customViewHolder.textView.setText(texts[i]);
                break;
            case 2 :
                customViewHolder.textView.setText(texts[i]);
                break;
        }

        customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder buyDialog = new AlertDialog.Builder(mContext);
                buyDialog.setTitle("Buy");
                buyDialog.setMessage(i + "번째 아이템을 구입하시겠습니까?");
                buyDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO : buy item
                        dialog.cancel();
                        AlertDialog.Builder buyCompleteDialog = new AlertDialog.Builder(mContext);
                        buyCompleteDialog.setTitle("Buy Complete");
                        buyCompleteDialog.setMessage("구입이 완료되었습니다.");
                        buyCompleteDialog.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        buyCompleteDialog.show();
                    }
                });
                buyDialog.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                buyDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView textView;

        public CustomViewHolder(View view) {
            super(view);
            switch (from) {
                case 0 :
                    this.imageView = (ImageView) view.findViewById(R.id.background_item_image);
                    break;
                case 1 :
                    this.textView = (TextView) view.findViewById(R.id.font_item_text);
                    break;
                case 2 :
                    this.textView = (TextView) view.findViewById(R.id.etc_item_text);
                    break;
            }
        }
    }
}