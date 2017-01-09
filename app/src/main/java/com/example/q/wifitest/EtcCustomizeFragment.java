package com.example.q.wifitest;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by q on 2017-01-09.
 */

public class EtcCustomizeFragment extends Fragment implements TabLayout.OnTabSelectedListener {
    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private EtcCustomizeFragmentAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.etc_customize_item, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.etc_customize_tablayout);

        tabLayout.addTab(tabLayout.newTab().setText("폰트 크기"));
        tabLayout.addTab(tabLayout.newTab().setText("폰트 색깔"));
        tabLayout.addTab(tabLayout.newTab().setText("스페이스"));

        viewPager = (ViewPager) view.findViewById(R.id.etc_customize_viewpager);

        adapter = new EtcCustomizeFragmentAdapter((CustomizeActivity)getActivity(), getFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(this);

        return view;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
