package com.alphawallet.app.ui;
import static androidx.core.content.ContentProviderCompat.requireContext;

import static com.alphawallet.app.C.Key.WALLET;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
public class AutoLockActivity extends BaseActivity implements StandardFunctionInterface
{
    private HomeViewModel viewModel;
    private SharedPreferencesManager sharedPreferencesManager;

    private LinearLayout mainLayout;
    private Switch oneSwitch, fiveSwitch, fifteenSwitch, thirtySwitch, oneHourSwitch;
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
        setContentView(R.layout.autolock_activity);

        mainLayout = findViewById(R.id.root_autolock_layout);
        oneSwitch = findViewById(R.id.one_switch);
        fiveSwitch = findViewById(R.id.five_switch);
        fifteenSwitch = findViewById(R.id.fifteen_switch);
        thirtySwitch = findViewById(R.id.thirty_switch);
        oneHourSwitch = findViewById(R.id.one_hour_switch);

        oneSwitch.setChecked(true);
        sharedPreferencesManager.putInt("autolock_time", 0);
        setSwitchListeners();
        toolbar();

        setTitle("Set Auto-Locking time");
    }

    private void toggleSwitch(int index) {

    }

    private void setSwitchListeners() {
        oneSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                sharedPreferencesManager.putInt("autolock_time", 15000);
                uncheckOtherSwitches(oneSwitch);
                restartApp();
            }
        });

        fiveSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                sharedPreferencesManager.putInt("autolock_time", 60 * 1000);
                uncheckOtherSwitches(fiveSwitch);
                restartApp();
            }
        });

        fifteenSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                sharedPreferencesManager.putInt("autolock_time", 3 * 60 * 1000);
                uncheckOtherSwitches(fifteenSwitch);
                restartApp();
            }
        });

        thirtySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                sharedPreferencesManager.putInt("autolock_time", 6 * 60 * 1000);
                uncheckOtherSwitches(thirtySwitch);
                restartApp();
            }
        });

        oneHourSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                sharedPreferencesManager.putInt("autolock_time", 15 * 60 * 1000);
                uncheckOtherSwitches(oneHourSwitch);
                restartApp();
            }
        });
    }

    private void uncheckOtherSwitches(Switch checkedSwitch) {
        if (checkedSwitch != oneSwitch && oneSwitch.isChecked()) {
            oneSwitch.setChecked(false);
        }
        if (checkedSwitch != fiveSwitch && fiveSwitch.isChecked()) {
            fiveSwitch.setChecked(false);
        }
        if (checkedSwitch != fifteenSwitch && fifteenSwitch.isChecked()) {
            fifteenSwitch.setChecked(false);
        }
        if (checkedSwitch != thirtySwitch && thirtySwitch.isChecked()) {
            thirtySwitch.setChecked(false);
        }
        if (checkedSwitch != oneHourSwitch && oneHourSwitch.isChecked()) {
            oneHourSwitch.setChecked(false);
        }
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

    public void restartApp() {
        showToast("Restart your app!!!");
    }
}

