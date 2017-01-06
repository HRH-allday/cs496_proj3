package com.example.q.wifitest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by q on 2017-01-05.
 */

public class ShopAdapter extends FragmentPagerAdapter {
    int tabCnt;
    private BackgroundFragment backgroundFragment;
    private FontFragment fontFragment;
    private EtcFragment etcFragment;

    public ShopAdapter(FragmentManager fm, int tabCnt) {
        super(fm);
        this.tabCnt = tabCnt;

        backgroundFragment = new BackgroundFragment();
        backgroundFragment.setFrom(MainActivity.CREATED_FROM_SHOP);
        fontFragment = new FontFragment();
        fontFragment.setFrom(MainActivity.CREATED_FROM_SHOP);
        etcFragment = new EtcFragment();
        etcFragment.setFrom(MainActivity.CREATED_FROM_SHOP);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0 :
                return backgroundFragment;
            case 1 :
                return fontFragment;
            case 2 :
                return etcFragment;
            default :
                return null;
        }
    }

    @Override
    public int getCount() { return tabCnt; }
}
