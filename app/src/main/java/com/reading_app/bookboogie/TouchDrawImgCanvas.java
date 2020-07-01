package com.reading_app.bookboogie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class TouchDrawImgCanvas extends View {

    // 형광펜 칠 이미지를 저장하는 변수. ImgCanvasActivity에서 setBitmapImg()를 통해 받아온다.
    Bitmap img;

    // 형광펜 색들어가는 변수. 후에 색 변경가능하게 함. 기본 노란색.
    int color = Color.YELLOW;
    // 형광펜 굵기 저장하는 변수. 후에 굵기 변경 가능하게(세 단계로) 함. 기본 50.
    int line_thickness = 50;

    // 사용자가 터치하기 바로 전의 캔버스의 x, y좌표값을 넣는 변수. 좌표는 0부터 시작하기 때문에 -1로 초기화.
    // touchEvent에서 사용.
    int X, Y = -1;

    //그리기 위한 도구로 페인트 객체만들음.
    Paint paint = new Paint();
    // 이미지와 사용자가 긋는 선의 투명도가 다르기 때문에 새로운 객체로 사용.
    Paint img_paint = new Paint();

    //여러가지의 그리기 명령을 모았다가 한번에 출력해주는 버퍼역할을 담당한다.
    Path path = new Path();

    public TouchDrawImgCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        setupDrawing();

        canvas.drawBitmap(img, 0, 0, img_paint);

        canvas.drawPath(path, paint);

    }

    public void setupDrawing(){

        // 페인트 속성 설정.
        paint.setColor(color);      // 선 색 조절
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(line_thickness);       // 선 굵기 조절
        paint.setAntiAlias(true);
        // 0에 가까울 수록 투명하고 255에 가까울 수록 진함
        paint.setAlpha(50);     // 선 투명도 조절

        img_paint.setAlpha(255);

    }

    // 캔버스에 그림 그리는 액티비티에서 초기화 버튼을 누르면 캔버스 초기화 시켜주는 메소드
    public void initialization(){
        path.reset();
        invalidate();
    }

    public void setBitmapImg(Bitmap bitmap){
        img = bitmap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 사용자가 터치했을 때 캔버스위의 x, y좌표값.
        X = (int)event.getX();
        Y = (int)event.getY();

        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:       // 뷰를 누를때
                path.moveTo(X, Y);
                break;

            case MotionEvent.ACTION_MOVE:       // 뷰를 누른 후 움직일 때
                path.lineTo(X, Y);
                break;

            case MotionEvent.ACTION_UP:     // 뷰에서 손가락을 땔 때
                break;
        }

        invalidate();       // 화면을 무효화하고 onDraw()호출함.
        return true;

    }

}
