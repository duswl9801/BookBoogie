package com.reading_app.bookboogie.unused;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.reading_app.bookboogie.CalenderActivity;
import com.reading_app.bookboogie.DictionaryActivity;
import com.reading_app.bookboogie.R;
import com.reading_app.bookboogie.unused.ProfileActivity;

public class BtnMain extends AppCompatActivity {

    // setContentView 전에 view들 사용 못함.................!!
//    Button read_books_btn = findViewById(R.id.read_books_btn);
//    Button want_read_btn = findViewById(R.id.want_read_btn);
//    Button saved_passage_btn = findViewById(R.id.saved_passage_btn);
//    Button calender_btn = findViewById(R.id.calender_btn);
//    Button dictionary_btn = findViewById(R.id.dictionary_btn);
//    Button profile_btn = findViewById(R.id.profile_btn);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button read_books_btn = findViewById(R.id.read_books_btn);
        Button want_read_btn = findViewById(R.id.want_read_btn);
        Button saved_passage_btn = findViewById(R.id.saved_passage_btn);
        Button calender_btn = findViewById(R.id.calender_btn);
        Button dictionary_btn = findViewById(R.id.dictionary_btn);
        Button profile_btn = findViewById(R.id.profile_btn);

        // 사용자가 원하는 버튼 클릭했을 때, 해당 액티비티로 이동
//        read_books_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), ReadBooksActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        want_read_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), WantReadActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        saved_passage_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), SavedPassageActivity.class);
//                startActivity(intent);
//            }
//        });

        calender_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CalenderActivity.class);
                startActivity(intent);
            }
        });

        dictionary_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DictionaryActivity.class);
                startActivity(intent);
            }
        });

        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

    }

//    // 사용자가 원하는 버튼 클릭했을 때, 해당 액티비티로 이동하는 메소드
//    public void switchScreen(Button touched_btn){
//
//        if(touched_btn == read_books_btn){
//            touched_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getApplicationContext(), ReadBooksActivity.class);
//                    startActivity(intent);
//                }
//            });
//        }else if(touched_btn == want_read_btn){
//            touched_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getApplicationContext(), WantReadActivity.class);
//                    startActivity(intent);
//                }
//            });
//        }else if(touched_btn == saved_passage_btn){
//            touched_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getApplicationContext(), SavedPassageActivity.class);
//                    startActivity(intent);
//                }
//            });
//        }else if(touched_btn == calender_btn){
//            touched_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getApplicationContext(), CalenderActivity.class);
//                    startActivity(intent);
//                }
//            });
//        }else if(touched_btn == dictionary_btn){
//            touched_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getApplicationContext(), DictionaryActivity.class);
//                    startActivity(intent);
//                }
//            });
//        }else if(touched_btn == profile_btn){
//            touched_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
//                    startActivity(intent);
//                }
//            });
//        }
//
//    }

}
