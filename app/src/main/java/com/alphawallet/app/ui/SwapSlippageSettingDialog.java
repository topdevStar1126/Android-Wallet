package com.alphawallet.app.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.Nullable;

import com.alphawallet.app.R;
import com.alphawallet.app.widget.InputView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SwapSlippageSettingDialog extends BottomSheetDialogFragment {

    Button rate1_Btn, rate2_Btn, custom_Btn;
    InputView custom_Input;
    int flg = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_slippage_setting,  container, false);

        rate1_Btn = v.findViewById(R.id.ratebtn1);
        rate2_Btn = v.findViewById(R.id.ratebtn2);
        custom_Btn = v.findViewById(R.id.ratebtn3);
        custom_Input = v.findViewById(R.id.custom_slippage);
        custom_Input.setVisibility(View.INVISIBLE);


        rate1_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),
                        "0.25%", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        rate2_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),
                        "0.35%", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        custom_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flg == 0) {
                    custom_Btn.setText("OK");
                    custom_Input.setVisibility(View.VISIBLE);
                    flg  = 1;
                } else {
                    flg  = 0;
                    custom_Btn.setText("Custom");
                    custom_Input.setVisibility(View.INVISIBLE);
                }
            }
        });
        return v;
    }

}
