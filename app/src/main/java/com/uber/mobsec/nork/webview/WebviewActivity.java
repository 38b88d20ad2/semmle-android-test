package com.uber.mobsec.nork.webview;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.uber.mobsec.nork.R;

/**
 * Covers:
 * - CVE-2013-4710
 * - Ignores TLS Cert errors
 */
public class WebviewActivity extends AppCompatActivity {

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        WebView myWebView = (WebView) findViewById(R.id.webview);

        //
        myWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //Ignore TLS certificate errors and instruct the WebViewClient to load the website
                handler.proceed();
            }
        });

        // not a good idea!
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // terrible idea!
        myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");

        // has
        myWebView.loadUrl("http://www.droidsec.org/tests/addjsif/");
    }
}
