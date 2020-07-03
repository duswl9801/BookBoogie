package com.reading_app.bookboogie;

import android.Manifest;
import android.app.AlertDialog;
import android.content.AbstractThreadedSyncAdapter;
import android.content.Context;
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
import android.preference.PreferenceManager;
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
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

public class AddWantReadBookActivity extends AppCompatActivity {

    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 0;

    // 어레이리스트를 쉐어드 프리퍼런스에 저장할 떄, key로 사용할 문자열 변수.
    // 쉐어드 프리퍼런스 불러올때 key로 사용할 문자열 변수.
    private static final String WANT_READ_BOOKS = "want_read_books";

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1001;
    private static final int CAPTURE_IMAGE = 0;
    private static final int PICK_FROM_ALBUM = 1;

    // onActivityRedult에서도 쓰이기 때문에 static으로 선언함.
    private static ImageButton addBookImgBtn;

    Book book;

    Bitmap bitmap = null;
    String bitmap_to_string;

    Bitmap resize_img;

    ArrayList<Book> want_read_books = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_want_read_book);

        /////////////////////////////////////////////
        // 불러오기
        want_read_books = getStringArrayPref(WANT_READ_BOOKS, "want_book");

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
                if((book.isSearchedBook == false) && (bitmap != null)){
                    Book input_book = new Book();       // 쉐어드 프리퍼런스에 저장할 책 객체 생성.

                    if(book.isSearchedBook == true){       // 검색해서 저장하는 책일 때
                        input_book.setImage(book.getImage());
                    } else{     // 직접 입력하는 책일 때

                        input_book.isSearchedBook = false;
                        // 비트맵 이미지를 문자열로 바꿔서 저장.
//                    bitmap_to_string = getBase64String(bitmap);
                        bitmap_to_string = getBase64String(resize_img);
                        input_book.setImage(bitmap_to_string);

                    }
                    input_book.setTitle(book_title_editview.getText().toString());

                    /////////////////////////////////////////////////////////
                    want_read_books.add(input_book);

                    SharedPreferences wanted_book_pref = getSharedPreferences("wanted_book_data", MODE_PRIVATE);
                    SharedPreferences.Editor editor = wanted_book_pref.edit();

                    // gson이용해서 책 객체를 스트링으로 바꿈.
                    Gson gson = new Gson();
                    String book_to_string = gson.toJson(input_book);

                    // 바꾼 스트링 에디터로 쉐어드 프리퍼런스에 저장.
                    editor.putString(input_book.title, book_to_string);
                    editor.commit();
                    Toast.makeText(AddWantReadBookActivity.this, "저장 성공", Toast.LENGTH_SHORT).show();


                    ////////////////////////////////////////////////
                    // 저장
                    setStringArrayPref(WANT_READ_BOOKS, "want_book",  want_read_books);


//                finish();
                    Intent intent = new Intent(AddWantReadBookActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if(book.isSearchedBook == true){
                    Book input_book = new Book();       // 쉐어드 프리퍼런스에 저장할 책 객체 생성.

                    if(book.isSearchedBook == true){       // 검색해서 저장하는 책일 때
                        input_book.setImage(book.getImage());
                    } else{     // 직접 입력하는 책일 때

                        input_book.isSearchedBook = false;
                        // 비트맵 이미지를 문자열로 바꿔서 저장.
//                    bitmap_to_string = getBase64String(bitmap);
                        bitmap_to_string = getBase64String(resize_img);
                        input_book.setImage(bitmap_to_string);

                    }
                    input_book.setTitle(book_title_editview.getText().toString());

                    /////////////////////////////////////////////////////////
                    want_read_books.add(input_book);

                    SharedPreferences wanted_book_pref = getSharedPreferences("wanted_book_data", MODE_PRIVATE);
                    SharedPreferences.Editor editor = wanted_book_pref.edit();

                    // gson이용해서 책 객체를 스트링으로 바꿈.
                    Gson gson = new Gson();
                    String book_to_string = gson.toJson(input_book);

                    // 바꾼 스트링 에디터로 쉐어드 프리퍼런스에 저장.
                    editor.putString(input_book.title, book_to_string);
                    editor.commit();
                    Toast.makeText(AddWantReadBookActivity.this, "저장 성공", Toast.LENGTH_SHORT).show();


                    ////////////////////////////////////////////////
                    // 저장
                    setStringArrayPref(WANT_READ_BOOKS, "want_book",  want_read_books);


//                finish();
                    Intent intent = new Intent(AddWantReadBookActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else {

                    new AlertDialog.Builder(AddWantReadBookActivity.this) // TestActivity 부분에는 현재 Activity의 이름 입력.
                            .setMessage("이미지를 넣어주세요")     // 제목 부분 (직접 작성)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {      // 버튼1 (직접 작성)
                                public void onClick(DialogInterface dialog, int which){
//                                    Toast.makeText(getApplicationContext(), "확인 누름", Toast.LENGTH_SHORT).show(); // 실행할 코드
                                }
                            })

                            .show();

                }


            }
        });

        addBookImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImgDialog();
            }
        });

    }

    public void onResume() {
        super.onResume();
        Log.i("addwant~~~~", "onResume");

    }

    // 여기서 받아온 isbn 정보로 검색 결과 나타내야 함.
    public void onRestart() {
        super.onRestart();
        Log.i("addwant~~~~", "onRestart");



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

                    resize_img = Bitmap.createScaledBitmap(bitmap, 600, 900, true);

                    addBookImgBtn.setImageBitmap(resize_img);

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

                        resize_img = Bitmap.createScaledBitmap(bitmap, 600, 900, true);

                        // 사이즈 조절은 나중에
                        addBookImgBtn.setImageBitmap(resize_img);


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
                    resize_img = Bitmap.createScaledBitmap(bitmap, 600, 900, true);

                    addBookImgBtn.setImageBitmap(resize_img);

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
//    private void setStringArrayPref(Context context, String key, ArrayList<Book> values) {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = prefs.edit();
//        JSONArray a = new JSONArray();
//        for (int i = 0; i < values.size(); i++) {
//            a.put(values.get(i));
//        }
//        if (!values.isEmpty()) {
//            editor.putString(key, a.toString());
//        } else {
//            editor.putString(key, null);
//        }
//        editor.apply();
//
//    }

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
//
//    private ArrayList<Book> getStringArrayPref(Context context, String key) {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        String json = prefs.getString(key, null);
//        ArrayList<Book> urls = new ArrayList<Book>();
//        if (json != null) {
//            try {
//                JSONArray a = new JSONArray(json);
//                for (int i = 0; i < a.length(); i++) {
//                    Book url = (Book) a.get(i);
//                    urls.add(url);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return urls;
//    }

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
