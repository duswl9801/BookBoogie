package com.reading_app.bookboogie;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DirectAddDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_direct_add);

        Button add_btn = findViewById(R.id.book_add_btn);
        Button cancel_btn = findViewById(R.id.cancle_btn);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DirectAddDialogActivity.this);
                builder.setTitle("어디에 저장하시겠습니까?");
                builder.setItems(R.array.book_state, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0) {
//                                Toast.makeText(my_context, "첫번째", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(DirectAddDialogActivity.this, AddReadBookActivity.class);
                            intent.putExtra("Book", new Book());
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intent);
                            finish();
                        }
                        else if(which == 1) {
//                                Toast.makeText(my_context, "두번째", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(DirectAddDialogActivity.this, AddWantReadBookActivity.class);
                            intent.putExtra("Book", new Book());
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intent);
                            finish();

                        }

                    }
                });
                AlertDialog add_book_dialog = builder.create();
                add_book_dialog.show();

            }

        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }


}
