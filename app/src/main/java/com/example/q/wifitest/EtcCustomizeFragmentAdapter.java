package com.example.q.wifitest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.BaseAdapter;

/**
 * Created by q on 2017-01-09.
 */

public class EtcCustomizeFragmentAdapter extends FragmentPagerAdapter {
    int tabCnt;
    private FontSizeFragment fontSizeFragment1;
    private FontSizeFragment fontSizeFragment2;
    private FontSizeFragment fontSizeFragment3;

    public EtcCustomizeFragmentAdapter(FragmentManager fm, int tabCnt) {
        super(fm);
        this.tabCnt = tabCnt;

        fontSizeFragment1 = new FontSizeFragment();
        fontSizeFragment2 = new FontSizeFragment();
        fontSizeFragment3 = new FontSizeFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0 :
                return fontSizeFragment1;
            case 1 :
                return fontSizeFragment2;
            case 2 :
                return fontSizeFragment3;
            default :
                return null;
        }
    }

    @Override
    public int getCount() { return tabCnt; }
}
