package com.reading_app.bookboogie;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        startLoading();
    }

    public void startLoading(){

        Handler splash_handler = new Handler();
        splash_handler.postDelayed(new Runnable() {     // 뒤에 밀리세컨즈만큼 기다렸다 실행하는 메소드.
            @Override
            public void run() {
                finish();
            }
        }, 1000);

    }

}
