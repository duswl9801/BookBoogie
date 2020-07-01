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
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class SavedPassageListsActivity extends AppCompatActivity {

    // 쉐어드 프리퍼런스 불러올때 key로 사용할 문자열 변수.
    private static final String HIGHLIGHTED_IMGS_ARRAYLIST = "highlighted_imgs";

    ArrayList<String> str_save_imgs = new ArrayList<>();
    ArrayList<Bitmap> save_imgs = new ArrayList<>();

    // save_imgs에 넣을 비트맵 변수.
    // str_save_imgs의 요소들을 비트맵으로 바꿀때 이 변수에 저장한다.
    Bitmap save_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_passage_list);

        // 저장되어있는 이미지들의 어레이리스트를 불러온다.
        str_save_imgs = getStringArrayPref(getApplicationContext(), HIGHLIGHTED_IMGS_ARRAYLIST);
        for(int i = 0; i < str_save_imgs.size(); i++){

            // 문자열을 다시 비트맵으로 바꾸는 코드
            byte[] decodedByteArray = Base64.decode(str_save_imgs.get(i), Base64.NO_WRAP);
            save_img = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
            save_imgs.add(save_img);

        }

        ImageButton backBtn = findViewById(R.id.backBtn);
        ImageButton add_passage_btn = findViewById(R.id.add_passage_btn);

        RecyclerView recyclerView = findViewById(R.id.recyclerview_passages);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), 1));

        //TODO 어댑터 만들고 지정하기
        final SavedPassageListsAdapter passage_adapter = new SavedPassageListsAdapter(this, save_imgs);
        recyclerView.setAdapter(passage_adapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        add_passage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChoicePassageImgActivity.class);
                startActivity(intent);
            }
        });

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
