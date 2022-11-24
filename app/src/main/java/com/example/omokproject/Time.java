package com.example.omokproject;

import android.content.Context;
import android.content.res.Resources;

public class Time extends Thread implements Runnable{
    Model model;
    Board board;

    Time(Model model, Board board){
        this.model=model;
        this.board=board;
    }
    public void run(){
        super.run();
        while(model.threadstate){
            try{
                sleep(1000);
                if(model.turn_count%2==1) {
                    if(model.W_time==0) {
                        model.turn_count++;
                        model.threadstate = false;
                        model.setB_timeitem(0);
                    }
                    model.W_time--;
                }
                else if(model.turn_count%2==0) {
                    if (model.B_time == 0) {
                        model.turn_count++;
                        model.threadstate = false;
                        model.setW_timeitem(0);
                    }
                    model.B_time--;
                }
                board.postInvalidate();

            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}