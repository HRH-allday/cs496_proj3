package com.example.q.wifitest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by q on 2017-01-05.
 */

public class BackgroundFragment extends Fragment {
    private int from;
    private RecyclerView backgroundList;
    private RecyclerView.Adapter adapter;

    public BackgroundFragment(){
        from = -1;
    }


    public void setFrom(int from) {
        this.from = from;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        if (from == MainActivity.CREATED_FROM_SHOP) {
            view = inflater.inflate(R.layout.shop_items_fragment, null);
            backgroundList = (RecyclerView) view.findViewById(R.id.shop_items_recyclerview);
            adapter = new ShopFragmentAdapter(getContext(), 0, this);
        } else if (from == MainActivity.CREATED_FROM_CUSTOMIZE) {
            view = inflater.inflate(R.layout.customize_items_fragment, null);
            backgroundList = (RecyclerView) view.findViewById(R.id.customize_items_recyclerview);
            adapter = new CustomizeFragmentAdapter((CustomizeActivity) getActivity(), 0);
        } else
            view = null;

        backgroundList.setLayoutManager(layoutManager);
        backgroundList.setAdapter(adapter);

        return view;
    }
    public void onShopClickHandler(int coinLeft){
        TextView coinView = (TextView) getActivity().findViewById(R.id.shop_coin);
        coinView.setText("자산 : "+coinLeft+"원");
    }


}
