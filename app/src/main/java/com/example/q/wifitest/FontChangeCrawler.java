package com.example.q.wifitest;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Type;

/**
 * Created by q on 2017-01-09.
 */

public class FontChangeCrawler {
    private Typeface typeface;

    public FontChangeCrawler(Typeface typeface) {
        this.typeface = typeface;
    }

    public FontChangeCrawler(AssetManager assets, String assetsFontFileName) {
        typeface = Typeface.createFromAsset(assets, assetsFontFileName);
    }

    public void replaceFonts(ViewGroup viewTree) {
        View child;

        for (int i = 0; i < viewTree.getChildCount(); i++) {
            System.out.println("check");
            child = viewTree.getChildAt(i);

            if (child instanceof ViewGroup) {
                replaceFonts((ViewGroup) child);
            } else if (child instanceof TextView) {
                System.out.println("wtf : " + ((TextView)child).getText().toString());
                ((TextView) child).setTypeface(typeface);
            }
        }
    }
}
