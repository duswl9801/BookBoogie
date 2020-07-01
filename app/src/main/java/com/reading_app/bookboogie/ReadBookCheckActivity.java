package com.reading_app.bookboogie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

public class ReadBookCheckActivity extends AppCompatActivity {

    Book book;
    String intent_book_title;

    String img_string;
    Bitmap img_bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_book_check);

        Intent intent = getIntent();
        // 인텐트로 보내는 데이터의 용량이 한계가 있어서 책의 제목만 인텐트로 보내고 쉐어드 프리퍼런스에서 꺼내도록 함.
////        book = (Book) intent.getSerializableExtra("BookData");
//        intent_book = (Book) intent.getSerializableExtra("BookData");
        intent_book_title = intent.getStringExtra("BookData");

        // 쉐어드 프리퍼런스 열기.
        SharedPreferences book_sharedpreference = getSharedPreferences("book_data", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = book_sharedpreference.getString(intent_book_title, "");
        book = gson.fromJson(json, Book.class);

        TextView top_textview = findViewById(R.id.top_textview);
        top_textview.setText(book.title);

        ImageButton backBtn = findViewById(R.id.backBtn);
        ImageView book_img = findViewById(R.id.book_img);
        TextView book_title_textview = findViewById(R.id.book_title_textview);
        RatingBar rating_bar = findViewById(R.id.ratingBar);
        TextView year_month_textview = findViewById(R.id.year_month_textview);
        TextView memo = findViewById(R.id.memoText);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(book.isSearchedBook == true){     // 책의 이미지가 검색 결과인 url로 전해졌을때
            Uri book_uri = Uri.parse(book.getImage());
            Glide.with(this).load(book_uri).into(book_img);
        }
        else{       // 책의 이미지가 사용자가 직접 넣은 비트맵이미지일때

            img_string = book.getImage();

            // 문자열을 다시 비트맵으로 바꾸는 코드
            byte[] decodedByteArray = Base64.decode(img_string, Base64.NO_WRAP);
            img_bitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);

            book_img.setImageBitmap(img_bitmap);

        }


        book_title_textview.setText(book.getTitle());
        rating_bar.setRating(book.getRating());
        year_month_textview.setText(String.valueOf(book.getReadYear()) + "." + String.valueOf(book.getReadMonth()));
        memo.setText(book.getMemo());


    }
}
