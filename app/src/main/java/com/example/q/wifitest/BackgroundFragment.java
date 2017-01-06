package com.example.q.wifitest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by q on 2017-01-05.
 */

public class BackgroundFragment extends Fragment {
    private int from;
    private RecyclerView backgroundList;
    private RecyclerView.Adapter adapter;

    public BackgroundFragment() { from = -1; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.items_fragment, null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        backgroundList = (RecyclerView) view.findViewById(R.id.items_recyclerview);
        backgroundList.setLayoutManager(layoutManager);

        if (from == MainActivity.CREATED_FROM_SHOP)
            adapter = (RecyclerView.Adapter) new ShopFragmentAdapter(getContext(), 0);
        else if (from == MainActivity.CREATED_FROM_CUSTOMIZE)
            adapter = (RecyclerView.Adapter) new CustomizeFragmentAdapter((CustomizeActivity)getActivity(), 0);
        backgroundList.setAdapter(adapter);

        return view;
    }

    public void setFrom(int from) {
        this.from = from;
    }
}
