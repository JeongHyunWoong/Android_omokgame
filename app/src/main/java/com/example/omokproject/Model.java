package com.example.omokproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

public class Model {

    /////////////////////////////////////////////////변수 선언
    private int map[][] = new int[13][13]; //맵 배열
    public int White_Stone =1 ;
    public int Black_Stone =2;
    public int turn_count=1;
    private int Black_X;
    private int Black_Y;
    private int White_X;
    private int White_Y;
    private int W_turnitem=1;
    private int B_turnitem=1;
    private int W_timeitem=2;//2=보유중 1=사용대기 및 턴넘어갈시 사용 0= 정지
    private int B_timeitem=2;
    public boolean whitewinstate = false;
    public boolean blackwinstate = false;
    public boolean threadstate=false;
    public int B_time=10;
    public int W_time=10;
    public String W_playername;
    public String B_playername;

    SoundPool soundPool= new SoundPool(5,AudioManager.STREAM_MUSIC,0);
    public int stonesound,clicksound,win,bye ;
    public void loadsound(){
        stonesound = soundPool.load(VersusActivity.context,R.raw.stonesound,1);
        clicksound = soundPool.load(VersusActivity.context,R.raw.stonesound,1);
        win = soundPool.load(VersusActivity.context,R.raw.win,1);
    }
    public void playstonesound() {
        soundPool.play(stonesound,1.0f,1.0f,0,0,1.0f);
    }
    public void playclicksound(){
        soundPool.play(clicksound,1.0f,1.0f,0,0,1.0f);
    }
    public void playwinsound(){
        soundPool.play(win,1.0f,1.0f,0,0,1.0f);
    }

    // SoundPool soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC,0);
//    public void Playstonesound(){
//        int sound= soundPool.load(VersusActivity.context,R.raw.stonesound,1);
//        soundPool.play(sound,1f,1f,0,0,1f);
//    }

    public void setMap(int value,int i,int j){
        this.map[i][j] = value;
    }
    public int getMap(int i,int j){
        return map[i][j];
    }
    public void setBlack_X(int value){ Black_X = value; }
    public void setBlack_Y(int value){ Black_Y = value; }
    public void setWhite_X(int value){ White_X = value; }
    public void setWhite_Y(int value){ White_Y = value; }
    public int getBlack_X(){ return Black_X; }
    public int getBlack_Y(){ return Black_Y; }
    public int getWhite_X(){ return White_X; }
    public int getWhite_Y(){ return White_Y; }
    public int getW_turnitem(){return W_turnitem;}
    public int getB_turnitem(){return B_turnitem;}
    public int getW_timeitem(){return W_timeitem;}
    public int getB_timeitem(){return B_timeitem;}
    public int setW_timeitem(int value){return W_timeitem=value;}
    public int setB_timeitem(int value){return B_timeitem=value;}

