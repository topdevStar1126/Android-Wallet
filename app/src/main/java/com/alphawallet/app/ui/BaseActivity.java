package com.alphawallet.app.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.DrawableRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alphawallet.app.App;
import com.alphawallet.app.R;
import com.alphawallet.app.SharedPreferencesManager;
import com.alphawallet.app.entity.AuthenticationCallback;
import com.alphawallet.app.entity.AuthenticationFailType;
import com.alphawallet.app.entity.Operation;
import com.alphawallet.app.viewmodel.BaseViewModel;
import com.alphawallet.app.widget.AWalletAlertDialog;
import com.alphawallet.app.widget.SignTransactionDialog;

public abstract class BaseActivity extends AppCompatActivity
{
    public static AuthenticationCallback authCallback;

    SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager.getInstance(this);

    protected Toolbar toolbar()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null)
        {
            setSupportActionBar(toolbar);
            toolbar.setTitle(R.string.empty);
        }
        enableDisplayHomeAsUp();
        return toolbar;
    }

    private final Handler activityHandler = new Handler(Looper.getMainLooper());
    private final Runnable periodicUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            ((App) getApplication()).updateLastActivityTime();
            activityHandler.postDelayed(this, sharedPreferencesManager.getInt("autolock_time", 0) + 20000); // Update every 10 seconds
        }
    };

    protected void setTitle(String title)
    {
        ActionBar actionBar = getSupportActionBar();
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        if (toolbarTitle != null)
        {
            if (actionBar != null)
            {
                actionBar.setTitle(R.string.empty);
            }
            toolbarTitle.setText(title);
        }

        setDispatcher();
    }

    protected void setSubtitle(String subtitle)
    {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setSubtitle(subtitle);
        }
    }

    protected void enableDisplayHomeAsUp()
    {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void enableDisplayHomeAsUp(@DrawableRes int resourceId)
    {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(resourceId);
        }
    }

    protected void enableDisplayHomeAsHome(boolean active)
    {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(active);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_browser_home);
        }
    }

    protected void disableDisplayHomeAsUp()
    {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    protected void hideToolbar()
    {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.hide();
        }
    }

    protected void showToolbar()
    {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            handleBackPressed();
            finish();
        }
        return true;
    }

    public void displayToast(String message)
    {
        if (message != null)
        {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            BaseViewModel.onPushToast(null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (authCallback == null)
        {
            return;
        }

        if (requestCode >= SignTransactionDialog.REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS && requestCode <= SignTransactionDialog.REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS + 10)
        {
            Operation taskCode = Operation.values()[requestCode - SignTransactionDialog.REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS];
            if (resultCode == RESULT_OK)
            {
                authCallback.authenticatePass(taskCode);
            }
            else
            {
                authCallback.authenticateFail("", AuthenticationFailType.PIN_FAILED, taskCode);
            }

            authCallback = null;
        }
    }

    protected void displayErrorMessage(String message)
    {
        AWalletAlertDialog dialog = new AWalletAlertDialog(this);
        dialog.setTitle(R.string.title_dialog_error);
        dialog.setMessage(message);
        dialog.setButtonText(R.string.ok);
        dialog.setButtonListener(v -> dialog.dismiss());
        dialog.show();
    }

    public void setDispatcher()
    {
        final OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed()
            {
                handleBackPressed();
            }
        };

        this.getOnBackPressedDispatcher().addCallback(callback);
    }
    public void handleBackPressed()
    {
        finish();
    };

    @Override
    protected void onResume() {
        super.onResume();
        activityHandler.post(periodicUpdateRunnable);
    }
    @Override
    protected void onPause() {
        super.onPause();
        activityHandler.removeCallbacks(periodicUpdateRunnable);
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        ((App) getApplication()).updateLastActivityTime();
        return super.dispatchTouchEvent(event);
    }
}
