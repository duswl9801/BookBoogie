package com.reading_app.bookboogie;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class BookSearchActivity extends AppCompatActivity {

    // 베스트 셀러 뷰들 담는 변수. onResume()단계에서 객체화 함.
    // 검색하기 전 국내, 국외 베스트 셀러 보여주는 화면
    ConstraintLayout best_seller_container;
    ImageView domestic_book_img;
    TextView domestic_book_title;
    TextView domestic_book_description;
    ImageView overseas_book_img;
    TextView overseas_book_title;
    TextView overseas_book_description;

    // 검색 후 검색 결과 보여주는 화면
    RecyclerView book_recyclerview;
    BookSearchAdapter book_recycler_adapter;
    // 검색했을때 파싱해온 책들이 들어갈 어레이리스트트
    ArrayList<Book> received_books  = new ArrayList<>();

    // 국내, 국외 베스트 셀러들을 담을 배열. 5위까지만 가져와서 크기는 5로 설정.
    Book[] domestic_best_sellers = new Book[5];
    Book[] overseas_best_sellers = new Book[5];

    static Handler domestic_handler;
    static Handler overseas_handler;

    int display_num = 0;;
    boolean is_display = false;

    int book_idx;

    int domestic_indx = 0;
    int overseas_indx = 0;

    static Handler search_result_handler;

    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);

        checkInternetState();       // 네트워크 연결 확인

        // 베스트 셀러 책들 받아옴
        Thread best_seller = new Thread() {
            @Override
            public void run() {

                Log.d("book_result", "Thread check");

                domestic_best_sellers = getDomesticBestSellers();
                overseas_best_sellers = getOverseasBestSellers();

            }
        };

        best_seller.start();
        try {
            best_seller.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d("book_result", domestic_best_sellers[0].getTitle());
        Log.d("book_result", overseas_best_sellers[0].getTitle());

        // 상단 뷰들 객체화
        ImageButton back_btn = findViewById(R.id.backBtn);
        final TextView display_num_textview = findViewById(R.id.display_num_textview);
        // 이 서치뷰에서 책을 검색하면 책 정보가 뜨고 책 정보를 가져와서 저장할 수 있음.
        final SearchView book_search_view = findViewById(R.id.search_view);
        book_search_view.setQueryHint("책 검색하기");

//        // 베스트 셀러 뷰들 객체화
//        best_seller_container = findViewById(R.id.best_seller_container);
//        final ImageView domestic_book_img = findViewById(R.id.domestic_book_img);
//        final TextView domestic_book_title = findViewById(R.id.domestic_book_title);
//        final TextView domestic_book_description = findViewById(R.id.domestic_book_description);
//        final ImageView overseas_book_img = findViewById(R.id.overseas_book_img);
//        final TextView overseas_book_title = findViewById(R.id.overseas_book_title);
//        final TextView overseas_book_description = findViewById(R.id.overseas_book_description);
//
////        Thread domestic_bestseller = new Thread(new DomesticBestSeller(domestic_best_sellers));
////        domestic_bestseller.start();
////
////        Thread overseas_bestseller = new Thread(new OverseasBestSeller(overseas_best_sellers));
////        overseas_bestseller.start();
//
//        domestic_handler = new Handler();
//        overseas_handler = new Handler();
//
//        // 베스트 셀러 나타내는 스레드
//        new Thread(){
//            @Override
//            public void run() {
//
//                domestic_handler.post(new Runnable() {
//
//                    @Override
//                    public void run() {
//
//                        for(int i = 0; i < 5; i++){
//
//                            Uri book_uri = Uri.parse(domestic_best_sellers[i].getImage());
//                            Glide.with(BookSearchActivity.this).load(book_uri).into(domestic_book_img);
//                            domestic_book_title.setText(domestic_best_sellers[i].getTitle());
//                            domestic_book_description.setText(domestic_best_sellers[i].getDescription());
//
//                            Log.d("thread_check", domestic_best_sellers[i].getTitle());
//
//                            try {
//                                Thread.sleep(2000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//
//                            if(i==4){
//                                i = 0;
//                            }
//
//                        }
//
//                    }
//
//                });
//
//            }
//        }.start();
//
//        new Thread(){
//            @Override
//            public void run() {
//
//                overseas_handler.post(new Runnable() {
//
//                    @Override
//                    public void run() {
//
//                        for(int i = 0; i < 5; i++){
//
//                            Uri book_uri = Uri.parse(overseas_best_sellers[i].getImage());
//                            Glide.with(BookSearchActivity.this).load(book_uri).into(overseas_book_img);
//                            overseas_book_title.setText(overseas_best_sellers[i].getTitle());
//                            overseas_book_description.setText(overseas_best_sellers[i].getDescription());
//
//                            try {
//                                Thread.sleep(2000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//
//                            if(i==4){
//                                i = 0;
//                            }
//
//                        }
//
//                    }
//
//                });
//
//            }
//        }.start();

        // 리사이클러뷰 객체화
        book_recyclerview = findViewById(R.id.recyclerView);

        // 핸들러 객체화
        search_result_handler = new Handler();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();

                onBackPressed();
            }
        });

        book_search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(final String query) {      // 검색 버튼 눌렀을때

                timer.cancel();

                best_seller_container.setVisibility(View.GONE);
                book_recyclerview.setVisibility(View.VISIBLE);

                new Thread(){
                    @Override
                    public void run() {
                        try{

                            // 검색된 책들 넣기. 이 메소드에서 인터넷 연결하고 네이버 검색 결과 반환함.
                            received_books = searchBook(query);

                            if(received_books.size() == 0){

                                search_result_handler.post(new Runnable() {
                                    @Override
                                    public void run() {
//                                        AlertDialog.Builder builder = new AlertDialog.Builder(BookSearchActivity.this);
//                                        builder.setTitle("책을 직접 추가하시겠습니까?");
//
//                                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                Intent intent = new Intent(BookSearchActivity.this, AddReadBookActivity.class);
//                                                intent.putExtra("Book", new Book());
//                                                startActivity(intent);
//                                            }
//                                        });
//
//                                        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                dialog.dismiss();
//                                            }
//                                        });
//
//                                        AlertDialog direct_book_add = builder.create();

                                        display_num_textview.setText("검색 결과가 없습니다.");

                                        book_search_view.clearFocus();

                                        Intent intent = new Intent(BookSearchActivity.this, DirectAddDialogActivity.class);
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                        startActivity(intent);

                                    }
                                              });


                            }else{
//                                Message msg = search_result_handler.obtainMessage();
//                                search_result_handler.sendMessage(msg.obj(received_books));

//                                Message msg = Message.obtain();
//                                msg.obj = received_books;
//                                search_result_handler.sendMessage(msg);

                                search_result_handler.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        // 키보드 내리는 메소드.
                                        book_search_view.clearFocus();

                                        display_num_textview.setText("총 " + received_books.size() + "개의 검색 결과가 있습니다.");


                                        book_recyclerview.setLayoutManager(new LinearLayoutManager(BookSearchActivity.this));

                                        book_recycler_adapter = new BookSearchAdapter(BookSearchActivity.this, received_books);

                                        book_recyclerview.setAdapter(book_recycler_adapter);

                                    }
                                });

                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }.start();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });

