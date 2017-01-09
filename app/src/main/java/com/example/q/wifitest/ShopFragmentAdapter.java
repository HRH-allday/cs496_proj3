package com.example.q.wifitest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by q on 2017-01-05.
 */

public class ShopFragmentAdapter extends RecyclerView.Adapter<ShopFragmentAdapter.CustomViewHolder>{
    private Integer[] images = Arrays.copyOfRange(MainActivity.images, 1, MainActivity.images.length);
    private Integer[] imagePrices = {20, 30, 40, 50, 60, 70};
    private Integer[] texts = Arrays.copyOfRange(MainActivity.texts, 1, MainActivity.texts.length);
    private int from;
    private Context mContext;
    private BackgroundFragment bf = null;
    private FontFragment ff = null;
    private EtcFragment ef = null;
    private ShopActivity sa;
    private int job;
    private int money;


    public ShopFragmentAdapter(Context context, int from) {
        mContext = context;
        this.from = from;
    }
    public ShopFragmentAdapter(Context context, int from, BackgroundFragment backgroundFragment) {
        mContext = context;
        this.from = from;
        this.bf = backgroundFragment;
        this.job = 0;
    }
    public ShopFragmentAdapter(Context context, int from, FontFragment fontFragment) {
        mContext = context;
        this.from = from;
        this.ff = fontFragment;
        this.job = 1;
    }
    public ShopFragmentAdapter(Context context, int from, EtcFragment etcFragment) {
        mContext = context;
        this.from = from;
        this.ef = etcFragment;
        this.job = 2;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        this.money = MainActivity.coin;
        switch (from) {
            case 0 :
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.background_item, viewGroup, false);
                break;
            case 1 :
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.font_item, viewGroup, false);
                break;
            case 2 :
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.etc_shop_item, viewGroup, false);
                break;
            default :
                view = null;
                break;
        }
        ShopFragmentAdapter.CustomViewHolder viewHolder = new ShopFragmentAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int position) {
        switch (from) {
            case 0 :
                customViewHolder.imageView.setImageResource(images[position]);
                break;
            case 1 :
                customViewHolder.textView.setText(texts[position]);
                break;
            case 2 :
                customViewHolder.textView.setText(texts[position]);
                break;
        }

        customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int price = imagePrices[position];
                if(price > money){
                    AlertDialog.Builder noMoney = new AlertDialog.Builder(mContext);
                    noMoney.setTitle("Need More Money");
                    noMoney.setMessage("돈이 부족합니다");
                    noMoney.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    return;
                }
                else {
                    AlertDialog.Builder buyDialog = new AlertDialog.Builder(mContext);
                    buyDialog.setTitle("Buy");
                    buyDialog.setMessage(position + 1 + "번째 아이템을 구입하시겠습니까?");
                    buyDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            new ShopFragmentAdapter.SaveBuyInfo().execute(from, position, price);
                            dialog.cancel();
                        }
                    });
                    buyDialog.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    buyDialog.show();
                }
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


    private class SaveBuyInfo extends AsyncTask<Integer, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Integer... params) {
            URL url;
            StringBuffer response = null;
            JSONObject jobject = null;
            int type = params[0];
            int position = params[1];
            int price = params[2];

            try {
                url = new URL("http://ec2-52-79-95-160.ap-northeast-2.compute.amazonaws.com:3000/buy_item");
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

                String token = FirebaseInstanceId.getInstance().getToken();

                jobject = new JSONObject();
                jobject.put("token", token);
                jobject.put("buy_type", type);
                if (type == 0 || type == 1)
                    jobject.put("buy_item", position + 1);
                else
                    jobject.put("buy_item", position);
                jobject.put("left_money", money);

                OutputStream out_stream = conn.getOutputStream();

                out_stream.write(jobject.toString().getBytes());
                out_stream.flush();
                out_stream.close();

                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + conn.getResponseCode());
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                response = new StringBuffer();
                String input_line;

                while ((input_line = in.readLine()) != null) {
                    System.out.println("input_line : " + input_line);
                    response.append(input_line);
                }
                in.close();

                jobject = new JSONObject(response.toString());
                jobject.put("price", price);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return jobject;
        }

        @Override
        protected void onProgressUpdate(Void... params) {

        }

        @Override
        protected void onPostExecute(JSONObject jobject) {
            try {
                System.out.println(jobject.getString("result"));
                if (jobject.getString("result").compareTo("success") == 0) {
                    AlertDialog.Builder buyCompleteDialog = new AlertDialog.Builder(mContext);
                    buyCompleteDialog.setTitle("Buy Complete");
                    buyCompleteDialog.setMessage("구입이 완료되었습니다.");
                    buyCompleteDialog.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    buyCompleteDialog.show();

                    money -= jobject.getInt("price");
                    switch (job) {
                        case 0 :
                            bf.onShopClickHandler(money);
                            break;
                        case 1 :
                            ff.onShopClickHandler(money);
                            break;
                        case 2 :
                            ef.onShopClickHandler(money);
                            break;
                    }
                } else {
                    AlertDialog.Builder alreadyBoughtDialog = new AlertDialog.Builder(mContext);
                    alreadyBoughtDialog.setTitle("Already Bought");
                    alreadyBoughtDialog.setMessage("이미 구입하셨던 항목입니다.");
                    alreadyBoughtDialog.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alreadyBoughtDialog.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}