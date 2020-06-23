package com.reading_app.bookboogie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.reading_app.bookboogie.unused.OldBookSearchActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyMainActivity";

    // setContentView 전에 view들 사용 못함.................!!
//    Button read_books_btn = findViewById(R.id.read_books_btn);
//    Button want_read_btn = findViewById(R.id.want_read_btn);
//    Button saved_passage_btn = findViewById(R.id.saved_passage_btn);
//    Button calender_btn = findViewById(R.id.calender_btn);
//    Button dictionary_btn = findViewById(R.id.dictionary_btn);
//    Button profile_btn = findViewById(R.id.profile_btn);

    // 검색했을때 파싱해온 책들이 들어갈 어레이리스트트
    ArrayList<Book> searched_books;

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

        // 이 서치뷰에서 책을 검색하면 책 정보가 뜨고 책 정보를 가져와서 저장할 수 있음.
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

//        book_search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//
//            // 검색 버튼이 눌러졌을때 이벤트 처리
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                Toast.makeText(MainActivity.this, "검색 처리됨 : " + query, Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "검색 버튼 눌려지는 코드 확인");
//                String result = searchBook(query);
//
//
//                return true;
//            }
//
//            // 검색어 글자가 바뀔때마다 이벤트 처리
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
////                Toast.makeText(MainActivity.this, "검색 처리됨 : " + newText, Toast.LENGTH_SHORT).show();
////                searchBook(newText);
//
//                return false;
//            }
//        });

    }




}
