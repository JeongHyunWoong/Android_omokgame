package com.example.omokproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MeActivity extends AppCompatActivity {
    Model model = new Model();
    public MediaPlayer mp,mp2;
//    public SoundPool soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC,0);
//    int sound;
//    public void Playbgm(){
//        sound= soundPool.load(this,R.raw.bgm,1);
//        soundPool.play(sound,1f,1f,0,1,1f);
//    }

//    public void Playstartgamesound(){
//        int sound1= soundPool.load(this,R.raw.start,1);
//        int sound2 = soundPool.load(this,R.raw.startdaeguk,2);
//        soundPool.play(sound1,1f,1f,0,0,1f);
//        soundPool.play(sound2,1f,1f,0,0,1f);
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_me);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        mp= MediaPlayer.create(this,R.raw.naruto);
        mp.start();
    }

    public void onVersusButtonClicked(View v) {
        AlertDialog.Builder dial = new AlertDialog.Builder(this);
        AlertDialog.Builder dial2 = new AlertDialog.Builder(this);
        final EditText etext = new EditText(this);
        final EditText etext2 = new EditText(this);
        dial.setTitle("플레이어 이름 입력");
        dial.setMessage("백돌 플레이어");
        dial.setView(etext);
        dial2.setTitle("플레이어 이름 입력");
        dial2.setMessage("흑돌 플레이어");
        dial2.setView(etext2);

        dial.setPositiveButton("확인", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
            //model.W_playername = etext.getText().toString();
                Intent intent = new Intent(getApplicationContext(), VersusActivity.class);
                dialog.dismiss();
            }
        });
        dial.setNegativeButton("취소",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dial2.setPositiveButton("확인", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                mp.stop();
                mp = MediaPlayer.create(MeActivity.this,R.raw.start);
                mp2 = MediaPlayer.create(MeActivity.this,R.raw.startdaeguk);
                mp.start();
                mp2.start();
                finish();
                Toast.makeText(MeActivity.this, "대전모드 입장!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), VersusActivity.class);
                intent.putExtra("name",etext.getText().toString());
                intent.putExtra("name2",etext2.getText().toString());
                startActivity(intent);
            }
        });
        dial2.setNegativeButton("취소",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dial2.show();
        dial.show();
    }

    public void onSettingButtonClicked(View v){
        Toast.makeText(this, "세팅페이지", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),SettingActivity.class);
        startActivity(intent);
    }
    public void onExitButtonClicked(View v){
        mp.stop();
        finish();
    }
    public void onBackPressed(){
        mp.stop();
        finish();
    }
}