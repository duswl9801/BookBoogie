package com.reading_app.bookboogie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyMainActivity";     // 로그 찍을 때 이용할 변수

    /* 메인에서 CollectedBooksActivity로 넘어갈때,
     사용자가 읽은 책들을 클릭하고 넘어갔는지 읽고 싶은 책들을 클릭하고 넘어갔는지 구분해주는 변수
    읽은 책들을 클릭하면 0을 넘겨주고 읽고 싶은 책들을 클릭하면 1을 넘겨준다.
     */
    private static final int READ_BOOKS = 0;
    private static final int WANT_READ_BOOKS = 1;

    // 읽은 책들, 읽고 싶은 책들, 저장한 문장들의 개수를 저장해 놓을 변수.
    int count = 0;

    // 저장된 책들과 문장들을 열때 사용할 변수.
    SharedPreferences prefs;

    TextView read_books_count;
    TextView want_books_count;
    TextView saved_passage_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 스플래시 불러오기
        Intent splash_intent = new Intent(this, SplashActivity.class);
        startActivity(splash_intent);

        ConstraintLayout read_books_container = findViewById(R.id.read_books_container);
        ConstraintLayout want_read_container = findViewById(R.id.want_read_container);
        ConstraintLayout saved_passage_container = findViewById(R.id.saved_passage_container);
        ConstraintLayout calender_container = findViewById(R.id.calender_container);
        ConstraintLayout dictionary_container = findViewById(R.id.dictionary_container);
        ConstraintLayout change_theme_container = findViewById(R.id.change_theme_container);

        read_books_count = findViewById(R.id.read_books_count);
        want_books_count = findViewById(R.id.want_read_books_count);
        saved_passage_count = findViewById(R.id.saved_passage_count);

        // 이 서치뷰에서 책을 검색하면 책 정보가 뜨고 책 정보를 가져와서 저장할 수 있음.
        ImageButton book_search_btn = findViewById(R.id.book_search_btn);

        // 책의 바코드(isbn)을 찍어서 정보를 가져올 수 있게하는 카메라 버튼.
        ImageButton camera_btn = findViewById(R.id.camera_btn);

        setCounts();

        // 사용자가 원하는 기능의 화면 클릭했을 때, 해당 액티비티로 이동
        read_books_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CollectedBooksActivity.class);
                intent.putExtra("book_type", READ_BOOKS);
                startActivity(intent);
            }
        });

        want_read_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CollectedBooksActivity.class);
                intent.putExtra("book_type", WANT_READ_BOOKS);
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
                Intent intent = new Intent(getApplicationContext(), AnotherMainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ISBNScanActivity.class);
                intent.putExtra("MainKind", "Main");
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

    public void setCounts(){

        // 읽은 책들 개수 세팅
        prefs = getSharedPreferences("book_data", MODE_PRIVATE);
        Map<String, ?> read_book_prefsMap = prefs.getAll();
        for(Map.Entry<String, ?> entrty : read_book_prefsMap.entrySet()){
            count++;
        }

        read_books_count.setText(String.valueOf(count));

        // 개수 초기화
        count = 0;

        // 읽고 싶은 책들 개수 세팅
        prefs = getSharedPreferences("wanted_book_data", MODE_PRIVATE);
        Map<String, ?> prefsMap = prefs.getAll();
        for(Map.Entry<String, ?> entrty : prefsMap.entrySet()){
            count++;
        }

        want_books_count.setText(String.valueOf(count));

        // 개수 초기화
        count = 0;

        // 저장된 문장들 개수 세팅


    }




}
