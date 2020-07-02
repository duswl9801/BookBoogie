package com.reading_app.bookboogie;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class BookModifyActivity extends AppCompatActivity {

    private static final String WANT_READ_BOOKS = "want_read_books";
    private static final String READ_BOOKS = "read_books";

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1001;
    private static final int CAPTURE_IMAGE = 0;
    private static final int PICK_FROM_ALBUM = 1;

    private static ImageButton addBookImgBtn;
    Button month_btn;

    int modify_book_type = 0;

    String intent_book_title;

    Book book;

    String img_string;
    Bitmap img_bitmap;

    Bitmap bitmap;
    String bitmap_to_string;

    int read_year = 0, read_month = 0;

    ArrayList<Book> books = new ArrayList<>();

    // 수정할 책의 어레이리스트에서 인덱스.
    int index;

    // 읽은 월 추가 버튼을 눌러서 확인 버튼을 누르면 이 리스너가 실행됨.
    // 다이얼로그에서 선택된 년도, 월 값 받아서 버튼에 세팅.
    DatePickerDialog.OnDateSetListener month_picker_listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            read_year = year;
            read_month = month;

            month_btn.setText(String.valueOf(read_year) + "." + String.valueOf(read_month));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_read_book);

        Intent intent = getIntent();
        modify_book_type = intent.getIntExtra("modify_book_type", 0);
        intent_book_title = intent.getStringExtra("BookData");


        // xml 속성들
        TextView top = findViewById(R.id.textView24);
        top.setText("수정하기");

        ImageButton backBtn = findViewById(R.id.backBtn);
        Button saveBtn = findViewById(R.id.saveBtn);
        addBookImgBtn = findViewById(R.id.addBookImg);

        // saveBtn누를 때, 이 뷰들 안에 있는 값들을 가져와야 하기 때문에 final로 선언
        final EditText title = findViewById(R.id.book_title_editview);
        final RatingBar ratingBar = findViewById(R.id.ratingBar);
        month_btn = findViewById(R.id.input_month);
        final EditText memo = findViewById(R.id.memoText);

        TextView rate = findViewById(R.id.rate);
        TextView month = findViewById(R.id.month);
        TextView memo_tv = findViewById(R.id.memo);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        if(modify_book_type == 1){      // 읽고 싶은 책. 속성들 없앨거 없애기

            rate.setVisibility(View.GONE);
            month.setVisibility(View.GONE);
            memo_tv.setVisibility(View.GONE);

            ratingBar.setVisibility(View.GONE);
            month_btn.setVisibility(View.GONE);
            memo.setVisibility(View.GONE);

            // 불러오기
            books = getStringArrayPref(WANT_READ_BOOKS, "want_book");

            for(int i = 0; i < books.size(); i++){
                if(intent_book_title.equals(books.get(i).title)){
                    index = i;
                }
            }

            // 쉐어드 프리퍼런스에서 인텐트로 받은 책 제목으로 책 찾기
            SharedPreferences book_sharedpreference = getSharedPreferences("wanted_book_data", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = book_sharedpreference.getString(intent_book_title, "");
            book = gson.fromJson(json, Book.class);

            if(book.isSearchedBook == true){     // 책의 이미지가 검색 결과인 url로 전해졌을때
                Uri book_uri = Uri.parse(book.getImage());
                Glide.with(this).load(book_uri).into(addBookImgBtn);
            }
            else{       // 책의 이미지가 사용자가 직접 넣은 비트맵이미지일때

                img_string = book.getImage();

                // 문자열을 다시 비트맵으로 바꾸는 코드
                byte[] decodedByteArray = Base64.decode(img_string, Base64.NO_WRAP);
                img_bitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);

                addBookImgBtn.setImageBitmap(img_bitmap);

            }

            title.setText(book.getTitle());
            // 커서 맨 마지막으로 놓기
            title.setSelection(title.length());
        }
        else{

            // 불러오기
            books = getStringArrayPref(READ_BOOKS, "read_book");

            for(int i = 0; i < books.size(); i++){
                if(intent_book_title.equals(books.get(i).title)){
                    index = i;
                }
            }


            // 쉐어드 프리퍼런스 열기.
            SharedPreferences book_sharedpreference = getSharedPreferences("book_data", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = book_sharedpreference.getString(intent_book_title, "");
            book = gson.fromJson(json, Book.class);

            if(book.isSearchedBook == true){     // 책의 이미지가 검색 결과인 url로 전해졌을때
                Uri book_uri = Uri.parse(book.getImage());
                Glide.with(this).load(book_uri).into(addBookImgBtn);
            }
            else{       // 책의 이미지가 사용자가 직접 넣은 비트맵이미지일때

                img_string = book.getImage();

                // 문자열을 다시 비트맵으로 바꾸는 코드
                byte[] decodedByteArray = Base64.decode(img_string, Base64.NO_WRAP);
                img_bitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);

                addBookImgBtn.setImageBitmap(img_bitmap);

            }


            title.setText(book.getTitle());
            // 커서 맨 마지막으로 놓기
            title.setSelection(title.length());

            ratingBar.setRating(book.getRating());
            month_btn.setText(String.valueOf(book.getReadYear()) + "." + String.valueOf(book.getReadMonth()));
            memo.setText(book.getMemo());

        }

        addBookImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImgDialog();
            }
        });

        month_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YearMonthPickerDialog year_month_pick_dialog = new YearMonthPickerDialog();
                year_month_pick_dialog.setListener(month_picker_listener);
                year_month_pick_dialog.show(getSupportFragmentManager(), "YearMonthPicker");

            }

        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(modify_book_type == 1){  // 읽고 싶은 책 저장.

                    Book input_book = new Book();       // 쉐어드 프리퍼런스에 저장할 책 객체 생성.

                    if(book.isSearchedBook == true){       // 검색해서 저장하는 책일 때
                        input_book.setImage(book.getImage());
                    } else{     // 직접 입력하는 책일 때

                        input_book.isSearchedBook = false;
                        // 비트맵 이미지를 문자열로 바꿔서 저장.
                        bitmap_to_string = getBase64String(bitmap);
                        input_book.setImage(bitmap_to_string);

                    }
                    input_book.setTitle(title.getText().toString());

//                    /////////////////////////////////////////////////////////
//                    want_read_books.add(input_book);

                    // 어레이 리스트 객체 수정
                    books.get(index).setImage(input_book.image);
                    books.get(index).setTitle(input_book.title);

                    SharedPreferences wanted_book_pref = getSharedPreferences("wanted_book_data", MODE_PRIVATE);
                    SharedPreferences.Editor editor = wanted_book_pref.edit();

                    // gson이용해서 책 객체를 스트링으로 바꿈.
                    Gson gson = new Gson();
                    String book_to_string = gson.toJson(input_book);

                    // 바꾼 스트링 에디터로 쉐어드 프리퍼런스에 저장.
                    editor.putString(input_book.title, book_to_string);
                    editor.commit();
                    Toast.makeText(BookModifyActivity.this, "수정 성공", Toast.LENGTH_SHORT).show();


                    ////////////////////////////////////////////////
                    // 저장
                    setStringArrayPref(WANT_READ_BOOKS, "want_book",  books);


                    Intent intent = new Intent(BookModifyActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);


                }
                else{

                    Book input_book = new Book();

                    if(book.isSearchedBook == true){       // 검색해서 저장하는 책일 때

                        input_book.setImage(book.getImage());
                    }
                    else{

                        Log.d("searched", "book.isSearched이 false일때 코드");

                        // 저장되는건 input_book이라서 얘의 isSrarchedBook을 false로 바꿨어야 했는데 안바꿔서 직접저장할때 이미지가 안떴었음.
                        input_book.isSearchedBook = false;

                        // 비트맵 이미지를 문자열로 바꿔서 저장.
                        bitmap_to_string = getBase64String(bitmap);
                        input_book.setImage(bitmap_to_string);

                    }
                    input_book.setTitle(title.getText().toString());
                    input_book.setRating(ratingBar.getRating());
                    input_book.setReadYear(read_year);
                    input_book.setReadMonth(read_month);
                    input_book.setMemo(memo.getText().toString());

//                    ////////////////////////////////////////////////////
//                    read_books.add(input_book);

                    // 어레이 리스트 객체 수정
                    books.get(index).setImage(input_book.image);
                    books.get(index).setTitle(input_book.title);
                    books.get(index).setRating(input_book.rating);
                    books.get(index).setReadYear(input_book.read_year);
                    books.get(index).setReadMonth(input_book.read_month);
                    books.get(index).setMemo(input_book.memo);

                    SharedPreferences read_book_sharedpreference = getSharedPreferences("book_data", MODE_PRIVATE);
                    SharedPreferences.Editor book_editor = read_book_sharedpreference.edit();

                    // gson이용해서 책 객체를 스트링으로 바꿈.
                    Gson gson = new Gson();
                    String book_to_string = gson.toJson(input_book);

                    // 바꾼 스트링 에디터로 쉐어드 프리퍼런스에 저장.
                    book_editor.putString(input_book.title, book_to_string);
                    book_editor.commit();

                    Toast.makeText(BookModifyActivity.this, "수정 성공", Toast.LENGTH_SHORT).show();

                    Log.d("searched_book_check", String.valueOf(input_book.isSearchedBook));

                    ////////////////////////////////////////////////
                    // 저장
                    setStringArrayPref(READ_BOOKS, "read_book",  books);

                    Intent intent = new Intent(BookModifyActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }


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

                    // 카메라 암시적 인텐트. 사용할 때마다 카메라 어플 선택하도록 함.
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    // 카메라 어플 선택 메세지
                    String title = getResources().getString(R.string.chooser_title);
                    // 사진 찍을때마다 어플 선택하기 위해서 createChooser(인텐트, 메세지) 사용.
                    Intent chooser = Intent.createChooser(intent, title);

                    // 인텐트를 실행할 수 있는 액티비티가 1개 이상일 때 실행하도록 함.
                    if(intent.resolveActivity(getPackageManager()) != null){
                        startActivityForResult(chooser, CAPTURE_IMAGE);
                    }

                }else if(which == 1){// 앨범에서 불러오기 선택

                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, PICK_FROM_ALBUM);

                }
                /*
                기본 이미지 선택.
                사진 찍거나 갤러리에서 이미지 가져올때와 같은 방식으로 사진 전해주려고 이미지 리소스를 비트맵으로 변환해서 저장.
                 */
                else {
                    ImageButton addBookImgBtn = findViewById(R.id.addBookImg);

                    bitmap = BitmapFactory.decodeResource(BookModifyActivity.this.getResources(), R.drawable.book_cover_book);
                    book.isSearchedBook = false;
                    addBookImgBtn.setImageBitmap(bitmap);

                }

            }
        });

        getImgDialog.show();
    }

    // 카메라와 갤러리에서 이미지 가져올 떄 사용하는 메소드.
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case CAPTURE_IMAGE:

                if(resultCode == RESULT_OK){        // 카메라로 사진을 가져왔을때

                    book.isSearchedBook = false;

                    bitmap = (Bitmap) data.getExtras().get("data");

                    if(bitmap != null){
                        // 사이즈 조절은 나중에
                        addBookImgBtn.setImageBitmap(bitmap);
                    }

                }

                break;

            case PICK_FROM_ALBUM:

                if(resultCode == RESULT_OK) {        // 카메라로 사진을 가져왔을때

                    book.isSearchedBook = false;

                    InputStream in = null;

                    try{
                        in = getContentResolver().openInputStream(data.getData());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    bitmap = BitmapFactory.decodeStream(in);

                    addBookImgBtn.setImageBitmap(bitmap);

                }

                break;
        }

    }

    // 비트맵 이미지를 문자열로 변환시켜주는 메소드.
    public String getBase64String(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
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
