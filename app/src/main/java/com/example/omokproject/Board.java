package com.example.omokproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.Toast;

public class Board extends View implements View.OnTouchListener {
    Context context;
    Model model = new Model();
    //Time thread;
    private Bitmap bgimage,playerbar_W,playerbar_B,whiteplayer,blackplayer;
    private Bitmap whitestoneimage, blackstoneimage;
    private Bitmap whitewinimage,blackwinimage;
    private Bitmap reverseimage;

    public Board(Context context, Model model) {
        super(context);
        this.context=context;
        this.model = model;
        model.initmap();//맵 배열 초기화 (0값)
        model.loadsound();
        //setBackgroundColor(Color.WHITE);

        Resources r = context.getResources();
        bgimage = BitmapFactory.decodeResource(r, R.drawable.omokboard);
        bgimage = Bitmap.createScaledBitmap(bgimage,900,900,true);
        whitestoneimage = BitmapFactory.decodeResource(r, R.drawable.whitestone);
        blackstoneimage = BitmapFactory.decodeResource(r, R.drawable.blackstone);
        whitewinimage =BitmapFactory.decodeResource(r,R.drawable.whitewin);
        blackwinimage = BitmapFactory.decodeResource(r,R.drawable.blackwin);
        whitestoneimage = Bitmap.createScaledBitmap(whitestoneimage, 63, 63, false);
        blackstoneimage = Bitmap.createScaledBitmap(blackstoneimage, 63, 63, false);
        whitewinimage = Bitmap.createScaledBitmap(whitewinimage,700,700,false);
        blackwinimage = Bitmap.createScaledBitmap(blackwinimage,700,700,false);
        reverseimage=BitmapFactory.decodeResource(r,R.drawable.reverseimage);
        reverseimage=Bitmap.createScaledBitmap(reverseimage,420,140,false);
        playerbar_B = BitmapFactory.decodeResource(r,R.drawable.playerbar_black);
        playerbar_B=Bitmap.createScaledBitmap(playerbar_B,980,140,false);
        playerbar_W = BitmapFactory.decodeResource(r,R.drawable.playerbar_white);
        playerbar_W=Bitmap.createScaledBitmap(playerbar_W,980,140,false);
        whiteplayer = BitmapFactory.decodeResource(r,R.drawable.whiteplayer);
        whiteplayer=Bitmap.createScaledBitmap(whiteplayer,280,105,false);
        blackplayer = BitmapFactory.decodeResource(r,R.drawable.blackplayer);
        blackplayer=Bitmap.createScaledBitmap(blackplayer,280,105,false);
        //image.setWidth();
           }

    public void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        /////////////////////////////////////////////배둑판 배경그리기
        canvas.drawBitmap(bgimage, 900, 900, null);
        ////////////////////////////////////////////플레이어 바 그리기
        if (model.turn_count % 2 == 1) {
            canvas.drawBitmap(playerbar_W, 35, 945, null);
        } else if (model.turn_count % 2 == 0) {
            canvas.drawBitmap(playerbar_B, 35, 945, null);
        }
        paint.setTextSize(42); //흑,백 닉네임
        canvas.drawText(""+model.W_playername, 320, 1029, paint);
        canvas.drawText(""+model.B_playername, 620, 1029, paint);

        canvas.drawBitmap(whitestoneimage, 431, 987, null);
        canvas.drawBitmap(blackstoneimage, 550, 987, null);
        //canvas.drawText(String.valueOf(bgimage.getWidth()), 50, 50, paint);
        //canvas.drawText(String.valueOf(bgimage.getHeight()), 50, 70, paint);
        paint.setColor(Color.BLACK);
        //canvas.drawCircle(0, 0, 10, paint);
        //canvas.drawRect(10, 20, 30, 40, paint);
        //canvas.drawLine(150,100,1350,100,paint);

        ///////////////////////////////////////////////바둑판 선 그리기
        paint.setStrokeWidth(3);//선 굵기
        for (int i = 0; i < 13; i++) {
            canvas.drawLine(105, (i + 1) * 70, 945, (i + 1) * 70, paint);
            canvas.drawLine(((i + 1) * 70) + 35, 70, ((i + 1) * 70) + 35, 910, paint);
            //바둑판 시작x점 150,시작y점 100 , 바둑판(노란색) 크기 1200*1200 , 가로세로 12*12
        }

