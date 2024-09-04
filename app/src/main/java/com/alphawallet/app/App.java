package com.alphawallet.app;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

import android.app.Activity;
import android.app.Application;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.preference.PreferenceManager;

import com.alphawallet.app.ui.CredentialActivity;
import com.alphawallet.app.ui.PinCodeConfirmActivity;
import com.alphawallet.app.util.TimberInit;
import com.alphawallet.app.walletconnect.AWWalletConnectClient;

import java.util.EmptyStackException;
import java.util.Stack;

import javax.inject.Inject;

import dagger.hilt.android.HiltAndroidApp;
import io.reactivex.plugins.RxJavaPlugins;
import io.realm.Realm;
import timber.log.Timber;

@HiltAndroidApp
public class App extends Application
{
    @Inject
    AWWalletConnectClient awWalletConnectClient;
    SharedPreferencesManager sharedPreferencesManager;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable inactivityRunnable;

    private String curActivtyName;
    private long lastActivityTime;

    private static App mInstance;
    private final Stack<Activity> activityStack = new Stack<>();

    public static App getInstance()
    {
        return mInstance;
    }

    public Activity getTopActivity()
    {
        try
        {
            return activityStack.peek();
        }
        catch (EmptyStackException e)
        {
            //
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate()
    {
        super.onCreate();
        mInstance = this;
        Realm.init(this);
        TimberInit.configTimber();

        sharedPreferencesManager = SharedPreferencesManager.getInstance(this);

        int defaultTheme = PreferenceManager.getDefaultSharedPreferences(this)
                .getInt("theme", C.THEME_AUTO);

        if (defaultTheme == C.THEME_LIGHT)
        {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO);
        }
        else if (defaultTheme == C.THEME_DARK)
        {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
        }
        else
        {
            UiModeManager uiModeManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
            int mode = uiModeManager.getNightMode();
            if (mode == UiModeManager.MODE_NIGHT_YES)
            {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
            }
            else if (mode == UiModeManager.MODE_NIGHT_NO)
            {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO);
            }
        }

        RxJavaPlugins.setErrorHandler(Timber::e);

        try
        {
            awWalletConnectClient.init(this);
        }
        catch (Exception e)
        {
            Timber.tag("WalletConnect").e(e);
        }

        //int autolock_time = sharedPreferencesManager.getInt("autolock_time", 0);
        int autolock_time = sharedPreferencesManager.getInt("autolock_time", 0);
        inactivityRunnable = new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                if(autolock_time == 0) {
                    return;
                }
                if (currentTime - lastActivityTime > sharedPreferencesManager.getInt("autolock_time", 0)) {
                    // User has been inactive for longer than the threshold
                    handleInactivity();
                }
                handler.postDelayed(this, sharedPreferencesManager.getInt("autolock_time", 0)); // Check periodically
            }
        };

        handler.postDelayed(inactivityRunnable, sharedPreferencesManager.getInt("autolock_time", 0));
    }

    public void updateLastActivityTime() {
        lastActivityTime = System.currentTimeMillis();
    }

    private void handleInactivity() {

    }
    public void showToast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
