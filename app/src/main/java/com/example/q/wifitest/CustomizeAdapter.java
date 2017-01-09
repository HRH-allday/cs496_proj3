package com.example.q.wifitest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by q on 2017-01-05.
 */

public class CustomizeAdapter extends FragmentPagerAdapter {
    int tabCnt;
    private BackgroundFragment backgroundFragment;
    private FontFragment fontFragment;
    private EtcCustomizeFragment etcCustomizeFragment;

    public CustomizeAdapter(FragmentManager fm, int tabCnt) {
        super(fm);
        this.tabCnt = tabCnt;

        backgroundFragment = new BackgroundFragment();
        backgroundFragment.setFrom(MainActivity.CREATED_FROM_CUSTOMIZE);
        fontFragment = new FontFragment();
        fontFragment.setFrom(MainActivity.CREATED_FROM_CUSTOMIZE);
        etcCustomizeFragment = new EtcCustomizeFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0 :
                return backgroundFragment;
            case 1 :
                return fontFragment;
            case 2 :
                return etcCustomizeFragment;
            default :
                return null;
        }
    }

    @Override
    public int getCount() { return tabCnt; }
}
