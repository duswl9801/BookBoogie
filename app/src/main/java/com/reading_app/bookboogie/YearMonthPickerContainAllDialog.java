package com.reading_app.bookboogie;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class YearMonthPickerContainAllDialog extends DialogFragment {

    // 선택할 수 있는 년도의 최대 최소.
    private static final int MIN_YEAR = 1980;
    private static final int MAX_YEAR = 2099;

    // 리스너 선언
    private DatePickerDialog.OnDateSetListener listener;

    // 오늘 날짜 받기 위한 캘린더 변수
    public Calendar calender = Calendar.getInstance();

    public void setListener(DatePickerDialog.OnDateSetListener listener){
        this.listener = listener;
    }

    Button all_book_btn;
    Button confirm_btn;
    Button cancel_btn;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View year_month_pick_dialog = inflater.inflate(R.layout.dialog_year_month_pick_contain_all, null);

        all_book_btn = year_month_pick_dialog.findViewById(R.id.all_book_btn);
        confirm_btn = year_month_pick_dialog.findViewById(R.id.confirm_btn);
        cancel_btn = year_month_pick_dialog.findViewById(R.id.cancel_btn);

        final NumberPicker year_picker = (NumberPicker) year_month_pick_dialog.findViewById(R.id.picker_year);
        final NumberPicker month_picker = (NumberPicker) year_month_pick_dialog.findViewById(R.id.picker_month);

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YearMonthPickerContainAllDialog.this.getDialog().cancel();
            }
        });

        all_book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사용자가 선택한 년도, 월 값 받음
                listener.onDateSet(null, year_picker.getValue(), month_picker.getValue(), 0);
                YearMonthPickerContainAllDialog.this.getDialog().cancel();
            }
        });

        // 다이얼로그의 확인 버튼을 누르면 AddReadBookActivity의 리스너가 불려진다. 여기서 year, month값 전달
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사용자가 선택한 년도, 월 값 받음
                listener.onDateSet(null, year_picker.getValue(), month_picker.getValue(), 0);
                YearMonthPickerContainAllDialog.this.getDialog().cancel();
            }
        });

        // 년도 선택 세팅
        int year = calender.get(Calendar.YEAR);
        year_picker.setMinValue(MIN_YEAR);
        year_picker.setMaxValue(MAX_YEAR);
        year_picker.setValue(year);

        // 월 선택 세팅
        month_picker.setMinValue(1);
        month_picker.setMaxValue(12);
        month_picker.setValue(calender.get(Calendar.MONTH) + 1);

        builder.setView(year_month_pick_dialog);

        return builder.create();
    }

}
