package com.reading_app.bookboogie;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WantReadCategoriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_want_read_books_categories);

        // 카테고리 데이터 리스트 생성.
        final ArrayList<String> categories = new ArrayList<>();
        categories.add(getString(R.string.category_all));
        categories.add(getString(R.string.category_novel));
        categories.add(getString(R.string.category_self_improvement));
        categories.add(getString(R.string.category_art));
        categories.add(getString(R.string.category_philosophy));

        ImageButton backBtn = findViewById(R.id.backBtn);
        ImageButton addBookBtn = findViewById(R.id.addBookBtn);
        ImageButton addCategoryBtn = findViewById(R.id.addCategoryBtn);

        // 리사이클러뷰 객체생성, 레이아웃 매니저 지정
        RecyclerView recyclerView = findViewById(R.id.recyclerview_categories);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), 1));

        // 리사이클러뷰 어댑터 객체 생성, 지정
        final WantReadCategoriesAdapter recyclerview_category_adapter = new WantReadCategoriesAdapter(this, categories);
        recyclerView.setAdapter(recyclerview_category_adapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
                onBackPressed();
            }
        });

        addBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddWantReadBookActivity.class);

                startActivity(intent);
            }
        });

        // 카테고리 추가는 액티비티 아니라 간단한 토스트 레이아웃이나 다른거 공부하고 만들기.
        addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(WantReadCategoriesActivity.this);
                View view = LayoutInflater.from(WantReadCategoriesActivity.this).inflate(R.layout.dialog_add_category, null, false);
                builder.setView(view);

                final EditText category_name = (EditText)view.findViewById(R.id.inputCategoryName);
                final Button category_add_btn = (Button)view.findViewById(R.id.category_add_btn);
                final Button category_cancle_btn = (Button)view.findViewById(R.id.category_cancle_btn);

                final AlertDialog category_dialog = builder.create();

                category_add_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String input_category_name = category_name.getText().toString();

                        categories.add(input_category_name);

                        recyclerview_category_adapter.notifyDataSetChanged();

                        category_dialog.dismiss();


                    }
                });

                category_cancle_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        category_dialog.cancel();
                    }
                });

                category_dialog.show();


            }
        });
    }

}
