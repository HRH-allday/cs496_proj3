package com.example.q.wifitest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by q on 2017-01-09.
 */

public class EtcFontFragment extends Fragment {
    View view;
    EditText editText;
    Button button;
    int type;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.etc_font_fragment, container, false);
        editText = (EditText) view.findViewById(R.id.etc_font_input);
        if (type == EtcCustomizeFragmentAdapter.FRAGMENT_TYPE_COLOR)
            editText.setHint("폰트 색 입력");

        button = (Button) view.findViewById(R.id.etc_font_submit);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputStr = editText.getText().toString().trim();
                switch (type) {
                    case EtcCustomizeFragmentAdapter.FRAGMENT_TYPE_SIZE :
                        if (isInteger(inputStr, 10)) {
                            editText.setText("");
                            ((CustomizeActivity)getActivity()).setPreviewTextSize(Integer.parseInt(inputStr));
                        } else {
                            Toast.makeText(getActivity(), "숫자만 입력 가능합니다", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case EtcCustomizeFragmentAdapter.FRAGMENT_TYPE_COLOR :
                        if (isInteger(inputStr, 16) && inputStr.length() == 6) {
                            editText.setText("");
                            ((CustomizeActivity)getActivity()).setPreviewTextColor(inputStr);
                        } else {
                            Toast.makeText(getActivity(), "6자리의 16진수를 입력해주세요", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });

        return view;
    }

    public void setType(int type) { this.type = type; }

    public boolean isInteger(String str, int radix) {
        if (str.isEmpty()) return false;
        for (int i = 0; i < str.length(); i++)
            if (Character.digit(str.charAt(i), radix) < 0) return false;

        return true;
    }
}
