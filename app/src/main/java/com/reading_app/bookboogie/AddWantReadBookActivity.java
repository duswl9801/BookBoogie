package com.reading_app.bookboogie;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

public class AddWantReadBookActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1001;
    private static final int CAPTURE_IMAGE = 0;
    private static final int PICK_FROM_ALBUM = 1;

    // onActivityRedult에서도 쓰이기 때문에 static으로 선언함.
    private static ImageButton addBookImgBtn;

    Book book;

    Bitmap bitmap;
    String bitmap_to_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_want_read_book);

        // 사용자에게 카메라 사용 권한 승인 받는 코드
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

        // 책 정보 들어있는 인텐트 받기. 제목이 "title"이면 사용자가 직접 입력하는 경우.
        Intent book_data = getIntent();
        book = (Book)book_data.getSerializableExtra("Book");

        ImageButton backBtn = findViewById(R.id.backBtn);
        Button saveBtn = findViewById(R.id.saveBtn);
        addBookImgBtn = findViewById(R.id.addBookImg);
        // saveBtn누를 때, 이 뷰 안에 있는 값들을 가져와야 하기 때문에 final로 선언
        final EditText book_title_editview = findViewById(R.id.book_title_editview);

        if(book.getTitle().equals("title")){    // 사용자가 직접 책 추가하는 경우
            book.isSearchedBook = false;
        } else {        // 책 정보 받아와서 추가하는 경우

            book_title_editview.setText(book.getTitle());
            // 커서 맨 마지막으로 놓기
            book_title_editview.setSelection(book_title_editview.length());
            Glide.with(this).load(book.getImage()).override(400, 600).into(addBookImgBtn);

        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // 저장버튼을 누르면 읽고 싶은 책들 쉐어드 프리퍼런스에 입력한 값 저장해야 함.
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Book input_book = new Book();       // 쉐어드 프리퍼런스에 저장할 책 객체 생성.

                if(book.isSearchedBook == true){       // 검색해서 저장하는 책일 때
                    input_book.setImage(book.getImage());
                } else{     // 직접 입력하는 책일 때

                    input_book.isSearchedBook = false;
                    // 비트맵 이미지를 문자열로 바꿔서 저장.
                    bitmap_to_string = getBase64String(bitmap);
                    input_book.setImage(bitmap_to_string);

                }
                input_book.setTitle(book_title_editview.getText().toString());

                SharedPreferences wanted_book_pref = getSharedPreferences("wanted_book_data", MODE_PRIVATE);
                SharedPreferences.Editor editor = wanted_book_pref.edit();

                // gson이용해서 책 객체를 스트링으로 바꿈.
                Gson gson = new Gson();
                String book_to_string = gson.toJson(input_book);

                // 바꾼 스트링 에디터로 쉐어드 프리퍼런스에 저장.
                editor.putString(input_book.title, book_to_string);
                editor.commit();
                Toast.makeText(AddWantReadBookActivity.this, "저장 성공", Toast.LENGTH_SHORT).show();

                finish();
            }
        });

        addBookImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImgDialog();
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

                    bitmap = BitmapFactory.decodeResource(AddWantReadBookActivity.this.getResources(), R.drawable.book_cover_book);
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



}
