package com.alphawallet.app.ui;


import static androidx.core.content.ContentProviderCompat.requireContext;

import static com.alphawallet.app.C.Key.WALLET;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alphawallet.app.R;
import com.alphawallet.app.SharedPreferencesManager;
import com.alphawallet.app.analytics.Analytics;
import com.alphawallet.app.entity.StandardFunctionInterface;
import com.alphawallet.app.entity.Wallet;
import com.alphawallet.app.entity.WalletType;
import com.alphawallet.app.viewmodel.HomeViewModel;
import com.alphawallet.app.viewmodel.NameThisWalletViewModel;
import com.alphawallet.app.widget.FunctionButtonBar;
import com.alphawallet.app.widget.InputView;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Collections;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.disposables.Disposable;

@AndroidEntryPoint
public class SecurityActivity extends BaseActivity implements StandardFunctionInterface
{
    private HomeViewModel viewModel;
    private SharedPreferencesManager sharedPreferencesManager;
    private TextView setPassword, autoLock, setPinCode;
    private Switch passcodeSwitch;
    private Wallet wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this)
                .get(HomeViewModel.class);

        viewModel.identify();
        viewModel.setWalletStartup();
        viewModel.setCurrencyAndLocale(getBaseContext());
        viewModel.tryToShowWhatsNewDialog(getBaseContext());
        wallet = getIntent().getParcelableExtra(WALLET);
        sharedPreferencesManager = SharedPreferencesManager.getInstance(getBaseContext());
        setContentView(R.layout.security_layout);

        setPassword = findViewById(R.id.set_password);
        autoLock = findViewById(R.id.auto_lock);
        passcodeSwitch = findViewById(R.id.passcode_switch);
        setPinCode = findViewById(R.id.set_pincode);

        setPassword.setOnClickListener(v -> {
            Intent intent = new Intent(this, SetPassword.class);
            this.startActivity(intent);
        });

        setPinCode.setOnClickListener(v -> {
            Intent intent = new Intent(this, PinCodeSetActivity.class);
            this.startActivity(intent);
        });

        boolean passCode = sharedPreferencesManager.getBoolean("passcode", false);
        passcodeSwitch.setChecked(passCode);
        passcodeSwitch.setOnCheckedChangeListener((v, isChecked) -> {
            if(isChecked) {
                sharedPreferencesManager.putBoolean("passcode", true);
            } else {
                sharedPreferencesManager.putBoolean("passcode", false);
            }
        });

        autoLock.setOnClickListener(v -> {
            Intent intent = new Intent(this, AutoLockActivity.class);
            this.startActivity(intent);
        });

        boolean passcode = sharedPreferencesManager.getBoolean("passcode", false);
        if(passcode) {
            if(sharedPreferencesManager.getInt("pass_kind", 10) == 0) {
                String curPassword = sharedPreferencesManager.getString("cur_password", "");
                if(!curPassword.isEmpty()){
                    Intent intent1 = new Intent(this, CredentialActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent1.putExtra("activity", 1);
                    sharedPreferencesManager.putBoolean("cred_flag", true);
                    startActivity(intent1);
                }
            } else if(sharedPreferencesManager.getInt("pass_kind", 10) == 1) {
                String curPin = sharedPreferencesManager.getString("cur_pincode", "");
                if(!curPin.isEmpty()){
                    Intent intent1 = new Intent(this, PinCodeConfirmActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent1.putExtra("activity", 1);
                    sharedPreferencesManager.putBoolean("cred_flag", true);
                    startActivity(intent1);
                }
            }
        }

        toolbar();
        setTitle(getString(R.string.security));
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        boolean passCode = sharedPreferencesManager.getBoolean("passcode", false);
        passcodeSwitch.setChecked(passCode);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    public void showToast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}

