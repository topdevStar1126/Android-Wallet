package com.alphawallet.app.ui;


import static com.alphawallet.app.C.IMPORT_REQUEST_CODE;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.app.KeyguardManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.alphawallet.app.R;
import com.alphawallet.app.SharedPreferencesManager;
import com.alphawallet.app.analytics.Analytics;
import com.alphawallet.app.entity.AnalyticsProperties;
import com.alphawallet.app.entity.CreateWalletCallbackInterface;
import com.alphawallet.app.entity.CustomViewSettings;
import com.alphawallet.app.entity.Operation;
import com.alphawallet.app.entity.Wallet;
import com.alphawallet.app.entity.analytics.FirstWalletAction;
import com.alphawallet.app.router.HomeRouter;
import com.alphawallet.app.router.ImportWalletRouter;
import com.alphawallet.app.service.KeyService;
import com.alphawallet.app.service.TokensService;
import com.alphawallet.app.util.RootUtil;
import com.alphawallet.app.viewmodel.SplashViewModel;
import com.alphawallet.app.widget.AWalletAlertDialog;
import com.alphawallet.app.widget.PasswordInputView;
import com.alphawallet.app.widget.SignTransactionDialog;
import com.google.android.material.button.MaterialButton;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class CredentialActivity extends BaseActivity
{

    private EditText passwordInputView;
    private MaterialButton psswrdBtn;
    private TokensService tokensService;

    SharedPreferencesManager sharedPreferencesManager;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credential_activiy);
        passwordInputView = findViewById(R.id.password_view);
        psswrdBtn = findViewById(R.id.password_btn);

        sharedPreferencesManager = SharedPreferencesManager.getInstance(this);

        String curPassword = sharedPreferencesManager.getString("cur_password", "");
        psswrdBtn.setOnClickListener(v -> {
           if(String.valueOf(passwordInputView.getText()).equals(curPassword)) {
               sharedPreferencesManager.putBoolean("cred_flag", false);
               finish();
           } else {
               showToast("Incorrect Password!!!");
           }
        });
    }

    protected Activity getThisActivity()
    {
        return this;
    }

    //wallet created, now check if we need to import

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        handler = null;
    }

    public void showToast(CharSequence msg) {
        Toast.makeText(getThisActivity(), msg, Toast.LENGTH_LONG).show();
    }

}

