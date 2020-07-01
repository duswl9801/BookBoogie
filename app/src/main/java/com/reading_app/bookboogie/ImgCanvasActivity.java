package com.reading_app.bookboogie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ImgCanvasActivity extends AppCompatActivity {

    // 어레이리스트를 쉐어드 프리퍼런스에 저장할 떄, key로 사용할 문자열 변수.
    // 쉐어드 프리퍼런스 불러올때 key로 사용할 문자열 변수.
    private static final String HIGHLIGHTED_IMGS_ARRAYLIST = "highlighted_imgs";

    // xml 파일의 속성들 선언
    ImageButton back_btn;
    Button save_btn;
    Button initialize_btn;
    TouchDrawImgCanvas my_canvas;

    // 쉐어드에 저장되어있는 비트맵이미지 넣을 변수. 문자열로 저장되어 있기 때문에 string으로 먼저 받고 비트맵으로 바꾼다.
    Bitmap img;
    String str_img;

    Bitmap resized_img;

    // 사용자가 save 버튼을 누를때 캔버스뷰를 비트맵 이미지로 저장할때 사용하는 변수.
    Bitmap save_img;
    String str_save_img;

    /*
    액티비티가 시작될 떄 쉐어드 프리퍼런스에 저장되어있는 값을 이 변수에 넣음.
    사용자가 형광펜을 다 치고 세이브 버튼을 누르면 문자열로 변환된 캔버스 뷰의 이미지가 이 변수에 추가됨.
    그리고 이 변수가 제이슨 형식으로 변환되어 쉐어드 프리퍼런스에 저장됨.
     */
    ArrayList<String> str_save_imgs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_canvas);

        back_btn = findViewById(R.id.backBtn);
        save_btn = findViewById(R.id.saveBtn);
        initialize_btn = findViewById(R.id.initialize_btn);
        my_canvas = (TouchDrawImgCanvas)findViewById(R.id.img_canvas);

        // 저장되어있는 이미지들의 어레이리스트를 불러온다.
        str_save_imgs = getStringArrayPref(getApplicationContext(), HIGHLIGHTED_IMGS_ARRAYLIST);

        // ChoicePassageImgActivity에서 저장한 이미지를 불러와서 이 액티비티에서 사용한다.
        SharedPreferences prefs = getSharedPreferences("canvas_img_data", MODE_PRIVATE);
        str_img = prefs.getString("canvas_img", "default");

        // 문자열을 다시 비트맵으로 바꾸는 코드
        byte[] decodedByteArray = Base64.decode(str_img, Base64.NO_WRAP);
        img = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);

        // 이미지 사이즈 작게 나와서 크게 키움.
        resized_img = Bitmap.createScaledBitmap(img, 900, 1200, true);

        // 캔버스에 이미지를 전달해 캔버스에서 이미지를 그릴수 있게 하는 메소드.
        my_canvas.setBitmapImg(resized_img);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 뷰를 비트맵으로 변환ㅎ하는 코드
                TouchDrawImgCanvas view = (TouchDrawImgCanvas)findViewById(R.id.img_canvas);
                view.setDrawingCacheEnabled(true);
                view.buildDrawingCache();
                save_img = view.getDrawingCache();


                str_save_img = getBase64String(save_img);

                str_save_imgs.add(str_save_img);

                setStringArrayPref(getApplicationContext(), HIGHLIGHTED_IMGS_ARRAYLIST, str_save_imgs);

                Toast.makeText(getApplicationContext(), "저장 성공", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), SavedPassageListsActivity.class);
                // 바로 저장된 문장들 액티비티로 넘어가고 이미지 저장하기까지 액티비티들은 없애기 위해 플래그 사용
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        initialize_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my_canvas.initialization();
            }
        });

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
    private void setStringArrayPref(Context context, String key, ArrayList<String> values) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.apply();
    }

    // 제이슨을 어레이리스트로 변환하는 메소드
    private ArrayList<String> getStringArrayPref(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList<String> urls = new ArrayList<String>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }


}
