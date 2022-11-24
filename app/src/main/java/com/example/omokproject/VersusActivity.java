package com.example.omokproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class VersusActivity extends AppCompatActivity {
    public static Context context;
    Model model;
    Board board;
    SoundPool soundPool= new SoundPool(5, AudioManager.STREAM_MUSIC,0);
    public int bye;
    public void loadsound(){
        bye = soundPool.load(VersusActivity.context,R.raw.bye,1);
    }
    public void playbyesound(){
        soundPool.play(bye,1.0f,1.0f,0,0,1.0f);
    }
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();

        context=this;
        super.onCreate(savedInstanceState);
        model = new Model();
        board = new Board(this,model);
        model.W_playername=intent.getStringExtra("name");
        model.B_playername = intent.getStringExtra("name2");
        board.setOnTouchListener(board);
        setContentView(R.layout.activity_main_me_game);
        FrameLayout blo = (FrameLayout)findViewById(R.id.boardlayout);
        blo.addView(board);
        loadsound();
    }
    public void onBackPressed(){
        //super.onBackPressed();
        Toast.makeText(this, "뒤로가기", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder dial = new AlertDialog.Builder(this);
        dial.setTitle("게임 종료");
        dial.setMessage("게임을 종료하고 메인메뉴로 돌아가시겠습니까?");
        dial.setCancelable(false);
        dial.setPositiveButton("확인",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int WhichButton) {
                playbyesound();
                finish();
                Intent intent = new Intent(getApplicationContext(), MeActivity.class);
                startActivity(intent);
            }
        });
        dial.setNegativeButton("취소",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int WhichButton) {
                dialog.cancel();
            }
        });
        dial.show();
    }

    public void onSurrenderButtonClicked(View v){
        if(model.whitewinstate==false&&model.blackwinstate==false) {
            AlertDialog.Builder dial = new AlertDialog.Builder(this);
            dial.setTitle("항복 선언");
            dial.setMessage("패배를 인정하시겠습니까??");
            dial.setCancelable(false);
            dial.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int WhichButton) {
                    //finish();
                    if (model.turn_count % 2 == 0)
                        model.blackwinstate = true;
                    else if (model.turn_count % 2 == 1)
                        model.whitewinstate = true;
                    board.invalidate();
                }
            });
            dial.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int WhichButton) {
                    dialog.cancel();
                }
            });
            dial.show();
        }
    }
}