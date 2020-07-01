package com.reading_app.bookboogie;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class CollectedBooksActivity extends AppCompatActivity {

    // 옆에 생성자 안붙여서 add할 때 에러났었음.
    ArrayList<Book> books = new ArrayList<>();

    /*
    책이 읽은 책인지 읽고 싶은 책인지 알기 위한 변수.
    메인에서 인텐트로 값 받아온다.
    0이 읽은 책, 1이 읽고 싶은 책이다.
    기본 값은 0.
     */
    int book_type = 0;

    int read_year = 0, read_month = 0;


    // 읽은 월 추가 버튼을 눌러서 확인 버튼을 누르면 이 리스너가 실행됨.
    // 다이얼로그에서 선택된 년도, 월 값 받아서 버튼에 세팅.
    DatePickerDialog.OnDateSetListener month_picker_listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            read_year = year;
            read_month = month;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collected_books);

        // xml파일의 속성들 객체화
        ImageButton back_btn = findViewById(R.id.back_btn);
        TextView book_type_textview = findViewById(R.id.book_type);
        ImageButton add_book_btn = findViewById(R.id.add_book_btn);
        Button month_pick_btn = findViewById(R.id.month_pick_btn);

        // 메인에서 값 받아서 book_type가 0이면 읽은 책들에 저장되어있는 책을 보여주고
        // 1아면 읽고 싶은 책들에 저장되어 있는 책을 보여줌.
        Intent book_type_intent = getIntent();
        book_type = book_type_intent.getIntExtra("book_type", 0);

        if(book_type == 0){     // 읽은 책들 불러오기

            book_type_textview.setText("읽은 책들");

            // 쉐어드 프리퍼런스 열기.
            SharedPreferences book_sharedpreference = getSharedPreferences("book_data", MODE_PRIVATE);
            Collection<?> collection = book_sharedpreference.getAll().values();
            Iterator<?> iter = collection.iterator();

            while(iter.hasNext()){
                Gson gson = new Gson();
                String json = (String)iter.next();
                books.add(gson.fromJson(json, Book.class));

                Log.d("isSearched_check", String.valueOf(gson.fromJson(json, Book.class).isSearchedBook));
            }

        } else if(book_type == 1){      // 읽고 싶은 책들 불러오기

            // 읽고 싶은 책들에서는 책의 읽은 년도, 월 필요 없기때문에 버튼 사라지게 함.
            month_pick_btn.setVisibility(View.GONE);

            book_type_textview.setText("읽고 싶은 책들");

            // 쉐어드 프리퍼런스 열기.
            SharedPreferences book_sharedpreference = getSharedPreferences("wanted_book_data", MODE_PRIVATE);
            Collection<?> collection = book_sharedpreference.getAll().values();
            Iterator<?> iter = collection.iterator();

            while(iter.hasNext()){
                Gson gson = new Gson();
                String json = (String)iter.next();
                books.add(gson.fromJson(json, Book.class));

                Log.d("isSearched_check", String.valueOf(gson.fromJson(json, Book.class).isSearchedBook));
            }


        }

        // 쉐어드 프리퍼런스 저장되어있는거 객체로 바꾸는 코드가 아직 불안정해서 다른 코드 주석처리 해 놓음
//        Map<String, ?> allEntries = book_sharedpreference.getAll();
//        for(Map.Entry<String, ?> entry : allEntries.entrySet()){
//            Gson gson = new Gson();
//            String json = entry.getString();
//            final String key = entry.getKey();
//            books.add(entry.getValue(key));
//        }

        RecyclerView books_recyclerview = findViewById(R.id.recyclerview_books);
        books_recyclerview.setLayoutManager(new GridLayoutManager(this, 3));
        books_recyclerview.addItemDecoration(new DividerItemDecoration(books_recyclerview.getContext(), 1));

        final CollectedBooksAdapter recyclerview_book_adapter = new CollectedBooksAdapter(this, books, book_type);
        books_recyclerview.setAdapter(recyclerview_book_adapter);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        add_book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(book_type == 0){     // 읽은 책 추가 액티비티로 이동.
                    Intent intent = new Intent(getApplicationContext(), AddReadBookActivity.class);
                    intent.putExtra("Book", new Book());
                    startActivity(intent);
//                    startActivityForResult(intent, REQUEST_ADD_BOOK);

                }else{      // 읽고 싶은 책 추가 액티비티로 이동.
                    Intent intent = new Intent(getApplicationContext(), AddWantReadBookActivity.class);
                    intent.putExtra("Book", new Book());
                    startActivity(intent);
                }

            }
        });

        month_pick_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YearMonthPickerContainAllDialog year_month_pick_dialog = new YearMonthPickerContainAllDialog();
                year_month_pick_dialog.setListener(month_picker_listener);
                year_month_pick_dialog.show(getSupportFragmentManager(), "YearMonthPicker");
            }
        });

    }




}
