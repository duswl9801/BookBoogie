package com.reading_app.bookboogie;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class BarcodeReader extends AppCompatActivity implements  DecoratedBarcodeView.TorchListener {

    private CaptureManager manager;
    private boolean isFlashOn = false;// 플래시가 켜져 있는지

    private Button btFlash;
    private DecoratedBarcodeView barcodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode_reader);

        barcodeView = findViewById(R.id.db_qr);

        manager = new CaptureManager(this,barcodeView);
        manager.initializeFromIntent(getIntent(),savedInstanceState);
        manager.decode();

        btFlash = findViewById(R.id.bt_flash);
        btFlash.setText("플래시 켜기");
        btFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFlashOn){
                    onTorchOff();
                }else{
                    onTorchOn();
                }
            }
        });
    }


    @Override
    public void onTorchOn() {
        barcodeView.setTorchOn();
        btFlash.setText("플래시 끄기");
        isFlashOn = true;
    }

    @Override
    public void onTorchOff() {
        barcodeView.setTorchOff();
        btFlash.setText("플래시 켜기");
        isFlashOn = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        manager.onSaveInstanceState(outState);
    }

}
