package com.alphawallet.app.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.alphawallet.app.R;

public class CustomBrowser extends BaseActivity {
    private WebView webView;
    private EditText urlInput;
    private ProgressBar progressBar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_custom_browser);
        // Initialize views
        webView = findViewById(R.id.webView);
        urlInput = findViewById(R.id.urlInput);
        progressBar = findViewById(R.id.progressBar);
        urlInput.setText(getIntent().getStringExtra("url"));
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                // Show the ProgressBar when the page starts loading
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // Hide the ProgressBar when the page finishes loading
                progressBar.setVisibility(View.GONE);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                // Make sure the webview becomes visible as soon as loading starts
                if (newProgress < 100) {
                    webView.setVisibility(View.VISIBLE);
                }
            }
        });
        String url = urlInput.getText().toString().trim();
        toolbar();
        setTitle("Browser");
        webView.loadUrl(url);
//        loadButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String url = urlInput.getText().toString().trim();
//                if (!url.isEmpty()) {
//                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
//                        url = "http://" + url; // Ensure proper URL format
//                    }
//                    webView.loadUrl(url);
//                } else {
//                    Toast.makeText(CustomBrowser.this, "Please enter a URL", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }
}

