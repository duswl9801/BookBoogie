package com.reading_app.bookboogie.unused;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.reading_app.bookboogie.Book;
import com.reading_app.bookboogie.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class OldBookSearchActivity extends AppCompatActivity {

    private static final String TAG = "MyBookSearchActivity";
    private static final String TAG2 = "ActivityCheck";


    // 검색했을때 파싱해온 책들이 들어갈 어레이리스트트
    ArrayList<Book> searched_books = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_book_search);
        setContentView(R.layout.act_book_search);

        Log.i(TAG2, "onCreat");


        ImageButton back_btn = findViewById(R.id.backBtn);

        // 이 서치뷰에서 책을 검색하면 책 정보가 뜨고 책 정보를 가져와서 저장할 수 있음.
        final SearchView book_search_view = findViewById(R.id.search_view);
        book_search_view.setQueryHint("책 검색하기");


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        book_search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            // 검색 버튼이 눌러졌을때 이벤트 처리
            @Override
            public boolean onQueryTextSubmit(final String query) {

//                Toast.makeText(BookSearchActivity.this, "검색 처리됨 : " + query, Toast.LENGTH_SHORT).show();
//                String result = searchBook(query);



                new Thread(){

                    @Override
                    public void run() {
                        try{

                            final String result = searchBook(query);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    // 키보드 내리는 메소드.
                                    book_search_view.clearFocus();

                                    TextView search_result = (TextView) findViewById(R.id.search_result);
                                    search_result.setText(result);

                                }
                            });

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                }.start();

//                book_search_view.clearFocus();

                return true;
            }

            // 검색어 글자가 바뀔때마다 이벤트 처리
            @Override
            public boolean onQueryTextChange(String newText) {

//                Toast.makeText(MainActivity.this, "검색 처리됨 : " + newText, Toast.LENGTH_SHORT).show();
//                searchBook(newText);

                return false;
            }
        });


    }

    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
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



    // 네이버 검색 open api 이용.  책 검색하는 메소드.
    public String searchBook(String search_term) {       // 검색어 search_term
        int title_count = 0; // count가 0일때 먼저 title와 description에 naver 정보가 들어감. 그 정보 없애기 위해서 0일때 title과 description 넘어가려고 사용.
        int description_count = 0; //

        // 네이버에 애플리케이션 등록해서 받은 키 입력.
        final String client_id = "ebzIWyffeC6qLCrTT0Jk";
        final String client_secret = "eJ4GmD8PLx";

        // 결과 저장할 변수.
        StringBuffer string_buffer = new StringBuffer();


        try {

            String book_seatch_text = URLEncoder.encode(search_term, "UTF-8");

            String book_search_apiURL_string = "https://openapi.naver.com/v1/search/book.xml?query=" + book_seatch_text + "&display=20";

            URL book_search_url = new URL(book_search_apiURL_string);
            HttpURLConnection book_search_connection = (HttpURLConnection) book_search_url.openConnection();
            book_search_connection.setRequestMethod("GET");
            book_search_connection.setRequestProperty("X-Naver-Client-Id", client_id);
            book_search_connection.setRequestProperty("X-Naver-Client-Secret", client_secret);

            Log.d(TAG, " url 연결 코드 확인");

            // xml 파싱. 팩도리 만들고 팩토리로 파서 생성(풀 파서 사용)
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();

            String tag;

            parser.setInput(new InputStreamReader(book_search_connection.getInputStream(), "UTF-8"));

            parser.next();

            int event_type = parser.getEventType();

            Book searched_book = new Book();
//            searched_book = null;

            Log.d("Hi", searched_book.getTitle());

            while (event_type != XmlPullParser.END_DOCUMENT) {

//                Log.d(TAG, "while 시작");

//                Log.d(TAG, " while 들어가나 확인 ");

                switch (event_type) {
                    case XmlPullParser.END_DOCUMENT:        // 문서의 끝
//                        Log.d(TAG, "문서 끝");
                        break;

                    case XmlPullParser.START_DOCUMENT:

//                        ApiData api_data = null;
//                        api_data = new ApiData();

//                        ArrayList<String> titles = new ArrayList<>();
//                        titles.add("a");

                        Log.d(TAG, " 문서 시작 ");
                        break;

                    case XmlPullParser.START_TAG:

                        tag = parser.getName();

//                        Log.d(TAG, "태그 시작");

                        switch (tag) {
                            case "display":

                                string_buffer.append("검색 결과는 총 ");
                                parser.next();
                                string_buffer.append(parser.getText());
                                string_buffer.append("개 입니다.");
                                string_buffer.append("\n");
                                string_buffer.append("\n");

                                break;

                            case "item":

                                Log.d(TAG, "item");

//                                Book searched_book = new Book();
//                                searched_book = null;

                                break;

                            case "title":

                                Log.d(TAG, "title");

                                if(title_count == 0) {
                                    title_count++;
                                    break;
                                }
                                else{
                                    string_buffer.append("제목 : ");
                                    parser.next();

                                    string_buffer.append(parser.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                                    // 밑에처럼 코드 작성하면 <b></b>같은 html 태그들이 나옴.
//                            string_buffer.append(xpp.getText());

                                    string_buffer.append("\n");

                                // TODO 자꾸 초기화하라는 메시지 뜨는데 뭔지 모르겠음. 고치기 -> 제일 위에 title이랑 description으로 검색 결과 제외한 다른 거 나옴. 그거때문에 안된 것.
                                searched_book.setTitle(parser.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));

                                Log.d("settitle", searched_book.getTitle());

                                    break;
                                }


                            case "image":

                                Log.d(TAG, "image");

                                string_buffer.append("이미지 : ");
                                parser.next();

                                string_buffer.append(parser.getText());
                                string_buffer.append("\n");

                                searched_book.setImage(parser.getText());

                                break;

                            case "author":

                                Log.d(TAG, "author");

                                string_buffer.append("저자 : ");
                                parser.next();

                                string_buffer.append(parser.getText());
                                string_buffer.append("\n");

                                searched_book.setAuthor(parser.getText());

                                break;

                            case "price":

                                Log.d(TAG, "price");

                                string_buffer.append("가격 : ");
                                parser.next();

                                string_buffer.append(parser.getText());
                                string_buffer.append("\n");

                                searched_book.setPrice(parser.getText());

                                break;


                            case "isbn":

                                Log.d(TAG, "isbn");

                                string_buffer.append("isbn : ");
                                parser.next();

                                string_buffer.append(parser.getText());
                                string_buffer.append("\n");

                                searched_book.setIsbn(parser.getText());

                                break;

                            case "description":

                                Log.d(TAG, "description");

                                parser.next();

                                if(description_count == 0){
                                    description_count++;
                                    break;
                                }
                                else{

                                    string_buffer.append("내용 : ");
                                    string_buffer.append(parser.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                                    string_buffer.append("\n");
                                    string_buffer.append("\n");

                                    searched_book.setDescription(parser.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));

                                    searched_books.add(searched_book);

                                    break;
                                }



                        }
                        break;

                    case XmlPullParser.END_TAG:
//                        Log.d(TAG, "태그 끝");
                        break;
                }

                event_type = parser.next();

            }

//            Log.d(TAG, " while 뒷부분 확인");


        } catch (UnsupportedEncodingException | MalformedURLException e) {
            e.printStackTrace();
            Log.d(TAG, "error : " + e);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        Log.d("ArrayList", String.valueOf(searched_books.size()));

//        return searched_books;

        return string_buffer.toString();
    }

}

