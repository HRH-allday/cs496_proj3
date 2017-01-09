package com.example.q.wifitest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by q on 2017-01-09.
 */

public class IsSpacedFragment extends Fragment {
    View view;
    Button button;

    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.isspaced_fragment, container, false);

        button = (Button) view.findViewById(R.id.isspaced_change_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : 클릭하면 스페이스바 된 상태 <-> 스페이스바 되지 않은 상태 변경
            }
        });

        return view;
    }
}
