package com.reading_app.bookboogie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class PassageCheckActivity extends AppCompatActivity {

    String str;
    Bitmap bit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passage_check);

        ImageView imgv = findViewById(R.id.imgv);

        SharedPreferences preferences = getSharedPreferences("highlighted_img", MODE_PRIVATE);
        str = preferences.getString("str_highlighted_img", "default");

        // 문자열을 다시 비트맵으로 바꾸는 코드
        byte[] decodedByteArray = Base64.decode(str, Base64.NO_WRAP);
        bit = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);

        imgv.setImageBitmap(bit);



    }

}
