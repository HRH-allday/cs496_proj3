package com.example.q.wifitest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by q on 2017-01-09.
 */

public class EtcCustomizeFragmentAdapter extends FragmentPagerAdapter {
    static final int FRAGMENT_TYPE_SIZE = 1023;
    static final int FRAGMENT_TYPE_COLOR = 1022;
    int tabCnt;
    private EtcFontFragment fontSizeFragment;
    private EtcFontFragment fontColorFragment;
    private IsSpacedFragment isSpacedFragment;
    private CustomizeActivity customizeActivity;

    public EtcCustomizeFragmentAdapter(CustomizeActivity ca, FragmentManager fm, int tabCnt) {
        super(fm);
        this.tabCnt = tabCnt;
        this.customizeActivity = ca;

        fontSizeFragment = new EtcFontFragment();
        fontSizeFragment.setType(FRAGMENT_TYPE_SIZE);
        fontColorFragment = new EtcFontFragment();
        fontColorFragment.setType(FRAGMENT_TYPE_COLOR);
        isSpacedFragment = new IsSpacedFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0 :
                return fontSizeFragment;
            case 1 :
                return fontColorFragment;
            case 2 :
                return isSpacedFragment;
            default :
                return null;
        }
    }

    @Override
    public int getCount() { return tabCnt; }
}
