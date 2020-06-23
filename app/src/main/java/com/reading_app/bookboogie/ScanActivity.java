package com.reading_app.bookboogie;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanActivity extends AppCompatActivity  {

    private static final String TAG = "SCAN01" ;
    private int REQUEST_ISBN = 1;

    String isbn = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreat");

//        checkInternetState();

        IntentIntegrator isbn_scanner = new IntentIntegrator(this);
        // 바코드 인식시 소리 끄기
        isbn_scanner.setBeepEnabled(false);
        isbn_scanner.setCaptureActivity(BarcodeReader.class);
        isbn_scanner.initiateScan();
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

    // 바코드 스캐너 실행시키려면 필요함.
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult isbn_result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

//        // int는 에러남. 스캔한 결과가 string형.
//        String re = isbn_result.getContents();
//        int res = isbn_result.getContents();

        if(isbn_result != null){

            if(isbn_result.getContents() == null){
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                finish();

            }else {
                Toast.makeText(this, "Scanned : " + isbn_result.getContents(), Toast.LENGTH_LONG).show();

                // 문자열 isbn에 정보 저장해서 이 값을 ISBNScanActivity에 넘겨줌.
                isbn = isbn_result.getContents();

                Intent isbn_data = new Intent(this, ISBNScanActivity.class);
                isbn_data.putExtra("isbn", isbn);
                setResult(REQUEST_ISBN, isbn_data);
                finish();

            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);

        }

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
