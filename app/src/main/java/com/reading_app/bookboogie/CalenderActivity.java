package com.reading_app.bookboogie;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Collections;

public class CalenderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        ImageButton backBtn = findViewById(R.id.backBtn);
        BarChart read_book_barchart = findViewById(R.id.read_books_barchart);

        ArrayList NoOfEmp = new ArrayList();

        // 값 추가. x index와 그에 맞는 수치 넣는 코드.
        // 수치에 따라 그래프 값 범위 자동으로 바뀜.
        NoOfEmp.add(new BarEntry(3, 0));
        NoOfEmp.add(new BarEntry(2, 1));
        NoOfEmp.add(new BarEntry(1, 2));
        NoOfEmp.add(new BarEntry(5, 3));
        NoOfEmp.add(new BarEntry(0, 4));
        NoOfEmp.add(new BarEntry(1, 5));
        NoOfEmp.add(new BarEntry(2, 6));
        NoOfEmp.add(new BarEntry(0, 7));
        NoOfEmp.add(new BarEntry(0, 8));
        NoOfEmp.add(new BarEntry(1, 9));

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
        month.add("11");
        month.add("12");

        /*
        차트로 나타낼 데이터.
        label은 밑에 표시됨.
         */
        BarDataSet bar_data_set = new BarDataSet(NoOfEmp, "읽은 책들의 수");

        /*
          그래프 나타나는 속도 조절하는 코드.
          animateY는 y축으로 위로 올라와지는 메소드고
          animateX는 x축마다 하나씩 나옴
         */
        read_book_barchart.animateY(2000);

        /*
         x축과 데이터 셋을 묶어서 바 데이터에 넣음.
         */
        BarData data = new BarData(month, bar_data_set);

        bar_data_set.setColors(Collections.singletonList(Color.DKGRAY));      // 막대 색 조절하는 코드

        // 막대 그래프에 데이터 넣음.
        read_book_barchart.setData(data);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

}
