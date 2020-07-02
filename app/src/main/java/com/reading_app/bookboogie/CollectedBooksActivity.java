package com.reading_app.bookboogie;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class CollectedBooksActivity extends AppCompatActivity {

    // 어레이리스트를 쉐어드 프리퍼런스에 저장할 떄, key로 사용할 문자열 변수.
    // 쉐어드 프리퍼런스 불러올때 key로 사용할 문자열 변수.
    // 읽고 싶은 책
    private static final String WANT_READ_BOOKS = "want_read_books";
    // 읽은 책
    private static final String READ_BOOKS = "read_books";

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

    RecyclerView books_recyclerview;
    Button month_pick_btn;


    // 읽은 월 추가 버튼을 눌러서 확인 버튼을 누르면 이 리스너가 실행됨.
    // 다이얼로그에서 선택된 년도, 월 값 받아서 버튼에 세팅.
    DatePickerDialog.OnDateSetListener month_picker_listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            read_year = year;
            read_month = month;
            setRecyclerview(books, read_year, read_month);

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
        month_pick_btn = findViewById(R.id.month_pick_btn);

        // 메인에서 값 받아서 book_type가 0이면 읽은 책들에 저장되어있는 책을 보여주고
        // 1아면 읽고 싶은 책들에 저장되어 있는 책을 보여줌.
        Intent book_type_intent = getIntent();
        book_type = book_type_intent.getIntExtra("book_type", 0);

        if(book_type == 0){     // 읽은 책들 불러오기

            book_type_textview.setText("읽은 책들");

            books = getStringArrayPref(READ_BOOKS, "read_book");

//            // 쉐어드 프리퍼런스 열기.
//            SharedPreferences book_sharedpreference = getSharedPreferences("book_data", MODE_PRIVATE);
//            Map<String, ?> prefsMap = book_sharedpreference.getAll();
//            for(Map.Entry<String, ?> entry : prefsMap.entrySet()){
//                Gson gson = new Gson();
//                String json =  entry.getValue().toString();
//                books.add(gson.fromJson(json, Book.class));
//
//            }

//            Collection<?> collection = book_sharedpreference.getAll().values();
//            Iterator<?> iter = collection.iterator();
//
//            while(iter.hasNext()){
//                Gson gson = new Gson();
//                String json = (String)iter.next();
//                books.add(gson.fromJson(json, Book.class));
//
//                Log.d("isSearched_check", String.valueOf(gson.fromJson(json, Book.class).isSearchedBook));
//            }

        } else if(book_type == 1){      // 읽고 싶은 책들 불러오기

            // 읽고 싶은 책들에서는 책의 읽은 년도, 월 필요 없기때문에 버튼 사라지게 함.
            month_pick_btn.setVisibility(View.GONE);

            book_type_textview.setText("읽고 싶은 책들");

//            // 쉐어드 프리퍼런스 열기.
//            SharedPreferences book_sharedpreference = getSharedPreferences("wanted_book_data", MODE_PRIVATE);
//            Collection<?> collection = book_sharedpreference.getAll().values();
//            Iterator<?> iter = collection.iterator();
//
//            while(iter.hasNext()){
//                Gson gson = new Gson();
//                String json = (String)iter.next();
//                books.add(gson.fromJson(json, Book.class));
//
//                Log.d("isSearched_check", String.valueOf(gson.fromJson(json, Book.class).isSearchedBook));
//            }

            //////////////////////////////////////////////
            books = getStringArrayPref(WANT_READ_BOOKS, "want_book");


        }

        // 쉐어드 프리퍼런스 저장되어있는거 객체로 바꾸는 코드가 아직 불안정해서 다른 코드 주석처리 해 놓음
//        Map<String, ?> allEntries = book_sharedpreference.getAll();
//        for(Map.Entry<String, ?> entry : allEntries.entrySet()){
//            Gson gson = new Gson();
//            String json = entry.getString();
//            final String key = entry.getKey();
//            books.add(entry.getValue(key));
//        }

        books_recyclerview = findViewById(R.id.recyclerview_books);
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
//                setRecyclerview(books, read_year, read_month);
            }
        });

    }

    public void onResume() {
        super.onResume();
        Log.i("collect~~~~", "onResume");

    }

    // 여기서 받아온 isbn 정보로 검색 결과 나타내야 함.
    public void onRestart() {
        super.onRestart();
        Log.i("collect~~~~", "onRestart");

//        if(book_type == 0) {     // 읽은 책들 불러오기
//
//            books = getStringArrayPref(READ_BOOKS, "read_book");
//        }else{
//            books = getStringArrayPref(WANT_READ_BOOKS, "want_book");
//        }
//
//        // 리사이클러뷰 재배치
//        setRecyclerview();


    }

    public void setRecyclerview(){

            CollectedBooksAdapter recyclerview_book_adapter = new CollectedBooksAdapter(this, books, book_type);
            books_recyclerview.setAdapter(recyclerview_book_adapter);
            recyclerview_book_adapter.changeItem();

    }

    public void setRecyclerview(ArrayList<Book> books, int year, int month){

        // 선택된 달에 맞는 책들 넣을 어레이리스트
        ArrayList<Book> year_selected_books = new ArrayList<>();
        // 최종적으로 사용자가 선택한 년도와 월에 읽은 책들이 여기 들어감.
        ArrayList<Book> month_selected_books = new ArrayList<>();

        if(year == 0){
            month_pick_btn.setText("전체보기");
            CollectedBooksAdapter recyclerview_book_adapter = new CollectedBooksAdapter(this, books, book_type);
            books_recyclerview.setAdapter(recyclerview_book_adapter);
            recyclerview_book_adapter.changeItem();
        } else {

            month_pick_btn.setText(String.valueOf(year) + "." + String.valueOf(month));


            // 1차로 사용자가 선택한 년도와 같은 년도에 읽은 책들 골라냄
            for(int i = 0; i < books.size(); i++){
                if(books.get(i).read_year == year) {
                    year_selected_books.add(books.get(i));
                }
            }

            // 마지막으로 읽은 월까지 같은 책들 골라내서 어댑터에 인자로 넘겨준다.
            for(int i = 0; i < year_selected_books.size(); i++){
                if(year_selected_books.get(i).read_month == month){
                    month_selected_books.add(year_selected_books.get(i));
                }
            }

            CollectedBooksAdapter recyclerview_book_adapter = new CollectedBooksAdapter(this, month_selected_books, book_type);
            books_recyclerview.setAdapter(recyclerview_book_adapter);
            recyclerview_book_adapter.changeItem();

        }

    }


    // 어레이리스트를 쉐어드 프리퍼런스에 저장하는 메소드.
    private void setStringArrayPref(String sp_name, String key, ArrayList<Book> values) {

        SharedPreferences prefs = getSharedPreferences(sp_name, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(values);

        editor.putString(key, json);
        editor.commit();

    }



    // 제이슨을 어레이리스트로 변환하는 메소드
    private ArrayList<Book> getStringArrayPref(String sp_name, String key) {

        ArrayList<Book> urls = new ArrayList<>();

        SharedPreferences prefs = getSharedPreferences(sp_name, MODE_PRIVATE);

        // 어레이 문자열로 바꾼거 저장됨
        String arr_to_string = prefs.getString(key, "default");

        Type listtype = new TypeToken<ArrayList<Book>>(){}.getType();

        Gson gson = new Gson();
        try{
            urls = gson.fromJson(arr_to_string, listtype);
        } catch (IllegalStateException |  JsonSyntaxException e) {
            e.printStackTrace();
        }


        return urls;
    }


}
