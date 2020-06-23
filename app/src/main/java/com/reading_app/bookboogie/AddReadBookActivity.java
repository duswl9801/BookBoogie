package com.reading_app.bookboogie;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

public class AddReadBookActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1001;
    private static final int CAPTURE_IMAGE = 0;
    private static final int PICK_FROM_ALBUM = 1;
//    private File tempFile;
    private static ImageButton addBookImgBtn;
    Bitmap bitmap;
//    Book book;

    int readYear = 0, readMonth = 0, readDay = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_read_book);

        int permssionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (permssionCheck!= PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this,"권한 승인이 필요합니다",Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                Toast.makeText(this,"000부분 사용을 위해 카메라 권한이 필요합니다.",Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
                Toast.makeText(this,"000부분 사용을 위해 카메라 권한이 필요합니다.",Toast.LENGTH_LONG).show();

            }
        }

        Book book = new Book();

        // 책 정보 들어있는 인텐트 받기. 제목이 "title"이면 사용자가 직접 입력하는 경우.
        Intent book_data = getIntent();
        book = (Book)book_data.getSerializableExtra("Book");

        ImageButton backBtn = findViewById(R.id.backBtn);
        Button saveBtn = findViewById(R.id.saveBtn);
        addBookImgBtn = findViewById(R.id.addBookImg);
        final EditText title = findViewById(R.id.book_title_editview);
        final RatingBar ratingBar = findViewById(R.id.ratingBar);
        final TextView day = findViewById(R.id.day);
//        Button categoryBtn = findViewById(R.id.categoryBtn);
        final EditText memo = findViewById(R.id.memoText);

//        EditText title = findViewById(R.id.titleText);
//        RatingBar ratingBar = findViewById(R.id.ratingBar);
//        TextView day = findViewById(R.id.day);
//        Button categoryBtn = findViewById(R.id.categoryBtn);
//        EditText memo = findViewById(R.id.memoText);

        if(book.getTitle().equals("title")){    // 사용자가 직접 책 추가하는 경우

        } else {        // 책 정보 받아와서 추가하는 경우
            title.setText(book.getTitle());
            // 커서 맨 마지막으로 놓기
            title.setSelection(title.length());
            Glide.with(this).load(book.getImage()).override(400, 600).into(addBookImgBtn);

        }


        addBookImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImgDialog();
            }
        });

        day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecteDay();
//                Log.i("Day", String.valueOf(readYear + readMonth + readDay));
//                Toast.makeText(getApplicationContext(), readYear+"."+readMonth+"."+readDay, Toast.LENGTH_SHORT).show();

//                TextView day = findViewById(R.id.day);
//                day.setText(book.readYear+"."+book.readMonth+"."+book.readDay);
//                day.setText(readYear+"."+readMonth+"."+readDay);
            }

        });

//         save 버튼을 눌렀을때 Book 클래스의 객체가 전달 되어야 함. 계속 객체에 null이 들어가 있다고 에러가 뜸.
        // 저장버튼을 누르면 쉐어드 프리퍼런스에 입력한 값 저장해야 함.
        // 화면에 있는 값들 다 받아서 책
        saveBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Book input_book = new Book();

                input_book.setImage(addBookImgBtn.getBackground().toString());
                input_book.setTitle(title.getText().toString());
                input_book.setRating(ratingBar.getRating());
                input_book.setMemo(memo.getText().toString());

                Log.d("BookDataCheck", input_book.getImage().toString());
                Log.d("BookDataCheck", String.valueOf(input_book.getRating()));

                SharedPreferences book_sharedpreference = getSharedPreferences("book_data", MODE_PRIVATE);
                SharedPreferences.Editor book_editor = book_sharedpreference.edit();

                // gson이용해서 책 객체를 스트링으로 바꿈.
                Gson gson = new Gson();
                String book_to_string = gson.toJson(input_book);

                // 바꾼 스트링 에디터로 쉐어드 프리퍼런스에 저장.
                book_editor.putString(input_book.title, book_to_string);
                book_editor.commit();

                Toast.makeText(AddReadBookActivity.this, "commit", Toast.LENGTH_SHORT).show();


//                book.img = bitmap;
//                book.title = title.getText().toString();
//                book.rating = ratingBar.getRating();
//                book.memo = memo.getText().toString();
//
//                Intent intent = new Intent(getApplicationContext(), ReadBooksCategoriesActivity.class);
//
//                intent.putExtra("Book", book);
//
//                startActivity(intent);

            }

        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), ReadBooksCategoriesActivity.class);
//                startActivity(intent);
                onBackPressed();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this,"승인이 허가되어 있습니다.",Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this,"아직 승인받지 않았습니다.",Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

    // 읽은 책의 이미지 추가하는 메소드.
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
//                        startActivity(chooser);
                        // (intent, requestCode)
                        startActivityForResult(chooser, CAPTURE_IMAGE);
                    }

                }else if(which == 1) {// 앨범에서 불러오기 선택

                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, PICK_FROM_ALBUM);
                }
                else {
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

    void selecteDay(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                book.readYear = year;
//                book.readMonth = month + 1;
//                book.readDay = dayOfMonth;
                readYear = year;
                readMonth = month + 1;
                readDay = dayOfMonth;

                final TextView day = findViewById(R.id.day);
                day.setText(readYear+"."+readMonth+"."+readDay);

            }
        }, 2020, 2, 10);


        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
// switch코드 쓰는게 훨씬 직관적
        if(requestCode == CAPTURE_IMAGE){

            bitmap = (Bitmap) data.getExtras().get("data");
            if(bitmap != null){
                // 사이즈 조절은 나중에
                addBookImgBtn.setImageBitmap(bitmap);
//                addBookImgBtn.getBackground(bitmap);
            }
        }

        if (requestCode == PICK_FROM_ALBUM) {

            InputStream in = null;

            try{
                in = getContentResolver().openInputStream(data.getData());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            bitmap = BitmapFactory.decodeStream(in);
            addBookImgBtn.setImageBitmap(bitmap);



//            Uri photoUri = data.getData();
//            Cursor cursor = null;
//
//            try {
//
//                /*
//                 *  Uri 스키마를
//                 *  content:/// 에서 file:/// 로  변경한다.
//                 */
//                String[] proj = { MediaStore.Images.Media.DATA };
//
//                assert photoUri != null;
//                cursor = getContentResolver().query(photoUri, proj, null, null, null);
//
//                assert cursor != null;
//                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//
//                cursor.moveToFirst();
//
//                tempFile = new File(cursor.getString(column_index));
//
//            } finally {
//                if (cursor != null) {
//                    cursor.close();
//                }
//            }
//
//            setImage();

        }

    }

//    private void setImage() {
//
//        ImageButton imageView = findViewById(R.id.addBookImg);
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
//
//        imageView.setImageBitmap(originalBm);
//
//    }

}