        //////////////////////////////////////////////화점찍기
        canvas.drawCircle(525, 490, 10, paint); //중앙
        canvas.drawCircle(315, 280, 10, paint);
        canvas.drawCircle(735, 280, 10, paint);
        canvas.drawCircle(315, 700, 10, paint);
        canvas.drawCircle(735, 700, 10, paint);

        /////////////////////////////////////////////돌 그리기
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
                if (model.getMap(i, j) == 1) {
                    //canvas.drawCircle(i*100+150,j*100+100,50,paint); //배열값에 따라 돌그려넣기
                    canvas.drawBitmap(whitestoneimage, i * 70 + 74, j * 70 + 40, paint);
                } else if (model.getMap(i, j) == 2) {
                    canvas.drawBitmap(blackstoneimage, i * 70 + 74, j * 70 + 40, paint);
                }
            }
        }
        ////////////////////////////////////////////최근 돌 표식찍기
        paint.setColor(Color.RED);
        if (model.turn_count != 1) { //시작을 제외하고
            if (model.turn_count % 2 == 1) //검은돌을 놓고 흰색턴이 되었을 때
                canvas.drawCircle(model.getBlack_X() * 70 + 105, model.getBlack_Y() * 70 + 70, 10, paint);
            else if (model.turn_count % 2 == 0) { //흰돌을 놓고 검은색턴이 되었을 때
                canvas.drawCircle(model.getWhite_X() * 70 + 105, model.getWhite_Y() * 70 + 70, 10, paint);
            }
        }
        ////////////////////////////////////////////승리 도장
        if (model.whitewinstate == true) {
                canvas.drawBitmap(whitewinimage, 175, 140, paint);
            model.playwinsound();
        }
        if (model.blackwinstate == true){
            canvas.drawBitmap(blackwinimage, 175, 140, paint);
            model.playwinsound();
        }

        ///////////////////////////////////////////아이템 그리기
        canvas.drawBitmap(reverseimage,316,1080,paint);
        //////////////////////////////////////////남은 시간 그리기
        super.onDraw(canvas);
    }

    public boolean onTouch(View v, MotionEvent event) {

        float X = event.getX();
        float Y = event.getY();
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
                if ((X > (70 + i * 70) && X < 70 + (i + 1) * 70) && (Y > (35 + j * 70) && Y < (35 + (j + 1) * 70))) {   // 좌표임계 범위에 배열값에 값넣기
                    if (model.turn_count % 2 == 1) { //백돌의 차례일 때
                        if (model.getMap(i, j) == 0&&model.blackwinstate==false&&model.whitewinstate==false) { //빈칸이고 상대가 승리하지 않았을때
                            model.setMap(1, i, j);
                            model.setWhite_X(i);
                            model.setWhite_Y(j);
                            model.playstonesound();
                            model.turn_count++;
                            if(model.getW_timeitem()==1){
                                Time th = new Time(model,this);
                                model.threadstate=true;
                                th.start();
                            }
                            if(model.getB_timeitem()==1){
                                model.threadstate=false;
                                model.W_time=10;
                                model.setB_timeitem(0);
                            }
                            invalidate();
                        }
                    } else if (model.turn_count % 2 == 0) {
                        if (model.getMap(i, j) == 0&&model.whitewinstate==false&&model.blackwinstate==false) {
                            model.setMap(2, i, j);
                            model.setBlack_X(i);
                            model.setBlack_Y(j);
                            model.playstonesound();
                            model.turn_count++;
                            if(model.getB_timeitem()==1){
                                Time th = new Time(model,this);
                                model.threadstate=true;
                                th.start();
                            }
                            if(model.getW_timeitem()==1){
                                model.threadstate=false;
                                model.B_time=10;
                                model.setW_timeitem(0);
                            }
                            invalidate();
                        }
                    }
                }
            }
        }
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
                if (model.white_widthcheck(i, j)==true||model.white_heightcheck(i,j)==true||model.white_leftdiagcheck(i,j)==true||model.white_rightdiagcheck(i,j)==true) {
                    model.whitewinstate = true;
                    invalidate();
                }
                if (model.black_widthcheck(i, j)==true||model.black_heightcheck(i,j)==true||model.black_leftdiagcheck(i,j)==true||model.black_rightdiagcheck(i,j)==true) {
                    model.blackwinstate = true;
                    invalidate();
                }
            }
        }

        if((X>300&&X<650)&&(Y>1100&&Y<1350)) {
            if(model.whitewinstate==false&&model.blackwinstate==false) {
                model.reverseturn();
                model.playclicksound();
                invalidate();
            }
        }
        return false;
    }
}