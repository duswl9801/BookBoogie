package com.reading_app.bookboogie;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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

public class ISBNScanActivity extends AppCompatActivity {

    private static final String TAG = "SCAN00" ;
    private int REQUEST_ISBN = 1;

    RecyclerView scan_result_recycler;
    BookSearchAdapter scan_recycler_adapter;

    // 검색했을때 파싱해온 책들이 들어갈 어레이리스트트
    ArrayList<Book> received_books  = new ArrayList<>();

    //scanactivity로 데이터 받아오는 인텐트
    Intent data;
//    String isbn_test = null;
    String isbn = null;
    String main_kind; // 어떤 메인인지 저장하는 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isbn_scan);
        Log.i(TAG, "onCreat");

//        checkInternetState();

        ImageButton back_btn = findViewById(R.id.backBtn);
        ImageButton scan_btn = findViewById(R.id.scan_btn);

        Intent main = getIntent();
        main_kind = main.getStringExtra("MainKind");

        data = new Intent(this, ScanActivity.class);
        startActivityForResult(data, REQUEST_ISBN);

//        Log.d(TAG, isbn_test);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                data = new Intent(ISBNScanActivity.this, ScanActivity.class);
                startActivityForResult(data, REQUEST_ISBN);

            }
        });

    }

    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    // 여기서 받아온 isbn 정보로 검색 결과 나타내야 함.
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");

//        checkInternetState();
//        startActivityForResult(data, REQUEST_ISBN);


        scan_result_recycler = findViewById(R.id.recyclerView);
        scan_result_recycler.setLayoutManager(new LinearLayoutManager(this));

        new Thread(){

            @Override
            public void run() {
                try{

                    received_books = searchBook(isbn);

                    if(received_books.size() == 0){
                        Intent intent = new Intent(ISBNScanActivity.this, NoResultDialogActivity.class);
                        intent.putExtra("MainKind", main_kind);
                        startActivity(intent);

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                scan_recycler_adapter = new BookSearchAdapter(ISBNScanActivity.this, received_books);
                                scan_result_recycler.setAdapter(scan_recycler_adapter);

                            }
                        });
                    }



                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }.start();
    }

    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    // 여기서 받아온 isbn 정보로 검색 결과 나타내야 함.
    public void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");



    }

    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    // 바코드 정보를 ScanAcrivity에서 받아서 문자열에 저장하고 이 화면이 보일때 그 결과를 출력함.
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {

            case 1:

                isbn = data.getStringExtra("isbn");
                Log.d(TAG, isbn);

                break;


            default:

                break;

        }

    }

    // 네이버 검색 open api 이용.  책 검색하는 메소드.
    public ArrayList<Book> searchBook(String search_term) {       // 검색어 search_term

        // 네이버에 애플리케이션 등록해서 받은 키 입력.
        final String client_id = "ebzIWyffeC6qLCrTT0Jk";
        final String client_secret = "eJ4GmD8PLx";

        // 검색했을때 파싱해온 책들이 들어갈 어레이리스트트.
        // 검색할 때마다 새로운 어레이리스트들이어야 하므로 함수 안에서 생성함.
        ArrayList<Book> searched_books = new ArrayList<>();

        // 검색 결과 넣을 책 객체 생성.
        Book searched_book = new Book();

        /*
         처음에 api의 태그 title과 description에 책 정보가 아닌 네이버 정보가 들어감.
         이 정보는 책 객체에 넣지 않기 위해 count가 0일때는 title과 description에서 바로 break가 걸림.
         */
        int title_count = 0;
        int description_count = 0;


        try {

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


            while (event_type != XmlPullParser.END_DOCUMENT) {

                switch (event_type) {

                    case XmlPullParser.END_DOCUMENT:        // 문서의 끝
                        break;

                    case XmlPullParser.START_DOCUMENT:      // 문서 시작
                        break;

                    case XmlPullParser.START_TAG:       // 태그 시작

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
                                else {
                                    searched_book.setTitle(parser.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                                    break;
                                }

                            case "image":

                                parser.next();
                                searched_book.setImage(parser.getText());

                                break;

                            case "author":

                                parser.next();
                                searched_book.setAuthor(parser.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));

                                break;

                            case "price":

                                parser.next();
                                searched_book.setPrice(parser.getText());

                                break;

                            case "description":

                                parser.next();

                                if(description_count == 0){
                                    description_count++;
                                    break;
                                }
                                else{
                                    searched_book.setDescription(parser.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                                    // description이 마지막 부분의 tag이기 떄문에 여기서 책 어레이리스트에 추가.
                                    searched_books.add(searched_book);
                                    break;
                                }

                        }
                        break;
                }

                event_type = parser.next();


            }


        } catch (UnsupportedEncodingException | MalformedURLException e) {
            e.printStackTrace();
            Log.d(TAG, "error : " + e);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

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