//        domestic_handler = new Handler(){
//            @Override
//            public void handleMessage(Message msg){
//
//                Book domestic_book = (Book) msg.obj;
//
//                if(msg.what == 0){
//                    Uri book_uri = Uri.parse(domestic_book.getImage());
//                    Glide.with(BookSearchActivity.this).load(book_uri).into(domestic_book_img);
//                    domestic_book_title.setText(domestic_book.getTitle());
//                    domestic_book_description.setText(domestic_book.getDescription());
//
//                }
//
////                Uri book_uri = Uri.parse(domestic_book.getImage());
////                Glide.with(BookSearchActivity.this).load(book_uri).into(domestic_book_img);
////                domestic_book_title.setText(domestic_book.getTitle());
////                domestic_book_description.setText(domestic_book.getDescription());
//
//            }
//        };
//
//        overseas_handler = new Handler(){
//            @Override
//            public void handleMessage(Message msg){
//
//                Book overseas_book = (Book) msg.obj;
//
//                if(msg.what == 0){
//                    Uri book_uri = Uri.parse(overseas_book.getImage());
//                    Glide.with(BookSearchActivity.this).load(book_uri).into(overseas_book_img);
//                    overseas_book_title.setText(overseas_book.getTitle());
//                    overseas_book_description.setText(overseas_book.getDescription());
//                }
//
//            }
//        };
//            @Override
//            public void handleMessage(Message msg){
//        // 스레드 안에서 책 검색 결과가 나오면 그 결과를 리사이클러뷰에 붙여줄 핸들러.
//        search_result_handler = new Handler(){
//
//                // 키보드 내리는 메소드.
//                book_search_view.clearFocus();
//
//                book_recyclerview = findViewById(R.id.recyclerView);
//                book_recyclerview.setLayoutManager(new LinearLayoutManager(BookSearchActivity.this));
//
////                book_recycler_adapter = new BookSearchAdapter(BookSearchActivity.this, received_books);
//                book_recycler_adapter = new BookSearchAdapter(BookSearchActivity.this, msg);
//
//                book_recyclerview.setAdapter(book_recycler_adapter);
//            }
//        };

    }

    public void onBackPressed(){
        super.onBackPressed();
        timer.cancel();
    }

    // 여기서 베스트 셀러 2초마다 바뀌는 스레드 써야함. creat에서 쓰면 이 액티비티의 화면이 나오지 않음.
    public void onResume() {
        super.onResume();

        // 베스트 셀러 뷰들 객체화
        best_seller_container = findViewById(R.id.best_seller_container);
        domestic_book_img = findViewById(R.id.domestic_book_img);
        domestic_book_title = findViewById(R.id.domestic_book_title);
        domestic_book_description = findViewById(R.id.domestic_book_description);
        overseas_book_img = findViewById(R.id.overseas_book_img);
        overseas_book_title = findViewById(R.id.overseas_book_title);
        overseas_book_description = findViewById(R.id.overseas_book_description);

        domestic_handler = new Handler();
        overseas_handler = new Handler();

        timer = new Timer();

        TimerTask DomesticTimer = new TimerTask() {
            @Override
            public void run() {
                domestic_handler.post(new Runnable() {

                    @Override
                    public void run() {

//                        for(int i = 0; i < 5; i++){

                            Uri book_uri = Uri.parse(domestic_best_sellers[domestic_indx].getImage());
                            Glide.with(BookSearchActivity.this).load(book_uri).into(domestic_book_img);
                            domestic_book_title.setText(domestic_best_sellers[domestic_indx].getTitle());
                            domestic_book_description.setText(domestic_best_sellers[domestic_indx].getDescription());

                            Log.d("thread_check", domestic_best_sellers[domestic_indx].getTitle());

//                            try {
//                                Thread.sleep(2000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }

//                            if(i==4){
//                                i = 0;
//                            }

                        domestic_indx++;

                        if(domestic_indx == 4){
                            domestic_indx = 0;
                        }

//                        }

                    }

                });
            }
        };

        TimerTask OverseasTimer = new TimerTask() {
            @Override
            public void run() {
                overseas_handler.post(new Runnable() {

                    @Override
                    public void run() {
//                        while (true){
//                        for(int i = 0; i < 5; i++){
//                            int i = 0;

                            Uri book_uri = Uri.parse(overseas_best_sellers[overseas_indx].getImage());
                            Glide.with(BookSearchActivity.this).load(book_uri).into(overseas_book_img);
                            overseas_book_title.setText(overseas_best_sellers[overseas_indx].getTitle());
                            overseas_book_description.setText(overseas_best_sellers[overseas_indx].getDescription());

//                            try {
//                                Thread.sleep(2000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }

//                            if(i==4){
//                                i = 0;
//                            }
                            overseas_indx++;

                            if(overseas_indx == 4){
                                overseas_indx=0;
                            }

//                        }

                    }

                });
            }
        };


        timer.schedule(DomesticTimer, 0, 2000);
        timer.schedule(OverseasTimer, 0, 2000);



//        // 베스트 셀러 나타내는 스레드
//        new Thread(){
//            @Override
//            public void run() {
//
//                domestic_handler.post(new Runnable() {
//
//                    @Override
//                    public void run() {
//
//                        for(int i = 0; i < 5; i++){
//
//                            Uri book_uri = Uri.parse(domestic_best_sellers[i].getImage());
//                            Glide.with(BookSearchActivity.this).load(book_uri).into(domestic_book_img);
//                            domestic_book_title.setText(domestic_best_sellers[i].getTitle());
//                            domestic_book_description.setText(domestic_best_sellers[i].getDescription());
//
//                            Log.d("thread_check", domestic_best_sellers[i].getTitle());
//
//                            try {
//                                Thread.sleep(2000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//
//                            if(i==4){
//                                i = 0;
//                            }
//
//                        }
//
//                    }
//
//                });
//
//            }
//        }.start();
//
//        new Thread(){
//            @Override
//            public void run() {
//
//                overseas_handler.post(new Runnable() {
//
//                    @Override
//                    public void run() {
//
//                        for(int i = 0; i < 5; i++){
//
//                            Uri book_uri = Uri.parse(overseas_best_sellers[i].getImage());
//                            Glide.with(BookSearchActivity.this).load(book_uri).into(overseas_book_img);
//                            overseas_book_title.setText(overseas_best_sellers[i].getTitle());
//                            overseas_book_description.setText(overseas_best_sellers[i].getDescription());
//
//                            try {
//                                Thread.sleep(2000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//
//                            if(i==4){
//                                i = 0;
//                            }
//
//                        }
//
//                    }
//
//                });
//
//            }
//        }.start();

    }

    public void onPause() {
        super.onPause();

        timer.cancel();
    }

    // 네이버 검색 open api 이용. 책 검색하는 메소드.
    public ArrayList<Book> searchBook(String search_term){ // 검색어 search_term

        // 검색했을때 파싱해온 책들이 들어갈 어레이리스트트.
        // 검색할 때마다 새로운 어레이리스트들이어야 하므로 함수 안에서 생성함.
        ArrayList<Book> searched_books = new ArrayList<>();
        // 검색 결과 넣을 책 객체 생성.
//        Book searched_book = new Book();

        book_idx = 0;

        /*
         처음에 api의 태그 title과 description에 책 정보가 아닌 네이버 정보가 들어감.
         이 정보는 책 객체에 넣지 않기 위해 count가 0일때는 title과 description에서 바로 break가 걸림.
         */
        int title_count = 0;
        int description_count = 0;

        // 네이버에 애플리케이션 등록해서 받은 키 입력.
        final String client_id = "ebzIWyffeC6qLCrTT0Jk";
        final String client_secret = "eJ4GmD8PLx";

        try {
            // http 연결 요청하기.
            String book_seatch_text = URLEncoder.encode(search_term, "UTF-8");

            String book_search_apiURL_string = "https://openapi.naver.com/v1/search/book.xml?query=" + book_seatch_text + "&display=20";

            URL book_search_url = new URL(book_search_apiURL_string);
            HttpURLConnection book_search_connection = (HttpURLConnection) book_search_url.openConnection();
            book_search_connection.setRequestMethod("GET");
            book_search_connection.setRequestProperty("X-Naver-Client-Id", client_id);
            book_search_connection.setRequestProperty("X-Naver-Client-Secret", client_secret);

            // xml 파싱. 팩도리 만들고 팩토리로 파서 생성(풀 파서 사용)
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();

            String tag;

            parser.setInput(new InputStreamReader(book_search_connection.getInputStream(), "UTF-8"));

            parser.next();

            int event_type = parser.getEventType();

            Log.d("first display num", String.valueOf(display_num));

            while (is_display != true) {

                switch (event_type) {
                    case XmlPullParser.START_DOCUMENT:  // 문서 시작
                        break;

                    case XmlPullParser.START_TAG:      // 태그 시작

                        tag = parser.getName();
                        switch (tag) {
                            case "display":

                                parser.next();
                                display_num = Integer.parseInt(parser.getText());

                                is_display = true;

                                break;
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        break;

                }
                event_type = parser.next();
            }

            Book[] searched_book  = new Book[display_num];
            for(int i = 0; i < display_num; i++){
                searched_book[i] = new Book();
            }


            while (event_type != XmlPullParser.END_DOCUMENT) {

                switch (event_type) {
                    case XmlPullParser.START_TAG:      // 태그 시작
                        tag = parser.getName();
                        switch (tag) {
                            case "item":
                                break;
                            case "title":

                                parser.next();

                                if(title_count == 0) {
                                    title_count++;
                                    break;
                                }
                                else{
                                    searched_book[book_idx].setTitle(parser.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                                    break;
                                }

                            case "image":

                                parser.next();
                                searched_book[book_idx].setImage(parser.getText());

                                Log.d("imageuri", searched_book[book_idx].getImage().toString());

                                break;
                            case "author":

                                parser.next();
                                searched_book[book_idx].setAuthor(parser.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));

                                break;
                            case "price":

                                parser.next();
                                searched_book[book_idx].setPrice(parser.getText());

                                break;
                            case "description":

                                parser.next();

                                if(description_count == 0){
                                    description_count++;
                                    break;
                                }
                                else{
                                    searched_book[book_idx].setDescription(parser.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                                    // description이 마지막 부분의 tag이기 떄문에 여기서 책 어레이리스트에 추가.
//                                    searched_books.add(searched_book);
                                    book_idx++;
                                    break;
                                }


                        }
                        break;

                    case XmlPullParser.END_TAG:
                        break;

                }
                event_type = parser.next();
            }

            for(int i =0; i < display_num; i++){
                searched_books.add(searched_book[i]);
            }

        } catch (UnsupportedEncodingException | MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        // 다시 false로 만들어야 다음 검색때 display_num을 받음
        is_display = false;

        return searched_books;
    }

    // 인터넷 연결 상태 확인하는 메소드. 연결되어 있으면 다이얼로그가 안뜨고 연결되어 있지 않으면 다이얼로그가 뜬다.
    private void checkInternetState(){

        ConnectivityManager connectivity_manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        assert connectivity_manager != null;
        // ()괄호 안은 인터넷과 연결되었다는 뜻 앞에 !가 있으므로 인터넷과 연결 되지 않았을때를 말함.
        if(!(connectivity_manager.getActiveNetworkInfo() != null && connectivity_manager.getActiveNetworkInfo().isConnected())){
            new AlertDialog.Builder(this)
                    .setMessage("인터넷과 연결되어 있지 않습니다.")
                    .setCancelable(false)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        }
    }

    /*
    인터파크 베스트 셀러 목록 가져오는 메소드.
    국내 5위까지 가져온다.
    스레드 이용해서 2초마다 다음 순위 책 보이도록 함.
     */
    public Book[] getDomesticBestSellers(){

        Log.d("api_check", "베스트 셀러 가져오는 메소드 실행");

        Book[] domestic = new Book[5];
        // 객체 초기화 꼭 해줘야함.
        for(int i = 0; i < 5; i++){
            domestic[i] = new Book();
        }

        int title_count = 0;

        // 인터파크에서 제공받은 인증키
        String key = "03AF737A433771D0B95020C72A43E0BF8738186BA0DD508B5B95822D490BBB90";

        try{

            Log.d("api_check", "네트워크 연결 시작");

            String domestic_best_seller_url_string = "http://book.interpark.com/api/bestSeller.api?key=" + key + "&categoryId=100";
//            String domestic_best_seller_url_string = "http://book.interpark.com/api/bestSeller.api?key=03AF737A433771D0B95020C72A43E0BF8738186BA0DD508B5B95822D490BBB90&category=100";


            URL domestic_best_seller_url = new URL(domestic_best_seller_url_string);
            HttpURLConnection best_seller_connection = (HttpURLConnection) domestic_best_seller_url.openConnection();
            best_seller_connection.setRequestMethod("GET");

            Log.d("api_check", "네트워크 연결 확인");

            // xml 파싱. 팩도리 만들고 팩토리로 파서 생성(풀 파서 사용)
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();

            String tag;

            parser.setInput(new InputStreamReader(best_seller_connection.getInputStream(), "UTF-8"));

            parser.next();

            int event_type = parser.getEventType();

            Log.d("api_check", "for문 전");

            int i = 0;

            while (event_type != XmlPullParser.END_DOCUMENT){
//            for(int i = 0; i < 5; i++){

                if(i == 5){ // 5위까지의 책만 나타낼 것이므로 5위 맡아면 와일문 돌지 않게 하기 위한 코드.
                    break;
                }

                Log.d("api_check", "for문 들어옴");

                switch (event_type) {

                    case XmlPullParser.START_TAG:      // 태그 시작

                        Log.d("api_check", "switch문 들어옴");

                        tag = parser.getName();
                        switch (tag) {
                            case "title":

                                parser.next();

                                Log.d("api_check", "title태그 들어옴");

                                if(title_count == 0) {

                                    Log.d("api_check", "first title : " + parser.getText());

                                    title_count++;
                                    break;
                                }
                                else{

                                    Log.d("api_check", "settitle 하는 곳 들어옴");

                                    domestic[i].setTitle(parser.getText());
                                    Log.d("api_check", "getTitle확인");
                                    Log.d("api_check", "title : " + domestic[i].getTitle());
                                    break;
                                }

                            case "description":

                                parser.next();
                                domestic[i].setDescription(parser.getText());
                                Log.d("api_check", "title : " + domestic[i].getDescription());

                                break;

                            case "coverLargeUrl":

                                parser.next();
                                domestic[i].setImage(parser.getText());
                                Log.d("api_check", "title : " + domestic[i].getImage());

                                i++;



                                break;

                        }
                        break;
                }
                event_type = parser.next();

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d("api_check", "connection error");
            Log.e("api_check", "connection error");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("api_check", "connection error");
            Log.e("api_check", "connection error");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Log.d("api_check", "connection error");
            Log.e("api_check", "connection error");
        }

        return domestic;
    }

    /*
    인터파크 베스트 셀러 목록 가져오는 메소드.
    국외 5위까지Book[] domestic = new Book[5]; 가져온다.
    스레드 이용해서 2초마다 다음 순위 책 보이도록 함.
     */
    public Book[] getOverseasBestSellers(){

        Book[] overseas = new Book[5];
        // 객체 초기화 꼭 해줘야함.
        for(int i = 0; i < 5; i++){
            overseas[i] = new Book();
        }

        int title_count = 0;

        // 인터파크에서 제공받은 인증키
        String key = "03AF737A433771D0B95020C72A43E0BF8738186BA0DD508B5B95822D490BBB90";

        try{
            String overseas_best_seller_url_string = "http://book.interpark.com/api/bestSeller.api?key=" + key + "&categoryId=200";

            URL overseas_best_seller_url = new URL(overseas_best_seller_url_string);
            HttpURLConnection best_seller_connection = (HttpURLConnection) overseas_best_seller_url.openConnection();
            best_seller_connection.setRequestMethod("GET");

            // xml 파싱. 팩도리 만들고 팩토리로 파서 생성(풀 파서 사용)
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();

            String tag;

            parser.setInput(new InputStreamReader(best_seller_connection.getInputStream(), "UTF-8"));

            parser.next();

            int event_type = parser.getEventType();

            int i = 0;

            while (event_type != XmlPullParser.END_DOCUMENT){
//            for(int i = 0; i < 5; i++){

                if(i == 5){ // 5위까지의 책만 나타낼 것이므로 5위 맡아면 와일문 돌지 않게 하기 위한 코드.
                    break;
                }

                switch (event_type) {
                    case XmlPullParser.START_TAG:      // 태그 시작
                        tag = parser.getName();
                        switch (tag) {
                            case "title":

                                parser.next();

                                if(title_count == 0) {
                                    title_count++;
                                }
                                else{
                                    overseas[i].setTitle(parser.getText());
                                }

                                break;

                            case "description":

                                parser.next();
                                overseas[i].setDescription(parser.getText());

                                break;

                            case "coverLargeUrl":

                                parser.next();
                                overseas[i].setImage(parser.getText());

                                i++;

                                break;

                        }
                        break;
                }
                event_type = parser.next();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        return overseas;
    }

//    public class BestSeller implements Runnable{
//
//        Book[] best_sellers = new Book[5];
//        Handler best_seller_handler = new Handler();
//
//        BestSeller(Book[] best_sellers, Handler best_seller_handler){
//            this.best_sellers = best_sellers;
//            this.best_seller_handler = best_seller_handler;
//        }
//
//        @Override
//        public void run() {
//
//            for(int i = 0; i < 5; i++){
//                try{
//                    // 메세지에 객체 넣기 위한 코드
////                    Message msg = best_seller_handler.obtainMessage();
////                    Message msg = new Message();
//                    Message msg = best_seller_handler.obtainMessage(0, best_sellers[i]);
////                    msg.what = 0;
////                    msg.obj = best_sellers[i];
//
//                    Log.d("msg_check", best_sellers[i].getTitle());
//
//                    best_seller_handler.sendMessage(msg);
//                    Thread.sleep(2000);
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                if(i == 4){
//                    i = 0;
//                }
//            }
//
//        }
//    }

//    public class DomesticBestSeller implements Runnable{
//
//        Book[] best_sellers = new Book[5];
//
//        DomesticBestSeller(Book[] best_sellers){
//            this.best_sellers = best_sellers;
//        }
//
//        @Override
//        public void run() {
//
//            domestic_handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    for(int i = 0; i < 5; i++){
//                        Uri book_uri = Uri.parse(best_sellers[i].getImage());
//                        Glide.with(BookSearchActivity.this).load(book_uri).into(domestic_book_img);
//                    }
//
//                }
//            });
////            for(int i = 0; i < 5; i++){
////                try{
////                    // 메세지에 객체 넣기 위한 코드
//////                    Message msg = best_seller_handler.obtainMessage();
//////                    Message msg = new Message();
////                    Log.d("msg_check", best_sellers[i].getTitle());
////
//////                    Message msg = domestic_handler.obtainMessage();
//////                    Message msg = new Message();
//////                    msg.what = 0;
//////                    msg.obj = best_sellers[i];
////
////                    Log.d("msg_check", best_sellers[i].getTitle());
////
////                    Thread.sleep(2000);
////
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
////
////                if(i == 4){
////                    i = 0;
////                }
////            }
//
//        }
//    }

//    public class OverseasBestSeller implements Runnable{
//
//        Book[] best_sellers = new Book[5];
//
//        OverseasBestSeller(Book[] best_sellers){
//            this.best_sellers = best_sellers;
//        }
//
//        @Override
//        public void run() {
//
//            for(int i = 0; i < 5; i++){
//                try{
//                    // 메세지에 객체 넣기 위한 코드
////                    Message msg = best_seller_handler.obtainMessage();
////                    Message msg = new Message();
//                    Message msg = overseas_handler.obtainMessage(0, best_sellers[i]);
////                    msg.what = 0;
////                    msg.obj = best_sellers[i];
//
//                    Log.d("msg_check", best_sellers[i].getTitle());
//
//                    overseas_handler.sendMessage(msg);
//
//                    Thread.sleep(2000);
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                if(i == 4){
//                    i = 0;
//                }
//            }
//
//        }
//    }


}
