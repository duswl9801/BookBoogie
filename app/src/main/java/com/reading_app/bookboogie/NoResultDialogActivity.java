package com.reading_app.bookboogie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

// todo 뒤로가기 못하게 막기.
public class NoResultDialogActivity extends AppCompatActivity {

    String main_kind; // 어떤 메인인지 저장하는 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_no_result);

        Intent main = getIntent();
        main_kind = main.getStringExtra("MainKind");


        Button check_btn = findViewById(R.id.check_btn);
        Button cancle_btn = findViewById(R.id.cancle_btn);

        check_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoResultDialogActivity.this, ScanActivity.class);
                startActivity(intent);
                finish();
            }
        });

        cancle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(main_kind.equals("Main")){
                    Intent intent = new Intent(NoResultDialogActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if(main_kind.equals("AnotherMain")){
                    Intent intent = new Intent(NoResultDialogActivity.this, AnotherMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
