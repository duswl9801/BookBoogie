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
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddReadBookActivity extends AppCompatActivity {

    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 0;


    // 어레이리스트를 쉐어드 프리퍼런스에 저장할 떄, key로 사용할 문자열 변수.
    // 쉐어드 프리퍼런스 불러올때 key로 사용할 문자열 변수.
    private static final String READ_BOOKS = "read_books";

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1001;
    private static final int CAPTURE_IMAGE = 0;
    private static final int PICK_FROM_ALBUM = 1;

    // onActivityRedult에서도 쓰이기 때문에 static으로 선언함.
    private static ImageButton addBookImgBtn;
    Button month_btn;

    Bitmap bitmap = null;
    Book book;

    Bitmap resize_img;

    String bitmap_to_string;

    int read_year = 0, read_month = 0;

    ArrayList<Book> read_books = new ArrayList<>();

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

        /////////////////////////////////////////////
        // 불러오기
        read_books = getStringArrayPref(READ_BOOKS, "read_book");

        // 책 정보 들어있는 인텐트 받기. 제목이 "title"이면 사용자가 직접 입력하는 경우.
        Intent book_data = getIntent();
        book = (Book)book_data.getSerializableExtra("Book");

        ImageButton backBtn = findViewById(R.id.backBtn);
        Button saveBtn = findViewById(R.id.saveBtn);
        addBookImgBtn = findViewById(R.id.addBookImg);

        // saveBtn누를 때, 이 뷰들 안에 있는 값들을 가져와야 하기 때문에 final로 선언
        final EditText title = findViewById(R.id.book_title_editview);
        final RatingBar ratingBar = findViewById(R.id.ratingBar);
        month_btn = findViewById(R.id.input_month);
        final EditText memo = findViewById(R.id.memoText);

        if(book.getTitle().equals("title")){    // 사용자가 직접 책 추가하는 경우
            book.isSearchedBook = false;
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

        month_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YearMonthPickerDialog year_month_pick_dialog = new YearMonthPickerDialog();
                year_month_pick_dialog.setListener(month_picker_listener);
                year_month_pick_dialog.show(getSupportFragmentManager(), "YearMonthPicker");

            }

        });

