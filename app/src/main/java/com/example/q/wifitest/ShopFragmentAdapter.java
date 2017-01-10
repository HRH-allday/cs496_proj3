package com.example.q.wifitest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * Created by q on 2017-01-05.
 */

public class ShopFragmentAdapter extends RecyclerView.Adapter<ShopFragmentAdapter.CustomViewHolder>{
    private Integer[] images = Arrays.copyOfRange(MainActivity.images, 1, MainActivity.images.length);
    private Integer[] imagePrices = {100, 150, 200, 250, 300, 500};
    private Integer[] fontPrices = {100, 150, 200, 250, 300, 500};
    private Integer[] etcPrices = {300, 300, 300};
    private Integer[] texts = Arrays.copyOfRange(MainActivity.texts, 1, MainActivity.texts.length);
    private String[] etcProducts = {"폰트 크기", "폰트 색", "띄어쓰기"};
    private int from;
    private Context mContext;
    private BackgroundFragment bf = null;
    private FontFragment ff = null;
    private EtcFragment ef = null;
    private ShopActivity shopActivity;
    private int job;


    public ShopFragmentAdapter(Context context, int from) {
        mContext = context;
        this.from = from;
    }
    public ShopFragmentAdapter(Context context, int from, BackgroundFragment backgroundFragment, ShopActivity shopActivity) {
        mContext = context;
        this.from = from;
        this.bf = backgroundFragment;
        this.job = 0;
        this.shopActivity = shopActivity;
    }
    public ShopFragmentAdapter(Context context, int from, FontFragment fontFragment, ShopActivity shopActivity) {
        mContext = context;
        this.from = from;
        this.ff = fontFragment;
        this.job = 1;
        this.shopActivity = shopActivity;
    }
    public ShopFragmentAdapter(Context context, int from, EtcFragment etcFragment, ShopActivity shopActivity) {
        mContext = context;
        this.from = from;
        this.ef = etcFragment;
        this.job = 2;
        this.shopActivity = shopActivity;
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
        ShopFragmentAdapter.CustomViewHolder viewHolder = new ShopFragmentAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, final int position) {
        switch (from) {
            case 0 :
                customViewHolder.imageView.setImageResource(images[position]);
                customViewHolder.imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        Display display = shopActivity.getWindowManager().getDefaultDisplay();
                        Point size = new Point();
                        display.getSize(size);
                        int screenWidth = size.x;
                        int screenHeight = size.y;

                        int imageHeight = customViewHolder.imageView.getHeight();

                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) customViewHolder.imageView.getLayoutParams();
                        params.width = (int)(((float) 1440 / 2560) * imageHeight);

                        customViewHolder.imageView.setLayoutParams(params);

                        scaleImage(customViewHolder.imageView);
                    }
                });
                break;
            case 1 :
                customViewHolder.textView.setText(texts[position]);
                break;
            case 2 :
                customViewHolder.textView.setText(etcProducts[position]);
                break;
        }

        customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int price;
                switch (from){
                    case 0:
                        price = imagePrices[position];
                        break;
                    case 1:
                        price = fontPrices[position];
                        break;
                    case 2:
                        price = etcPrices[position];
                        break;
                    default:
                        price = -1;
                        break;
                }
                if(price == -1){
                    AlertDialog.Builder badRequest = new AlertDialog.Builder(mContext);
                    badRequest.setTitle("Error");
                    badRequest.setMessage("잘못된 요청입니다");
                    badRequest.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    badRequest.show();
                    return;
                }
                else if(price > MainActivity.coin){
                    AlertDialog.Builder noMoney = new AlertDialog.Builder(mContext);
                    noMoney.setTitle("Need More Money");
                    noMoney.setMessage("돈이 부족합니다");
                    noMoney.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    noMoney.show();
                    return;
                }
                else {
                    AlertDialog.Builder buyDialog = new AlertDialog.Builder(mContext);
                    buyDialog.setTitle("Buy");
                    buyDialog.setMessage(position + 1 + "번째 아이템을 구입하시겠습니까?");
                    buyDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            new SaveBuyInfo().execute(from, position, price);
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
        switch (from) {
            case 0 :
                return images.length;
            case 1:
                return texts.length;
            case 2:
                return etcProducts.length;
            default:
                return 0;

        }
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView textView;

        public CustomViewHolder(View view) {
            super(view);
            switch (from) {
                case 0 :
                    imageView = (ImageView) view.findViewById(R.id.background_item_image);
                    break;
                case 1 :
                    textView = (TextView) view.findViewById(R.id.font_item_text);
                    break;
                case 2 :
                    textView = (TextView) view.findViewById(R.id.etc_item_text);
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
                jobject.put("left_money",MainActivity.coin);

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

                    MainActivity.coin -= jobject.getInt("price");
                    switch (job) {
                        case 0 :
                            bf.onShopClickHandler(MainActivity.coin);
                            break;
                        case 1 :
                            ff.onShopClickHandler(MainActivity.coin);
                            break;
                        case 2 :
                            ef.onShopClickHandler(MainActivity.coin);
                            break;
                    }
                } else if (jobject.getString("result").compareTo("error") == 0) {
                    AlertDialog.Builder alreadyBoughtDialog = new AlertDialog.Builder(mContext);
                    alreadyBoughtDialog.setTitle("Already Bought");
                    alreadyBoughtDialog.setMessage("이미 구입하셨던 항목입니다.");
                    alreadyBoughtDialog.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alreadyBoughtDialog.show();
                } else if (jobject.getString("result").compareTo("bad request") == 0) {
                    AlertDialog.Builder badRequestDialog = new AlertDialog.Builder(mContext);
                    badRequestDialog.setTitle("Bad Request");
                    badRequestDialog.setMessage("잘못된 요청입니다.");
                    badRequestDialog.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    badRequestDialog.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void scaleImage(ImageView view) throws NoSuchElementException {
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

        int boundingWidth = view.getWidth();
        int boundingHeight = view.getHeight();
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