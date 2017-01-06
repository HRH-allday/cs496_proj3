package com.example.q.wifitest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by q on 2017-01-06.
 */

public class ScanWork extends Thread {

    private HttpURLConnection huc;
    private URL u;
    private String ec2url = "http://ec2-52-79-95-160.ap-northeast-2.compute.amazonaws.com:3000";
    private String localurl = "http://143.248.49.213:3000";
    private JSONObject data;
    private String route;

    int count = 0;

    private JSONArray json_wifiinfo;
    private JSONObject postData;
    private WifiManager wifiManager;

    private ArrayList<ScanResult> mScanData = new ArrayList<ScanResult>();


    public void addItem(ScanResult sr){
        int level = sr.level;
        for(int i = 0 ; i<mScanData.size() ; i++){
            if( mScanData.get(i).level < level){
                mScanData.add(i, sr);
                return;
            }
        }
        mScanData.add(sr);
    }

    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                try {
                    Log.i("fin", "ds");
                    List<ScanResult> sr = wifiManager.getScanResults();
                    int count = 0;
                    json_wifiinfo = new JSONArray();
                    mScanData.clear();
                    for (ScanResult s : sr) {
                        if (s.SSID.equals("Welcome_KAIST")) {
                            addItem(s);
                            count += 1;
                        }
                    }
                    for(ScanResult s : mScanData){
                        JSONObject jobj = new JSONObject();
                        jobj.put("bssid", s.BSSID);
                        jobj.put("rssi", s.level);
                        json_wifiinfo.put(jobj);

                    }
                    postData = new JSONObject();
                    postData.put("token", FirebaseInstanceId.getInstance().getToken());
                    postData.put("info", json_wifiinfo);
                    PostThread p = new PostThread(postData, "/wifi_info");
                    p.start();

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            /*
            else if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                sendBroadcast(new Intent("wifi.ON_NETWORK_STATE_CHANGED"));
            }
            */
        }
    };
    @Override
    public void run() {
        super.run();
        while(true) {
            Log.i("entered", "hi");
            try {
                u = new URL(ec2url +  "/wifi_info");
                Log.i("connected", "");
                huc = (HttpURLConnection) u.openConnection();
                Log.i("open", "");
                huc.setRequestMethod("POST");
                huc.setDoInput(true);
                huc.setDoOutput(true);
                huc.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                OutputStream os = huc.getOutputStream();
                Log.i("sended", postData.toString());
                os.write((postData.toString()).getBytes("utf-8"));
                os.flush();
                os.close();
                InputStream is = huc.getInputStream();
                byte[] arr = new byte[is.available()];
                is.read(arr);
                is.close();
                sleep(4000);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
