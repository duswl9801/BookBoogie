package com.reading_app.bookboogie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class CalenderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        ImageButton backBtn = findViewById(R.id.backBtn);
        ImageButton calenderBtn = findViewById(R.id.calenderBtn);
        ImageButton chartBtn = findViewById(R.id.chartBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
                onBackPressed();
            }
        });


    }

}
