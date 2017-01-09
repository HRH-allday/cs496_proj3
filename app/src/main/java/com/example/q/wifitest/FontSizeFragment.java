package com.example.q.wifitest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by q on 2017-01-09.
 */

public class FontSizeFragment extends Fragment {
    View view;
    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.font_size_fragment, container, false);

        return view;
    }
}
