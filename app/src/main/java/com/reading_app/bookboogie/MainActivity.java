package com.reading_app.bookboogie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // 어레이리스트를 쉐어드 프리퍼런스에 저장할 떄, key로 사용할 문자열 변수.
    // 쉐어드 프리퍼런스 불러올때 key로 사용할 문자열 변수.
    // 읽고 싶은 책
    private static final String WANT_READ_BOOKS = "want_read_books";
    // 읽은 책
    private static final String READ_BOOKS = "read_books";

    private static final String TAG = "MyMainActivity";     // 로그 찍을 때 이용할 변수
    // 쉐어드 프리퍼런스 불러올때 key로 사용할 문자열 변수.
    private static final String HIGHLIGHTED_IMGS_ARRAYLIST = "highlighted_imgs";

    /* 메인에서 CollectedBooksActivity로 넘어갈때,
     사용자가 읽은 책들을 클릭하고 넘어갔는지 읽고 싶은 책들을 클릭하고 넘어갔는지 구분해주는 변수
    읽은 책들을 클릭하면 0을 넘겨주고 읽고 싶은 책들을 클릭하면 1을 넘겨준다.
     */
    private static final int READ_BOOKS_TYPE = 0;
    private static final int WANT_READ_BOOKS_TYPE = 1;

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
//        ConstraintLayout change_theme_container = findViewById(R.id.change_theme_container);

        read_books_count = findViewById(R.id.read_books_count);
        want_books_count = findViewById(R.id.want_read_books_count);
        saved_passage_count = findViewById(R.id.saved_passage_count);

        // 이 서치뷰에서 책을 검색하면 책 정보가 뜨고 책 정보를 가져와서 저장할 수 있음.
        ImageButton book_search_btn = findViewById(R.id.book_search_btn);

        // 책의 바코드(isbn)을 찍어서 정보를 가져올 수 있게하는 카메라 버튼.
        ImageButton camera_btn = findViewById(R.id.camera_btn);

        // 사용자가 원하는 기능의 화면 클릭했을 때, 해당 액티비티로 이동
        read_books_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CollectedBooksActivity.class);
                intent.putExtra("book_type", READ_BOOKS_TYPE);
                startActivity(intent);
            }
        });

        want_read_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CollectedBooksActivity.class);
                intent.putExtra("book_type", WANT_READ_BOOKS_TYPE);
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

//        change_theme_container.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), AnotherMainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });

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

    /*
     여기서 저장된 책들의 개수 세서 textview에 나타내야 함.
     책 추가하는 액티비티 갔다가 메인 돌아왔을때 저장된 책의 개수 늘어나야 하기 때문
     */
    public void onResume() {
        super.onResume();
        setCounts();
    }

    public void setCounts(){

        ArrayList<Book> book_count = new ArrayList<>();

        // 읽은 책들 개수 세팅
//        prefs = getSharedPreferences("book_data", MODE_PRIVATE);
//        Map<String, ?> read_book_prefsMap = prefs.getAll();
//        for(Map.Entry<String, ?> entrty : read_book_prefsMap.entrySet()){
//            count++;
//        }
        book_count = getStringArrayPref(READ_BOOKS , "read_book");
        count = book_count.size();

        read_books_count.setText(String.valueOf(count));

        // 개수 초기화
        count = 0;

        // 읽고 싶은 책들 개수 세팅
        book_count = getStringArrayPref(WANT_READ_BOOKS , "want_book");
        count = book_count.size();

        want_books_count.setText(String.valueOf(count));

        // 개수 초기화
        count = 0;

        // 저장된 문장들 개수 세팅
        // 저장되어있는 이미지들의 어레이리스트를 불러온다.
        ArrayList<String> str_save_imgs = new ArrayList<>();
        str_save_imgs = getStringArrayPref(getApplicationContext(), HIGHLIGHTED_IMGS_ARRAYLIST);
        count = str_save_imgs.size();

        saved_passage_count.setText(String.valueOf(count));
    }

    // 제이슨을 어레이리스트로 변환하는 메소드
    private ArrayList<String> getStringArrayPref(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList<String> urls = new ArrayList<String>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
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
        } catch (IllegalStateException | JsonSyntaxException e) {
            e.printStackTrace();
        }


        return urls;
    }




}
