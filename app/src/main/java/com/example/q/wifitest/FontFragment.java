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

public class FontFragment extends Fragment {
    private int from;
    private RecyclerView backgroundList;
    private RecyclerView.Adapter adapter;


    public FontFragment() { from = -1; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.items_fragment, null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        backgroundList = (RecyclerView) view.findViewById(R.id.items_recyclerview);
        backgroundList.setLayoutManager(layoutManager);

        if (from == MainActivity.CREATED_FROM_SHOP)
            adapter = (RecyclerView.Adapter) new ShopFragmentAdapter(getContext(), 1, this);
        else if (from == MainActivity.CREATED_FROM_CUSTOMIZE)
            adapter = (RecyclerView.Adapter) new CustomizeFragmentAdapter((CustomizeActivity)getActivity(), 1);
        backgroundList.setAdapter(adapter);

        return view;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public void onShopClickHandler(int coinLeft){
        TextView coinView = (TextView) getActivity().findViewById(R.id.shop_coin);
        coinView.setText(coinLeft+"원");
    }
}
