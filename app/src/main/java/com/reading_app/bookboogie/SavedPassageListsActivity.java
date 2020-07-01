package com.reading_app.bookboogie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SavedPassageListsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_passage_list);

        ImageButton backBtn = findViewById(R.id.backBtn);
        ImageButton add_passage_btn = findViewById(R.id.add_passage_btn);

        RecyclerView recyclerView = findViewById(R.id.recyclerview_passages);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        //TODO 어댑터 만들고 지정하기

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        add_passage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChoicePassageImgActivity.class);
                startActivity(intent);
            }
        });

    }

}
