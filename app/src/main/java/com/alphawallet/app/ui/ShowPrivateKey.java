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
import android.widget.TextView;

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
public class ShowPrivateKey extends BaseActivity implements StandardFunctionInterface
{
    private HomeViewModel viewModel;

    private String currentWalletAddress, privateKey;
    private SharedPreferencesManager sharedPreferencesManager;

    private FunctionButtonBar functionBar;
    private InputView inputName;

    private Wallet wallet;

    @Nullable
    private Disposable disposable;
    private TextView privateKeyLabel;

    private MaterialButton copyBtn;

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
        currentWalletAddress = viewModel.getWalletFullName(getBaseContext());

        sharedPreferencesManager = SharedPreferencesManager.getInstance(getBaseContext());
        privateKey = sharedPreferencesManager.getString(currentWalletAddress, "default");

        setContentView(R.layout.activity_show_private_key);

        privateKeyLabel = findViewById(R.id.privatekey_textView);
        privateKeyLabel.setText(privateKey);

        copyBtn = findViewById(R.id.copyPrivateKeyButton);

        copyBtn.setOnClickListener(v -> {
            copyBtn.setText(R.string.copied_to_clipboard);
            Handler handler1 = new Handler();
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("label", privateKey);
            clipboardManager.setPrimaryClip(clipData);
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    copyBtn.setText(R.string.copy_private_key);
                }
            }, 2000);
        });

        toolbar();

        setTitle(getString(R.string.show_private_key_title));
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

        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();

        disposable = null;
    }
}
