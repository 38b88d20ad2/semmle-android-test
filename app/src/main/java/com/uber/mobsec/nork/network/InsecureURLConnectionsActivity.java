package com.uber.mobsec.nork.network;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.uber.mobsec.nork.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

public class InsecureURLConnectionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insecure_urlconnections);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                String urlString = System.getProperty("url", "https://google.com");
                URL url = null;
                try {
                    url = new URL(urlString);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return;
                }
                URLConnection urlConnection = null;
                try {
                    urlConnection = url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                HttpsURLConnection httpsUrlConnection = (HttpsURLConnection) urlConnection;
                SSLSocketFactory sslSocketFactory = null;
                try {
                    sslSocketFactory = new InsecureSSLSocketFactory();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    return;
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                    return;
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                    return;
                } catch (UnrecoverableKeyException e) {
                    e.printStackTrace();
                    return;
                }
                // VULN: this socketfactory is insecure
                httpsUrlConnection.setSSLSocketFactory(sslSocketFactory);
                // VULN: this hostnameverfier is insecure
                httpsUrlConnection.setHostnameVerifier(new InsecureHostnameVerifier());
                try {
                    try (InputStream inputStream = httpsUrlConnection.getInputStream()) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            System.out.println(line);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        });
    }

}
