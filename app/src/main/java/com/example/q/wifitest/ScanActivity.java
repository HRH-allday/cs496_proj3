package com.example.q.wifitest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Created by q on 2017-01-06.
 */

public class ScanActivity extends AppCompatActivity {

    private Button check_btn;
    private Button set_btn;
    private TextView info_text;
    private TextView num_found;
    private String location = "";

    private ListView mListView = null;
    private ListViewAdapter mAdapter = null;


    private JSONArray json_wifiinfo;
    private JSONObject postData;
    private WifiManager wifiManager;

    private int set = 0;

    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 100;


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
                    mAdapter.clear();
                    for (ScanResult s : sr) {
                        if (s.SSID.equals("Welcome_KAIST")) {
                            mAdapter.addItem(s);
                            count += 1;
                        }
                    }
                    for (ScanResult s : mAdapter.mListData) {
                        JSONObject jobj = new JSONObject();
                        jobj.put("bssid", s.BSSID);
                        jobj.put("rssi", s.level);
                        json_wifiinfo.put(jobj);

                    }
                    postData = new JSONObject();
                    postData.put("token", FirebaseInstanceId.getInstance().getToken());
                    postData.put("location", location);
                    postData.put("info", json_wifiinfo);
                    if (set == 1) {
                        Toast.makeText(getApplicationContext(), "퀘스트 지점이 등록되었습니다!", Toast.LENGTH_LONG).show();
                        set = 0;
                        PostThread p = new PostThread(postData, "/set_location");
                        p.start();

                    } else {
                        PostThread p = new PostThread(postData, "/wifi_info");
                        p.start();
                    }
                    num_found.setText("number found : " + count);
                    //mAdapter.sort();
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);


        check_btn = (Button) findViewById(R.id.wifiCheck);
        set_btn = (Button) findViewById(R.id.setLocation);
        //info_text = (TextView) findViewById(R.id.info_text);
        num_found = (TextView) findViewById(R.id.numFound);

        FirebaseMessaging.getInstance().subscribeToTopic("news");
        final String token = FirebaseInstanceId.getInstance().getToken();
        Log.i("token", token);


        mListView = (ListView) findViewById(R.id.mList);
        mAdapter = new ListViewAdapter(this);
        mListView.setAdapter(mAdapter);


        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);


        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(mReceiver, filter);

        Button btn = (Button) findViewById(R.id.sendNotify);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jobj = new JSONObject();
                    jobj.put("token", token);
                    PostThread p = new PostThread(jobj, "/send_notification");
                    p.start();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        final ScanWork sw = new ScanWork();
        check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText loc = (EditText) findViewById(R.id.location);
                location = loc.getText().toString().trim();
                loc.setText("");
                Log.i("start", "hi");
                Toast.makeText(getApplicationContext(), "스캔 시작!", Toast.LENGTH_LONG).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Log.i("need", "perm");
                    requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
                } else {
                    wifiManager.startScan();
                }
                try {
                    sleep(4000);
                    sw.start();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
        Button stop_btn = (Button) findViewById(R.id.stop);
        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    sw.wait();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });

        set_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText loc = (EditText) findViewById(R.id.location);
                location = loc.getText().toString().trim();
                set = 1;
                loc.setText("");
                Log.i("start", "hi");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Log.i("need", "perm");
                    requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
                } else {
                    wifiManager.startScan();
                }

            }
        });

/*
        check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

                int network_type = activeNetwork.getType();

                String wifi_ssid = new String();
                String wifi_bssid = new String();
                String wifi_mac = new String();
                int wifi_ip;
                int wifi_speed;
                int wifi_freq ;
                if(network_type == cm.TYPE_WIFI){
                    wifi_ssid = wifiManager.getConnectionInfo().getSSID();
                    wifi_bssid = wifiManager.getConnectionInfo().getBSSID();
                    wifi_mac = wifiManager.getConnectionInfo().getMacAddress();
                    wifi_ip = wifiManager.getConnectionInfo().getIpAddress();
                    wifi_speed = wifiManager.getConnectionInfo().getLinkSpeed();
                    wifi_freq = wifiManager.getConnectionInfo().getFrequency();
                    info_text.setText("ssid : " + wifi_ssid + "\n bssid : " + wifi_bssid + "\n mac_address : "+wifi_mac + "\n ip : " + wifi_ip + "\n speed : " + wifi_speed + "\n frequency : " + wifi_freq);
                }



            }
        });

*/


    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Do something with granted permission
            wifiManager.startScan();
        }
    }

    private class ViewHolder {
        public TextView ssid;

        public TextView bssid;
        public TextView rssi;
    }

    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<ScanResult> mListData = new ArrayList<ScanResult>();

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void addItem(ScanResult sr) {
            int level = sr.level;
            for (int i = 0; i < mListData.size(); i++) {
                if (mListData.get(i).level < level) {
                    mListData.add(i, sr);
                    return;
                }
            }
            mListData.add(sr);
        }

        public void remove(int position) {
            mListData.remove(position);
            dataChange();
        }
        /*
        public final Comparator<ScanResult> ALPHA_COMPARATOR = new Comparator<ScanResult>() {
            private final Collator sCollator = Collator.getInstance();

            @Override
            public int compare(ScanResult mListDate_1, ScanResult mListDate_2) {
                return sCollator.compare(mListDate_1.level, mListDate_2.level);
            }
        };
        */

        public void dataChange() {

            notifyDataSetChanged();
        }

        public void clear() {
            mListData.clear();
        }

        /*
        public void sort(){
            Collections.sort(mListData, ALPHA_COMPARATOR);
            dataChange();
        }
           */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.item, null);

                holder.ssid = (TextView) convertView.findViewById(R.id.ssid);
                holder.bssid = (TextView) convertView.findViewById(R.id.bssid);
                holder.rssi = (TextView) convertView.findViewById(R.id.rssi);
                /*
                holder.mac = (TextView) convertView.findViewById(R.id.mac);
                holder.ip = (TextView) convertView.findViewById(R.id.ip);
                holder.speed = (TextView) convertView.findViewById(R.id.speed);
                holder.freq = (TextView) convertView.findViewById(R.id.freq);
                */

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ScanResult mData = mListData.get(position);


            holder.ssid.setText(mData.SSID);
            holder.bssid.setText(mData.BSSID);
            holder.rssi.setText(mData.level + "");

            return convertView;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public class ScanWork extends Thread {

        @Override
        public void run() {
            super.run();
            Log.i("entered", "hi");
            try {
                while (true) {
                    set = 0;
                    wifiManager.startScan();
                    sleep(4000);
                }
            }catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();

                return;
            }

        }
    }

}
