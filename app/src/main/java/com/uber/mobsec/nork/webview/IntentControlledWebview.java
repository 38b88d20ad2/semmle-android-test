package com.uber.mobsec.nork.webview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.uber.mobsec.nork.R;

/**
 * Covers https://wiki.sei.cmu.edu/confluence/display/android/DRD02-J.+Do+not+allow+WebView+to+access+sensitive+local+resource+through+file+scheme
 *
 * tl;dr -  A malicious application may create and store a crafted content on its local storage area,
 *          make it accessible with MODE_WORLD_READABLE permission, and send the URI
 *          (using the file: scheme) of this content to a target activity
 *
 * To exploit:
 * String pkg = "com.uber.mobsec.nork";
 * String cls = pkg + ".webview.DummyLauncherActivity";
 * String uri = "file:///[crafted HTML file]";
 * Intent intent = new Intent();
 * intent.setClassName(pkg, cls);
 * intent.putExtra("URL", uri);
 * this.startActivity(intent);
 */
public class IntentControlledWebview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_controlled_webview);

        WebView webView = (WebView) findViewById(R.id.webview);


        // turn on javascript
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);


        // NOTE: setAllowFileAccess defaults to true

        String turl = getIntent().getStringExtra("URL");
        webView.loadUrl(turl);
    }

}
