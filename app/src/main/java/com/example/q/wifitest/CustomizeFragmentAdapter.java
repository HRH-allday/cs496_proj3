package com.example.q.wifitest;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by q on 2017-01-05.
 */

public class CustomizeFragmentAdapter extends RecyclerView.Adapter<CustomizeFragmentAdapter.CustomViewHolder>  {
    private Integer[] images = MainActivity.images;
    private Integer[] texts = MainActivity.texts;
    private int from;
    private CustomizeActivity customizeActivity;
    private ArrayList<Integer> bought_items;

    public CustomizeFragmentAdapter(CustomizeActivity customizeActivity, int from) {
        this.customizeActivity = customizeActivity;
        this.from = from;
        try {
            JSONArray jarray = new GetBoughtItems().execute().get();

            bought_items = new ArrayList<>();

            for (int i = 0; i < jarray.length(); i++) {
                bought_items.add(jarray.getInt(i));
            }

            Collections.sort(bought_items);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.etc_shop_item, viewGroup, false);
                break;
            default :
                view = null;
                break;
        }
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, final int i) {
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

        if (bought_items.contains(i)) {
            customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                switch (from) {
                    case 0:
                        customizeActivity.setPreviewBackground(images[i], i);
                        break;
                    case 1:
                        customizeActivity.setPreviewFont(texts[i], i);
                        break;
                    case 2:
                        customizeActivity.setPreviewEtc(texts[i], i);
                        break;
                }
                }
            });
        } else {
            customViewHolder.itemView.setClickable(false);
            customViewHolder.itemView.setEnabled(false);
        }
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

    private class GetBoughtItems extends AsyncTask<Void, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(Void... params) {
            URL url;
            StringBuffer response = null;
            JSONObject jobject = null;
            JSONArray jarray = null;

            try {
                url = new URL("http://ec2-52-79-95-160.ap-northeast-2.compute.amazonaws.com:3000/get_bought_items");
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

                String token = FirebaseInstanceId.getInstance().getToken();

                jobject = new JSONObject();
                jobject.put("token", token);
                jobject.put("buy_type", from);

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

                System.out.println("wtf : " + response.toString());

                jarray = new JSONArray(response.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return jarray;
        }

        @Override
        protected void onProgressUpdate(Void... params) {

        }

        @Override
        protected void onPostExecute(JSONArray jarray) {
            /*
            try {
                bought_items = new ArrayList<>();

                for (int i = 0; i < jarray.length(); i++) {
                    bought_items.add(jarray.getInt(i));
                }

                Collections.sort(bought_items);
            } catch (Exception e) {
                e.printStackTrace();
            }
            */
        }
    }
}