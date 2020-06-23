package com.reading_app.bookboogie;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import com.bumptech.glide.Glide;

public class AddWantReadBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_want_read_book);

        Book book = new Book();

        Intent book_data = getIntent();
        book = (Book)book_data.getSerializableExtra("Book");

        ImageButton addBookImgBtn = findViewById(R.id.addBookImg);
        ImageButton backBtn = findViewById(R.id.backBtn);
        EditText book_title_editview = findViewById(R.id.book_title_editview);

        if(book.getTitle().equals("title")){    // 사용자가 직접 책 추가하는 경우

        } else {        // 책 정보 받아와서 추가하는 경우
            book_title_editview.setText(book.getTitle());
            // 커서 맨 마지막으로 놓기
            book_title_editview.setSelection(book_title_editview.length());
            Glide.with(this).load(book.getImage()).override(400, 600).into(addBookImgBtn);
        }


        addBookImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImgDialog();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), WantReadCategoriesActivity.class);
//                startActivity(intent);
                onBackPressed();
            }
        });
    }

    // 읽을 책의 이미지 추가하는 메소드.
    void getImgDialog(){

        ArrayList<String> getImgWay = new ArrayList<String>();
        getImgWay.add("카메라로 사진 촬영");
        getImgWay.add("앨범에서 불러오기");
        getImgWay.add("기본 이미지로 설정하기");
        /*
        String 안쓰는 이유 onenote -> 안드로이드 -> 안드로이드 스튜디오에 있음.
        arraylist getImgWay를 문자열 array로 바꿈.
        getImgWayList = ["카메라로 사진 촬영", "앨범에서 불러오기"]
         */
        final CharSequence[] getImgWayList = getImgWay.toArray(new String[getImgWay.size()]);

        // dialog Builder 생성
        AlertDialog.Builder getImgDialog = new AlertDialog.Builder(this);
        // setItems()로 dialog 단일 선택 목록 생성.
        getImgDialog.setItems(getImgWayList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {// dialog는 버튼 클릭 받는 dialog, which는 dialog 목록의 idx

                if(which == 0){// 카메라로 사진촬영 선택
                    // 카메라 암시적 인텐트
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivity(intent);
                }else if(which == 1){// 앨범에서 불러오기 선택

                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, 1);
                }else {
                    ImageButton addBookImgBtn = findViewById(R.id.addBookImg);
                    addBookImgBtn.setImageResource(R.drawable.book_cover_book);
                }

//                String selectedText = items[which].toString();
//                Toast.makeText(AddReadBookActivity.this, selectedText, Toast.LENGTH_SHORT).show();
            }
        });
//        getImgDialog.create();// 이거만 있으면 dialog 뜨지 않음.
        getImgDialog.show();
    }



}
