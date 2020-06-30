package com.reading_app.bookboogie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.reading_app.bookboogie.unused.ReadBooksCategoriesActivity;
import com.reading_app.bookboogie.unused.WantReadCategoriesActivity;

public class AnotherMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_main);

        // 스플래시 불러오기
        Intent splash_intent = new Intent(this, SplashActivity.class);
        startActivity(splash_intent);

        ConstraintLayout read_books_container = findViewById(R.id.another_read_books_container);
        ConstraintLayout want_read_container = findViewById(R.id.another_want_read_container);
        ConstraintLayout saved_passage_container = findViewById(R.id.another_saved_passage_container);
        ConstraintLayout calender_container = findViewById(R.id.another_calender_container);
        ConstraintLayout dictionary_container = findViewById(R.id.another_dictionary_container);
        ConstraintLayout change_theme_container = findViewById(R.id.another_change_theme_container);

//        // 이 서치뷰에서 책을 검색하면 책 정보가 뜨고 책 정보를 가져와서 저장할 수 있음.
//        SearchView book_search_view = findViewById(R.id.book_searchview);
//        book_search_view.setQueryHint("책 검색하기");
        ImageButton book_search_btn = findViewById(R.id.book_search_btn);


        // 책의 바코드(isbn)을 찍어서 정보를 가져올 수 있게하는 카메라 버튼.
        ImageButton camera_btn = findViewById(R.id.camera_btn);

        // 사용자가 원하는 기능의 화면 클릭했을 때, 해당 액티비티로 이동
        read_books_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReadBooksCategoriesActivity.class);
                startActivity(intent);
            }
        });

        want_read_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WantReadCategoriesActivity.class);
                startActivity(intent);
            }
        });

        saved_passage_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SavedPassageListsActivity.class);
                startActivity(intent);
            }
        });

        calender_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CalenderActivity.class);
                startActivity(intent);
            }
        });

        dictionary_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DictionaryActivity.class);
                startActivity(intent);
            }
        });

        change_theme_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ISBNScanActivity.class);
                intent.putExtra("MainKind", "AnotherMain");
                startActivity(intent);
            }
        });

        book_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BookSearchActivity.class);
                startActivity(intent);
            }
        });

    }

}
