package com.reading_app.bookboogie;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChoicePassageImgActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1001;
    private static final int CAPTURE_IMAGE = 0;
    private static final int PICK_FROM_ALBUM = 1;

    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 0;

    // xml 파일의 속성들 선언
    ImageButton back_btn;
    Button pick_img_btn;
    ImageView picked_img;
    Button highlight_img_btn;

    // 카메라나 갤러리로 사진 받아서 이미지뷰에 맞게 사이즈 변환한 이미지
    Bitmap resized_img;
    // 이미지를 스트링으로 변환해서 저장하는 변수. 쉐어드 프리퍼런스에 저장하기 위해 사용.
    String str_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_passage_img);

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

        back_btn = findViewById(R.id.backBtn);
        pick_img_btn = findViewById(R.id.pick_img_btn);
        picked_img = findViewById(R.id.picked_img);
        highlight_img_btn = findViewById(R.id.highlight_img_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        pick_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImgDialog();
            }
        });

        highlight_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                BitmapDrawable bitmapDrawable = (BitmapDrawable) picked_img.getDrawable();
//
//                Log.d("img_check", picked_img.toString());
//
//                // 비트맵이미지가 널이면 못 넘어가도록 설정
//                if(resized_img == null){
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
//                    builder.setTitle("이미지를 넣어 주세요");
//
//                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {}
//                    });
//
//                } else {
//                    Intent intent = new Intent(getApplicationContext(), ImgCanvasActivity.class);
//                    startActivity(intent);
//                }
//
//
//                //todo finsh()??????????????????????????????
                Intent intent = new Intent(getApplicationContext(), ImgCanvasActivity.class);
                startActivity(intent);
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
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

//                    /////////////////////////////////////////////////////////////////////////////
//                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
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

            }
        });
        getImgDialog.show();
    }

    /*
     카메라와 갤러리에서 이미지 가져와 사이즈 조절하고, 이미지뷰에 이미지 넣고
     이미지를 문자열로 바꿔서 쉐어드 프리퍼런스에 저장함
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_TAKE_PHOTO:

                if(resultCode == RESULT_OK){        // 카메라로 사진을 가져왔을때

//                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    File file = new File(mCurrentPhotoPath);
                    Bitmap bitmap = null;
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


                        // 사이즈 조절하고 이미지 뷰에 세팅하고 비트맵 스트링으로 바꾸기
//                        resized_img = Bitmap.createScaledBitmap(bitmap, 300, 400, true);
//                        picked_img.setImageBitmap(resized_img);
//
//                        str_img = getBase64String(resized_img);

//                        picked_img.setImageBitmap(rotatedBitmap);
//
//                        str_img = getBase64String(rotatedBitmap);

                        picked_img.setImageBitmap(bitmap);

                        str_img = getBase64String(bitmap);

                        // 쉐어드에 저장하기
                        SharedPreferences prefs = getSharedPreferences("canvas_img_data", MODE_PRIVATE);
                        // 작성한다고 선언.
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("canvas_img", str_img);
                        editor.commit();

                    }

                }
                else if(resultCode == RESULT_CANCELED)
                {
                    Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
                }

                break;

            case PICK_FROM_ALBUM:

                if(resultCode == RESULT_OK) {        // 앨범에서 사진을 가져왔을때

                    InputStream in = null;

                    try{
                        in = getContentResolver().openInputStream(data.getData());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    Bitmap bitmap = BitmapFactory.decodeStream(in);

                    // 사진 크기 조절하고 이미지 뷰에 세팅하고 비트맵 스트링으로 바꾸기
                    resized_img = Bitmap.createScaledBitmap(bitmap, 300, 400, true);
                    picked_img.setImageBitmap(resized_img);

                    str_img = getBase64String(resized_img);

                    // 쉐어드에 저장하기
                    SharedPreferences prefs = getSharedPreferences("canvas_img_data", MODE_PRIVATE);
                    // 작성한다고 선언.
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("canvas_img", str_img);
                    editor.commit();

                }
                else if(resultCode == RESULT_CANCELED)
                {
                    Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
                }

                break;
        }

    }

    // 비트맵 이미지를 문자열로 변환시켜주는 메드.
    public String getBase64String(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
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
