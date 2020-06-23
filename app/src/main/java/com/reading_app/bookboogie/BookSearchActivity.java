package com.reading_app.bookboogie;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


public class BookSearchActivity extends AppCompatActivity {

    RecyclerView book_recyclerview;
    BookSearchAdapter book_recycler_adapter;
    // 검색했을때 파싱해온 책들이 들어갈 어레이리스트트
    ArrayList<Book> received_books  = new ArrayList<>();
    int display_num = 0;;
    boolean is_display = false;

    int book_idx;

    static Handler search_result_handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);

        ImageButton back_btn = findViewById(R.id.backBtn);
        final TextView display_num_textview = findViewById(R.id.display_num_textview);

        // 이 서치뷰에서 책을 검색하면 책 정보가 뜨고 책 정보를 가져와서 저장할 수 있음.
        final SearchView book_search_view = findViewById(R.id.search_view);
        book_search_view.setQueryHint("책 검색하기");

        // 핸들러 객체화
        search_result_handler = new Handler();

        checkInternetState();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        book_search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(final String query) {

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

                                        book_recyclerview = findViewById(R.id.recyclerView);
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
}
