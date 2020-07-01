package com.reading_app.bookboogie;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class CalenderActivity extends AppCompatActivity {

    // 오늘 날짜 받기 위한 캘린더 변수
    public Calendar calender = Calendar.getInstance();

    // 오늘 년도를 넣기 위한 변수
    int today_year;
    //  사용자가 다른 해의 읽은 책 개수를 보기 위해. 바꾼 년도를 저장할 변수
    int changed_year;

    // 쉐어드 프리퍼런스에 저장되어 있는 책들 담는 어레이리스트
    ArrayList<Book> read_boooks = new ArrayList<>();

    BarChart read_book_barchart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        ImageButton backBtn = findViewById(R.id.backBtn);
        ImageButton backward_btn = findViewById(R.id.backward_btn);
        final TextView year_textview = findViewById(R.id.year_textview);
        ImageButton forward_btn = findViewById(R.id.forward_btn);
        read_book_barchart = findViewById(R.id.read_books_barchart);

        // 오늘 년도 받아서 텍스트뷰에 나타냄
        today_year = calender.get(Calendar.YEAR);
        year_textview.setText(String.valueOf(today_year));

        // 처음 년도는 오늘의 년도기 때문에 오늘 년도로 초기화 해줌
        changed_year = today_year;

        // 저장된 읽은 책들 불러와서 어레이 리스트에 넣기
        SharedPreferences book_sharedpreference = getSharedPreferences("book_data", MODE_PRIVATE);
        Collection<?> collection = book_sharedpreference.getAll().values();
        Iterator<?> iter = collection.iterator();

        while(iter.hasNext()){
            Gson gson = new Gson();
            String json = (String)iter.next();
            read_boooks.add(gson.fromJson(json, Book.class));
        }

        setChart(today_year, read_boooks);

        backward_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changed_year--;
                year_textview.setText(String.valueOf(changed_year));
                setChart(changed_year, read_boooks);
            }
        });

        forward_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changed_year++;
                year_textview.setText(String.valueOf(changed_year));
                setChart(changed_year, read_boooks);
            }
        });

//        ArrayList NoOfEmp = new ArrayList();
//
//        // 값 추가. x index와 그에 맞는 수치 넣는 코드.
//        // 수치에 따라 그래프 값 범위 자동으로 바뀜.
//        NoOfEmp.add(new BarEntry(3, 0));
//        NoOfEmp.add(new BarEntry(2, 1));
//        NoOfEmp.add(new BarEntry(1, 2));
//        NoOfEmp.add(new BarEntry(5, 3));
//        NoOfEmp.add(new BarEntry(0, 4));
//        NoOfEmp.add(new BarEntry(1, 5));
//        NoOfEmp.add(new BarEntry(2, 6));
//        NoOfEmp.add(new BarEntry(0, 7));
//        NoOfEmp.add(new BarEntry(0, 8));
//        NoOfEmp.add(new BarEntry(1, 9));
//
//        // x축에 달 넣기 위한 어레이리스트
//        ArrayList month = new ArrayList();
//
//        month.add("1");
//        month.add("2");
//        month.add("3");
//        month.add("4");
//        month.add("5");
//        month.add("6");
//        month.add("7");
//        month.add("8");
//        month.add("9");
//        month.add("10");
//        month.add("11");
//        month.add("12");
//
//        /*
//        차트로 나타낼 데이터.
//        label은 밑에 표시됨.
//         */
//        BarDataSet bar_data_set = new BarDataSet(NoOfEmp, "읽은 책들의 수");
//
//        /*
//          그래프 나타나는 속도 조절하는 코드.
//          animateY는 y축으로 위로 올라와지는 메소드고
//          animateX는 x축마다 하나씩 나옴
//         */
//        read_book_barchart.animateY(2000);
//
//        /*
//         x축과 데이터 셋을 묶어서 바 데이터에 넣음.
//         */
//        BarData data = new BarData(month, bar_data_set);
//
//        bar_data_set.setColors(Collections.singletonList(Color.DKGRAY));      // 막대 색 조절하는 코드
//
//        // 막대 그래프에 데이터 넣음.
//        read_book_barchart.setData(data);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    /*
    인자로 주어진 년도, 책들 어레이리스트에 따라서 차트 나타내는 메소드.
    x축은 월을 y축은 해당하는 월에 읽은 책들의 수를 나타낸다.
     */
    public void setChart(int year, ArrayList<Book> read_boooks){

        // 인자로 들어온 년도와 어레이리스트에 있는 책의 년도가 같은 책들만 넣을 어레이리스트.
        ArrayList<Book> same_year_book = new ArrayList<>();

        for(int i = 0; i < read_boooks.size(); i++){
            if(read_boooks.get(i).read_year == year){
                same_year_book.add(read_boooks.get(i));
            }
        }

        /*
        1년의 각 월마다 읽은 책의 수를 저장하기 위한 배열.
        1년이 12개월이기 때문에 배열의 크기 12로 설정.
        예를 들어 read_book_count[0]의 값은 1월에 읽은 책의 수.
         */
        int[] read_book_count = new int[12];
        // 초기화 시켜야 함.
        for(int i = 0; i < 12; i++){
            read_book_count[i] = 0;
        }

        // same_year_book안에 있는 책의 읽은 월을 저장할 변수.
        int read_month = 0;

        for(int i = 0; i < same_year_book.size(); i++){
            read_month = same_year_book.get(i).read_month;
            // index는 0부터 시작하기 때문에 책을 읽은 달에서 1을 뺀 인덱스에 저장해야한다.
            read_book_count[read_month-1]++;
        }

        ArrayList<BarEntry> count_of_book = new ArrayList<>();

        // 값 추가. x index와 그에 맞는 수치 넣는 코드.
        // 수치에 따라 그래프 값 범위 자동으로 바뀜.
        for(int i = 0; i < 12; i++){
            count_of_book.add(new BarEntry(read_book_count[i], i));
        }

//        count_of_book.add(new BarEntry(read_book_count[0], 0));

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
        label은 화면에서 차트 밑에 표시됨.
         */
        BarDataSet bar_data_set = new BarDataSet(count_of_book, "읽은 책들의 수");

         /*
          그래프 나타나는 속도 조절하는 코드.
          빠르게 차트 확인하기위해 1초만에 차트 보여지도록 설정함.
          animateY는 y축으로 위로 올라와지는 메소드고
          animateX는 x축마다 하나씩 나옴
         */
        read_book_barchart.animateY(1000);

        /*
         x축과 데이터 셋을 묶어서 바 데이터에 넣음.
         */
        BarData data = new BarData(month, bar_data_set);

        bar_data_set.setColors(Collections.singletonList(Color.DKGRAY));      // 막대 색 조절하는 코드

        // 막대 그래프에 데이터 넣음.
        read_book_barchart.setData(data);

    }




}
