package com.example.q.wifitest;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.NoSuchElementException;

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
                /*
                LinearLayout.LayoutParams imageViewParams = (LinearLayout.LayoutParams) customViewHolder.imageView.getLayoutParams();

                imageViewParams.width = (int)(((float) 1440 / 2560) * customViewHolder.itemView.getHeight());
                imageViewParams.height = customViewHolder.itemView.getHeight();

                customViewHolder.imageView.setLayoutParams(imageViewParams);

                scaleImage(customViewHolder.imageView, imageViewParams.width, imageViewParams.height);
                */
                customViewHolder.imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        LinearLayout.LayoutParams imageViewParams = (LinearLayout.LayoutParams) customViewHolder.imageView.getLayoutParams();

                        imageViewParams.width = (int)(((float) 1440 / 2560) * customViewHolder.itemView.getHeight());
                        imageViewParams.height = customViewHolder.itemView.getHeight();

                        customViewHolder.imageView.setLayoutParams(imageViewParams);

                        scaleImage(customViewHolder.imageView, imageViewParams.width, imageViewParams.height);
                    }
                });

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
                        customizeActivity.setPreviewBackground(i);
                        break;
                    case 1:
                        customizeActivity.setPreviewFont(i);
                        break;
                    default:
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

        }
    }

    private void scaleImage(ImageView view, int boundingWidth, int boundingHeight) throws NoSuchElementException {
        // Get bitmap from the the ImageView.
        Bitmap bitmap = null;

        Drawable drawing = view.getDrawable();
        bitmap = ((BitmapDrawable) drawing).getBitmap();

        // Get current dimensions AND the desired bounding box
        int width = 0;
        int height = 0;

        try {
            width = bitmap.getWidth();
            height = bitmap.getHeight();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Can't find bitmap on given view/drawable");
        }

        Log.i("Test", "original width = " + Integer.toString(width));
        Log.i("Test", "original height = " + Integer.toString(height));
        Log.i("Test", "bounding width = " + Integer.toString(boundingWidth));
        Log.i("Test", "bounding height = " + Integer.toString(boundingHeight));

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) boundingWidth) / width;
        float yScale = ((float) boundingHeight) / height;
        Log.i("Test", "xScale = " + Float.toString(xScale));
        Log.i("Test", "yScale = " + Float.toString(yScale));

        Bitmap scaledBitmap;
        if (xScale > yScale) {
            int newHeight = (int) (boundingHeight / xScale);
            Log.i("Test", "new height = " + newHeight);
            scaledBitmap = Bitmap.createBitmap(bitmap, 0, (height - newHeight) / 2 , width, newHeight, new Matrix(), true);
        } else {
            int newWidth = (int) (boundingWidth / yScale);
            Log.i("Test", "new width = " + newWidth);
            scaledBitmap = Bitmap.createBitmap(bitmap, (width - newWidth) / 2, 0, newWidth, height, new Matrix(), true);
        }

        // Create a new bitmap and convert it to a format understood by the ImageView
        width = scaledBitmap.getWidth(); // re-use
        height = scaledBitmap.getHeight(); // re-use
        Log.i("Test", "scaled width = " + Integer.toString(width));
        Log.i("Test", "scaled height = " + Integer.toString(height));

        // Apply the scaled bitmap
        view.setImageBitmap(scaledBitmap);
    }
}