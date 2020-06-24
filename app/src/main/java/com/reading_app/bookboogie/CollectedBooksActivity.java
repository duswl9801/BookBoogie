package com.reading_app.bookboogie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    0이 읽은 책, 1이 읽고 싶은 책이다.
    기본 값은 0.
     */
    int book_type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collected_books);

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
//        Map<String, ?> allEntries = book_sharedpreference.getAll();
//        for(Map.Entry<String, ?> entry : allEntries.entrySet()){
//            Gson gson = new Gson();
//            String json = entry.getString();
//            final String key = entry.getKey();
//            books.add(entry.getValue(key));
//        }



        Intent category = getIntent();
        String receive_category = category.getStringExtra("category");

//        // books 초기화
//        InitializeBookData();

        ImageButton back_btn = findViewById(R.id.back_btn);
        TextView category_name = findViewById(R.id.collected_books_category);
        ImageButton add_book_btn = findViewById(R.id.add_book_btn);

        category_name.setText(receive_category);

        RecyclerView books_recyclerview = findViewById(R.id.recyclerview_books);
        books_recyclerview.setLayoutManager(new GridLayoutManager(this, 3));
        books_recyclerview.addItemDecoration(new DividerItemDecoration(books_recyclerview.getContext(), 1));

        final CollectedBooksAdapter recyclerview_book_adapter = new CollectedBooksAdapter(this, books);
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

                }else{      // 읽고 싶은 책 추가 액티비티로 이동.
                    Intent intent = new Intent(getApplicationContext(), AddWantReadBookActivity.class);
                    intent.putExtra("Book", new Book());
                    startActivity(intent);
                }

            }
        });

    }

    public void InitializeBookData() {

//        books.add(new Book("샘플", "image", "가", "100", "가", "0", "가", 3.5, 2020, 06, "소설", "가"));
//        books.add(new Book("샘플", "image", "가", "100", "가", "0", "가", 3.5, 2020, 06, "소설", "가"));
//        books.add(new Book("샘플", "image", "가", "100", "가", "0", "가", 3.5, 2020, 06, "소설", "가"));
//        books.add(new Book("샘플", "image", "가", "100", "가", "0", "가", 3.5, 2020, 06, "소설", "가"));
//        books.add(new Book("샘플", "image", "가", "100", "가", "0", "가", 3.5, 2020, 06, "소설", "가"));
//        books.add(new Book("샘플", "image", "가", "100", "가", "0", "가", 3.5, 2020, 06, "소설", "가"));
//        books.add(new Book("샘플", "image", "가", "100", "가", "0", "가", 3.5, 2020, 06, "소설", "가"));
//        books.add(new Book("샘플", "image", "가", "100", "가", "0", "가", 3.5, 2020, 06, "소설", "가"));
//        books.add(new Book("샘플", "image", "가", "100", "가", "0", "가", 3.5, 2020, 06, "소설", "가"));
//        books.add(new Book("샘플", "image", "가", "100", "가", "0", "가", 3.5, 2020, 06, "소설", "가"));
//        books.add(new Book("샘플", "image", "가", "100", "가", "0", "가", 3.5, 2020, 06, "소설", "가"));
//        books.add(new Book("샘플", "image", "가", "100", "가", "0", "가", 3.5, 2020, 06, "소설", "가"));
//        books.add(new Book("샘플", "image", "가", "100", "가", "0", "가", 3.5, 2020, 06, "소설", "가"));
//        books.add(new Book("샘플", "image", "가", "100", "가", "0", "가", 3.5, 2020, 06, "소설", "가"));
//        books.add(new Book("샘플", "image", "가", "100", "가", "0", "가", 3.5, 2020, 06, "소설", "가"));
//        books.add(new Book("샘플", "image", "가", "100", "가", "0", "가", 3.5, 2020, 06, "소설", "가"));
//        books.add(new Book("샘플", "image", "가", "100", "가", "0", "가", 3.5, 2020, 06, "소설", "가"));
//        books.add(new Book("샘플", "image", "가", "100", "가", "0", "가", 3.5, 2020, 06, "소설", "가"));
//        books.add(new Book("샘플", "image", "가", "100", "가", "0", "가", 3.5, 2020, 06, "소설", "가"));
//        books.add(new Book("샘플", "image", "가", "100", "가", "0", "가", 3.5, 2020, 06, "소설", "가"));
//

        books.add(new Book());
        books.add(new Book());
        books.add(new Book());
        books.add(new Book());
        books.add(new Book());
        books.add(new Book());
        books.add(new Book());
        books.add(new Book());
        books.add(new Book());
        books.add(new Book());
        books.add(new Book());
        books.add(new Book());
        books.add(new Book());
        books.add(new Book());
        books.add(new Book());
        books.add(new Book());
        books.add(new Book());
        books.add(new Book());
        books.add(new Book());
        books.add(new Book());
        books.add(new Book());
        books.add(new Book());
        books.add(new Book());
        books.add(new Book());
        books.add(new Book());


    }
}
