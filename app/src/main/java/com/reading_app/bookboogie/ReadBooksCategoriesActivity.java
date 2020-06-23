package com.reading_app.bookboogie;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReadBooksCategoriesActivity extends AppCompatActivity {

    private static final int ADD_BOOK = 1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_books_categories);

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
        final ReadBooksCategoriesAdapter recyclerview_category_adapter = new ReadBooksCategoriesAdapter(this, categories);
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
                Intent intent = new Intent(getApplicationContext(), AddReadBookActivity.class);
//                startActivityForResult(intent, ADD_BOOK);
                startActivity(intent);
            }
        });


        addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ReadBooksCategoriesActivity.this);
                View view = LayoutInflater.from(ReadBooksCategoriesActivity.this).inflate(R.layout.dialog_add_category, null, false);
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

                        /* non-static method notifydatasetchanged() cannot be referenced from a static context 이런 에러메세지 나왔음
                        정적인 context에서 밑의 메소드가 쓰일 수 없다는 뜻. 구글링해서 다른 예제들을 보니 저 메소드를 쓰려면 객체화해서 그 객체에 써야한다는
                        글들이 나왔음. 그리고 밑의 코드를 보자 내가 클래스명을 가지고 이 메소드를 쓰고 있었다는 걸 알았음. 객체명으로 바꾸자 해결됨.
                         */
//                        ReadBooksCategoriesAdapter.notifyDataSetChanged();
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





//                AddCategoryDialog categoryDialog = new AddCategoryDialog();
//
//                // 여기서 리스너 쓰기
//                categoryDialog.show(getSupportFragmentManager(), "");

                // 사용자가 입력한 카테고리명이 전달되는지 알기 위해 로그 찍어봄. -> null 들어가 있음. 받아오지 못했음.
//                EditText editText = (EditText)findViewById(R.id.inputCategoryName);
//                String inputCategoryName = editText.getText().toString();
//                Log.d("categoryName", inputCategoryName);

                // 널포인터 에러. 다이얼로그의 에딧텍스트를 못 불러옴. 다이얼로그 클래스에서 그냥 문자열을 전달 받는 방식으로 해야겠음.
//                EditText editText = categoryDialog.getView().findViewById(R.id.inputCategoryName);
//                String inputCategoryName = editText.getText().toString();
//                Log.d("categoryName", inputCategoryName);





            }
        });

//        void addCategory(){
//
//            final EditText inputCategory = new EditText(this);
//
//            AlertDialog.Builder addCategoryDialog = new AlertDialog.Builder(this);
//
//            addCategoryDialog.setTitle("카테고리 추가");
//            addCategoryDialog.setMessage("카테고리 이름을 입력해주세요.");
//            addCategoryDialog.setView(inputCategory);
//            addCategoryDialog.setPositiveButton("입력", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Toast.makeText(getApplicationContext(),inputCategory.getText().toString() , Toast.LENGTH_LONG).show();
//                }
//            });
//            addCategoryDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                }
//            });
//            addCategoryDialog.show();
//        }

    }


//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == ADD_BOOK) {
//            Intent intent = getIntent();
//            Book book = (Book)intent.getSerializableExtra("book");
//
//            Toast.makeText(this, book.title, Toast.LENGTH_LONG);
//
//        }
//    }

}