//         save 버튼을 눌렀을때 Book 클래스의 객체가 전달 되어야 함. 계속 객체에 null이 들어가 있다고 에러가 뜸.
        // 저장버튼을 누르면 읽은 책들 쉐어드 프리퍼런스에 입력한 값 저장해야 함.
        saveBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Log.d("searched", "book.isSearchedBook is " + book.isSearchedBook);

                if( (book.isSearchedBook == false) && (bitmap != null) ){
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

                    ////////////////////////////////////////////////////
                    read_books.add(input_book);

                    Log.d("BookDataCheck", input_book.getImage().toString());
                    Log.d("BookDataCheck", String.valueOf(input_book.getRating()));

                    SharedPreferences read_book_sharedpreference = getSharedPreferences("book_data", MODE_PRIVATE);
                    SharedPreferences.Editor book_editor = read_book_sharedpreference.edit();

                    // gson이용해서 책 객체를 스트링으로 바꿈.
                    Gson gson = new Gson();
                    String book_to_string = gson.toJson(input_book);

                    // 바꾼 스트링 에디터로 쉐어드 프리퍼런스에 저장.
                    book_editor.putString(input_book.title, book_to_string);
                    book_editor.commit();

                    Toast.makeText(AddReadBookActivity.this, "저장 성공", Toast.LENGTH_SHORT).show();

                    Log.d("searched_book_check", String.valueOf(input_book.isSearchedBook));

                    ////////////////////////////////////////////////
                    // 저장
                    setStringArrayPref(READ_BOOKS, "read_book",  read_books);

//                finish();
                    Intent intent = new Intent(AddReadBookActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if(book.isSearchedBook == true){
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

                    ////////////////////////////////////////////////////
                    read_books.add(input_book);

                    Log.d("BookDataCheck", input_book.getImage().toString());
                    Log.d("BookDataCheck", String.valueOf(input_book.getRating()));

                    SharedPreferences read_book_sharedpreference = getSharedPreferences("book_data", MODE_PRIVATE);
                    SharedPreferences.Editor book_editor = read_book_sharedpreference.edit();

                    // gson이용해서 책 객체를 스트링으로 바꿈.
                    Gson gson = new Gson();
                    String book_to_string = gson.toJson(input_book);

                    // 바꾼 스트링 에디터로 쉐어드 프리퍼런스에 저장.
                    book_editor.putString(input_book.title, book_to_string);
                    book_editor.commit();

                    Toast.makeText(AddReadBookActivity.this, "저장 성공", Toast.LENGTH_SHORT).show();

                    Log.d("searched_book_check", String.valueOf(input_book.isSearchedBook));

                    ////////////////////////////////////////////////
                    // 저장
                    setStringArrayPref(READ_BOOKS, "read_book",  read_books);

//                finish();
                    Intent intent = new Intent(AddReadBookActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else{

                    new AlertDialog.Builder(AddReadBookActivity.this) // TestActivity 부분에는 현재 Activity의 이름 입력.
                            .setMessage("이미지를 넣어주세요")     // 제목 부분 (직접 작성)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {      // 버튼1 (직접 작성)
                                public void onClick(DialogInterface dialog, int which){
                                }
                            })
//                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {     // 버튼2 (직접 작성)
//                                public void onClick(DialogInterface dialog, int which){
////                                    Toast.makeText(getApplicationContext(), "취소 누름", Toast.LENGTH_SHORT).show(); // 실행할 코드
//                                }
//                            })
                            .show();

                }


            }

        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
//                    // 카메라 암시적 인텐트. 사용할 때마다 카메라 어플 선택하도록 함.
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//                    // 카메라 어플 선택 메세지
//                    String title = getResources().getString(R.string.chooser_title);
//                    // 사진 찍을때마다 어플 선택하기 위해서 createChooser(인텐트, 메세지) 사용.
//                    Intent chooser = Intent.createChooser(intent, title);
//
//                    // 인텐트를 실행할 수 있는 액티비티가 1개 이상일 때 실행하도록 함.
//                    if(intent.resolveActivity(getPackageManager()) != null){
//                        startActivityForResult(chooser, CAPTURE_IMAGE);
//                    }
                    dispatchTakePictureIntent();

                }else if(which == 1) {// 앨범에서 불러오기 선택

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

                    bitmap = BitmapFactory.decodeResource(AddReadBookActivity.this.getResources(), R.drawable.book_cover_book);
                    book.isSearchedBook = false;
                    addBookImgBtn.setImageBitmap(bitmap);

                    Log.d("searched", "book.isSearchedBook is " + book.isSearchedBook);
                }

            }
        });
        getImgDialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case CAPTURE_IMAGE:

                if(resultCode == RESULT_OK){        // 카메라로 사진을 가져왔을때

                    book.isSearchedBook = false;

//                    bitmap = (Bitmap) data.getExtras().get("data");
                    File file = new File(mCurrentPhotoPath);
                    bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(bitmap != null){

                        ExifInterface ei = null;
                        try {
                            ei = new ExifInterface(mCurrentPhotoPath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_UNDEFINED);

                        Bitmap rotatedBitmap = null;
                        switch(orientation) {

                            case ExifInterface.ORIENTATION_ROTATE_90:
                                rotatedBitmap = rotateImage(bitmap, 90);
                                break;

                            case ExifInterface.ORIENTATION_ROTATE_180:
                                rotatedBitmap = rotateImage(bitmap, 180);
                                break;

                            case ExifInterface.ORIENTATION_ROTATE_270:
                                rotatedBitmap = rotateImage(bitmap, 270);
                                break;

                            case ExifInterface.ORIENTATION_NORMAL:
                            default:
                                rotatedBitmap = bitmap;
                        }

                        Bitmap resize_img = Bitmap.createScaledBitmap(bitmap, 600, 900, true);
                        // 사이즈 조절은 나중에
                        addBookImgBtn.setImageBitmap(resize_img);
                    }

                }
                else if(resultCode == RESULT_CANCELED)
                {
                    Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
                }

                break;

            case PICK_FROM_ALBUM:

                if(resultCode == RESULT_OK) {        // 앨범에서 사진을 가져왔을때

                    book.isSearchedBook = false;

                    InputStream in = null;

                    try{
                        in = getContentResolver().openInputStream(data.getData());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    bitmap = BitmapFactory.decodeStream(in);

                    resize_img = Bitmap.createScaledBitmap(bitmap, 600, 900, true);

                    addBookImgBtn.setImageBitmap(resize_img);

                }
                else if(resultCode == RESULT_CANCELED)
                {
                    Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
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


    // 카메라로 촬영한 이미지를 파일로 저장해주는 함수
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // 카메라 인텐트를 실행하는 부분
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.reading_app.bookboogie.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    // 이미지 돌려주는 함수
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }


}
