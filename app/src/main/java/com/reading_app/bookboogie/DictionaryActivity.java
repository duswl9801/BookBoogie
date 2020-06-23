package com.reading_app.bookboogie;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class DictionaryActivity extends AppCompatActivity {

    private WebView dictionaryWebView;
    private String dictionaryUrl = "https://dict.naver.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        ImageButton backBtn = findViewById(R.id.backBtn);
        dictionaryWebView = (WebView) findViewById(R.id.dictionaryWebView);
        dictionaryWebView.getSettings().setJavaScriptEnabled(true);

        dictionaryWebView.loadUrl(dictionaryUrl);
        dictionaryWebView.setWebChromeClient(new WebChromeClient());
        dictionaryWebView.setWebViewClient(new WebViewClientClass());

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
                onBackPressed();
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && dictionaryWebView.canGoBack()) {
            dictionaryWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("check URL", url);
            view.loadUrl(url);
            return true;
        }


    }
}
