package com.alphawallet.app.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

import com.alphawallet.app.R;

public class PinCodeConfirmActivity extends BaseActivity {
    private EditText pinInputOrigin;
    private StringBuilder pinBuilder = new StringBuilder();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pincode_confirm);

        pinInputOrigin = findViewById(R.id.pin_input_origin);

        GridLayout numberPad = findViewById(R.id.number_pad);
        for (int i = 0; i < numberPad.getChildCount(); i++) {
            View child = numberPad.getChildAt(i);
            if (child instanceof Button) {
                final Button button = (Button) child;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String buttonText = button.getText().toString();
                        if (buttonText.equals("Delete")) {
                            if (pinBuilder.length() > 0) {
                                pinBuilder.deleteCharAt(pinBuilder.length() - 1);
                                pinInputOrigin.setText(pinBuilder.toString());
                            }
                        } else {
                            if (pinBuilder.length() < 4) {
                                pinBuilder.append(buttonText);
                                pinInputOrigin.setText(pinBuilder.toString());
                            }
                        }
                    }
                });
            }
        }

        Button submitButton = findViewById(R.id.button_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pin = pinInputOrigin.getText().toString();
                if (validatePin(pin)) {
                    String curPin = sharedPreferencesManager.getString("cur_pincode", "");
                    if(curPin.equals(pin)) {
                        sharedPreferencesManager.putBoolean("cred_flag", false);
                        finish();
                    } else {
                        showToast("Incorrect Pin Code!!!");
                    }
                } else {
                    // Show error message
                    showToast("Invalid Input!!!");
                }
            }
        });

        toolbar();
        setTitle("Confirm Your PinCode");
    }

    private boolean validatePin(String pin) {
        // Add your PIN validation logic here
        // For example, check if the PIN matches a pre-defined value
        return !TextUtils.isEmpty(pin) && pin.length() == 4;
    }

    public void showToast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}

