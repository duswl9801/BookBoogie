package com.reading_app.bookboogie.unused;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.reading_app.bookboogie.R;

public class AddCategoryDialog extends DialogFragment implements View.OnClickListener {

    private EditText catrgoryName;
    private String inputCategoryName;
    public CategoryDialogListener categoryDialogListener;

    // getActivity()에서 널 반환하는거 오류 해결하기 위해 사용.
    // context를 사용하게 해준다.
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    // 인터페이스 정의
    interface CategoryDialogListener {
        void onPositiveClicked(String categoryName);
    }

    // 호출할 리스너 초기화
    public void setDialogListener(CategoryDialogListener categoryDialogListener) {
        this.categoryDialogListener = categoryDialogListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.old_dialog_add_category, null);
        catrgoryName = (EditText) view.findViewById(R.id.inputCategoryName);

        builder.setView(inflater.inflate(R.layout.old_dialog_add_category, null))


                .setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        inputCategoryName = catrgoryName.getText().toString();
                        Log.i("My Tag", inputCategoryName);
//                       // 여기서 널 들어감.
//                       EditText editText = (EditText)getView().findViewById(R.id.inputCategoryName);
//
//                        String categoryName = editText.getText().toString();
//                        Log.d("categoryName", categoryName);

                        // 인텐트에 카테고리명 넣어서 전달하는 방법도 해보기
//                        Intent intent = new Intent(mContext.getApplicationContext(), ReadBooksCategoriesActivity.class);
//
//                        startActivity(intent);


                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddCategoryDialog.this.getDialog().cancel();
                    }
                });

        return builder.create();

    }

    @Override
    public void onClick(View v) {

    }
}
