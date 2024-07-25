package com.alphawallet.app.ui;


import static androidx.core.content.ContentProviderCompat.requireContext;

import static com.alphawallet.app.C.Key.WALLET;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
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
public class SetPassword extends BaseActivity implements StandardFunctionInterface
{
    private HomeViewModel viewModel;
    private SharedPreferencesManager sharedPreferencesManager;
    private EditText originPasswordInput, newPasswordInput, confirmPasswordInput;
    private MaterialButton passwordBtn;
    private FunctionButtonBar functionBar;
    private InputView inputName;
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
        setContentView(R.layout.set_password);

        originPasswordInput = findViewById(R.id.origin_password_input);
        newPasswordInput = findViewById(R.id.new_password_input);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);
        passwordBtn = findViewById(R.id.password_btn);
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this);

        String curPassword = sharedPreferencesManager.getString("cur_password", "");
        if(curPassword.isEmpty()) {
            originPasswordInput.setVisibility(View.GONE);
        }

        passwordBtn.setOnClickListener(v -> {
            if(String.valueOf(originPasswordInput.getText()).equals(curPassword)) {
                if(String.valueOf(newPasswordInput.getText()).equals(String.valueOf(confirmPasswordInput.getText()))) {
                    sharedPreferencesManager.putString("cur_password", String.valueOf(newPasswordInput.getText()));
                    sharedPreferencesManager.putInt("pass_kind", 0);
                    sharedPreferencesManager.putString("cur_pincode", "");
                    sharedPreferencesManager.putBoolean("passcode", !String.valueOf(newPasswordInput.getText()).isEmpty());
                    showToast("Password: " + newPasswordInput.getText());
                } else {
                    showToast("Incorrect confirm password!!!");
                }
            } else {
                showToast("Incorrect Password!!!");
            }
        });

        toolbar();

        setTitle(getString(R.string.security));
    }

    @Override
    protected void onResume()
    {
        super.onResume();
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