    ///////////////////////////////////////////////맵 배열 초기화
    public void initmap() {
        int i, j;
        for (i = 0; i < 13; i++) {
            for (j = 0; j < 13; j++) {
                map[i][j] = 0;
            }
        }
    }
    public boolean black_widthcheck(int a,int b){ //검정돌 가로확인.
        //System.out.println("들어옴");
        int count=0;
        int max_count=0;
        if(map[a][b] == 2){
            int j=b;

            for(int i=(a-5);i<=(a+5);i++){
                if(i>=0&&i<13){
                    if(map[i][j]==2){
                        count++;
                        if(max_count<count){
                            max_count=count;
                        }
                    }
                    else{
                        count=0;
                    }
                }
            }
            //System.out.println("max : " + max_count);
            if(max_count==5) //5목 확인
                return true;
            else if(max_count>=6){ //6목 방지
                return false;
            }
        }
        return false;
    }
    //-------------------------------------------------------------------------------------
    public boolean black_heightcheck(int a,int b){ //검정돌 세로확인.
        int count=0;
        int max_count=0;
        if(map[a][b] == 2){
            int i=a;

            for(int j=(b-5);j<=(b+5);j++){
                if(j>=0&&j<13){
                    if(map[i][j]==2){
                        count++;
                        if(max_count<count){
                            max_count=count;
                        }
                    }
                    else{
                        count=0;
                    }
                }
            }
            //System.out.println("max : " + max_count);
            if(max_count==5) //5목 확인
                return true;
            else if(max_count>=6){ //6목 방지
                return false;
            }
        }
        return false;
    }
    //--------------------------------------------------------------------------------------------------
    public boolean black_leftdiagcheck(int a,int b){//검정돌 좌상우하 대각선
        int count=0;
        int max_count=0;
        if(map[a][b]==2){
            for(int i=-5;i<=5;i++){
                if(a+i>=0&&a+i<13){
                    if(b+i>=0&&b+i<13){
                        if(map[a+i][b+i]==2){
                            count++;
                            if(max_count<count){
                                max_count=count;
                            }
                        }
                        else{
                            count=0;
                        }
                    }
                }
            }
            if(max_count==5){
                return true;
            }
            else if(max_count>=6){
                return false;
            }
        }
        return false;
    }
    //-------------------------------------------------------------------------------------------------
    public boolean black_rightdiagcheck(int a,int b){//검정돌 우상좌하 대각선
        int count=0;
        int max_count=0;
        if(map[a][b]==2){
            for(int i=-5;i<=5;i++){
                if(a-i>=0&&a-i<13){
                    if(b+i>=0&&b+i<13){
                        if(map[a-i][b+i]==2){
                            count++;
                            if(max_count<count){
                                max_count=count;
                            }
                        }
                        else{
                            count=0;
                        }
                    }
                }
            }
            if(max_count==5){
                return true;
            }
            else if(max_count>=6){
                return false;
            }
        }
        return false;
    }
    //--------------------------------------------------------------------------------------------------
    public boolean white_widthcheck(int a,int b){ //백돌 가로확인.
        //System.out.println("들어옴");
        int count=0;
        int max_count=0;
        if(map[a][b] == 1){
            int j=b;

            for(int i=(a-5);i<=(a+5);i++){
                if(i>=0&&i<13){
                    if(map[i][j]==1){
                        count++;
                        if(max_count<count){
                            max_count=count;
                        }
                    }
                    else{
                        count=0;
                    }
                }
            }
            Log.d("CHECK!!!!",""+max_count);
            //System.out.println("max : " + max_count);
            if(max_count==5) //5목 확인
                return true;
            else if(max_count>=6){ //6목 방지
                return false;
            }
        }
        return false;
    }
    //--------------------------------------------------------------------------------------------------
    public boolean white_heightcheck(int a,int b){ //백돌 세로확인
        int count=0;
        int max_count=0;
        if(map[a][b] == 1){
            int i=a;

            for(int j=(b-5);j<=(b+5);j++){
                if(j>=0&&j<13){
                    if(map[i][j]==1){
                        count++;
                        if(max_count<count){
                            max_count=count;
                        }
                    }
                    else{
                        count=0;
                    }
                }
            }
            //System.out.println("max : " + max_count);
            if(max_count==5) //5목 확인
                return true;
            else if(max_count>=6){ //6목 방지
                return false;
            }
        }
        return false;
    }
    //-----------------------------------------------------------------------------------------
    public boolean white_leftdiagcheck(int a,int b){//백돌 좌상우하 대각선
        int count=0;
        int max_count=0;
        if(map[a][b]==1){
            for(int i=-5;i<=5;i++){
                if(a+i>=0&&a+i<13){
                    if(b+i>=0&&b+i<13){
                        if(map[a+i][b+i]==1){
                            count++;
                            if(max_count<count){
                                max_count=count;
                            }
                        }
                        else{
                            count=0;
                        }
                    }
                }
            }
            if(max_count==5){
                return true;
            }
            else if(max_count>=6){
                return false;
            }
        }
        return false;
    }
    //-----------------------------------------------------------------------------------
    public boolean white_rightdiagcheck(int a,int b){//백돌 우상좌하 대각선
        int count=0;
        int max_count=0;
        if(map[a][b]==1){
            for(int i=-5;i<=5;i++){
                if(a-i>=0&&a-i<13){
                    if(b+i>=0&&b+i<13){
                        if(map[a-i][b+i]==1){
                            count++;
                            if(max_count<count){
                                max_count=count;
                            }
                        }
                        else{
                            count=0;
                        }
                    }
                }
            }
            if(max_count==5){
                return true;
            }
            else if(max_count>=6){
                return false;
            }
        }
        return false;
    }
    //------------------------------------------------------------------------
    /*
    public boolean triple_widthcheck(int a,int b){ //검정돌 가로확인.
        //System.out.println("들어옴");
        int count=0;
        int max_count=0;
        if(map[a][b] == 1||map[a][b] ==2){
            int j=b;
            for(int i=(a-4);i<=(a+4);i++){
                if(i>=0&&i<15){
                    if(map[i][j]==1||map[i][j]==2){
                        count++;
                        System.out.println(count);
                        if(max_count<count){
                            max_count=count;
                        }
                    }
                    else{
                        count=0;
                    }
                }
            }
            //System.out.println("max : " + max_count);
            if(max_count==3) //
                return true;
            else if(max_count>=6){ //6목 방지
                return false;
            }
        }
        return false;
    }
    //----------------------------------------------------------------------
    public boolean triple_lengthcheck(int a,int b){ //백돌 세로확인
        int count=0;
        int max_count=0;
        if(map[a][b] ==1||map[a][b] == 2){
            int i=a;
            for(int j=(b-4);j<=(b+4);j++){
                if(j>=0&&j<15){
                    if(map[i][j] ==1||map[i][j]==2){
                        count++;
                        if(max_count<count){
                            max_count=count;
                        }
                    }
                    else{
                        count=0;
                    }
                }
            }
            //System.out.println("max : " + max_count);
            if(max_count==3) //5목 확인
                return true;
            else if(max_count>=6){ //6목 방지
                return false;
            }
        }
        return false;
    }
    //----------------------------------------------------------------------
    public boolean triple_rightdiagcheck(int a,int b){//백돌 우상좌하 대각선
        int count=0;
        int max_count=0;
        if(map[a][b]==1||map[a][b]==2){
            for(int i=-4;i<=4;i++){
                if(a-i>=0&&a-i<15){
                    if(b+i>=0&&b+i<15){
                        if(map[a-i][b+i]==1||map[a-i][b+i]==2){
                            count++;
                            if(max_count<count){
                                max_count=count;
                            }
                        }
                        else{
                            count=0;
                        }
                    }
                }
            }
            if(max_count==3){
                return true;
            }
            else if(max_count>=6){
                return false;
            }
        }
        return false;
    }
    //---------------------------------------------------------
    public boolean triple_leftdiagcheck(int a,int b){//백돌 좌상우하 대각선
        int count=0;
        int max_count=0;
        if(map[a][b]==1||map[a][b]==2){
            for(int i=-4;i<=4;i++){
                if(a+i>=0&&a+i<15){
                    if(b+i>=0&&b+i<15){
                        if(map[a+i][b+i]==1||map[a+i][b+i]==2){
                            count++;
                            if(max_count<count){
                                max_count=count;
                            }
                        }
                        else{
                            count=0;
                        }
                    }
                }
            }
            if(max_count==3){
                return true;
            }
            else if(max_count>=6){
                return false;
            }
        }
        return false;
    }*/
    public void reverseturn(){
        if(turn_count%2==0){
            if(W_turnitem!=0){
                setMap(0,getWhite_X(),getWhite_Y());
                turn_count--;
                W_turnitem--;
            }
        }
        else if(turn_count%2==1){
            if(B_turnitem!=0){
                setMap(0,getBlack_X(),getBlack_Y());
                turn_count--;
                B_turnitem--;

            }
        }
    }
}