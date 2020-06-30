package com.reading_app.bookboogie;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class CalenderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        ImageButton backBtn = findViewById(R.id.backBtn);
        BarChart read_book_barchart = findViewById(R.id.read_books_barchart);

        ArrayList NoOfEmp = new ArrayList();

        // 값 추가. x index와 그에 맞는 수치 넣는 코드.
        NoOfEmp.add(new BarEntry(945f, 0));
        NoOfEmp.add(new BarEntry(1040f, 1));
        NoOfEmp.add(new BarEntry(1133f, 2));
        NoOfEmp.add(new BarEntry(1240f, 3));
        NoOfEmp.add(new BarEntry(1369f, 4));
        NoOfEmp.add(new BarEntry(1487f, 5));
        NoOfEmp.add(new BarEntry(1501f, 6));
        NoOfEmp.add(new BarEntry(1645f, 7));
        NoOfEmp.add(new BarEntry(1578f, 8));
        NoOfEmp.add(new BarEntry(1695f, 9));

        // x축에 달 넣기 위한 어레이리스트
        ArrayList month = new ArrayList();

        month.add("1");
        month.add("2");
        month.add("3");
        month.add("4");
        month.add("5");
        month.add("6");
        month.add("7");
        month.add("8");
        month.add("9");
        month.add("10");

        BarDataSet bar_data_set = new BarDataSet(NoOfEmp, "No Od Empoloyee");
        read_book_barchart.animateY(5000);
        BarData data = new BarData(month, bar_data_set);
        bar_data_set.setColors(ColorTemplate.COLORFUL_COLORS);
        read_book_barchart.setData(data);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

}
