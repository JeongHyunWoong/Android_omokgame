package com.example.omokproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        mp= MediaPlayer.create(this,R.raw.battle);
        mp.start();

        findViewById(R.id.btn1).setOnClickListener(mClick);
        findViewById(R.id.btn2).setOnClickListener(mClick);
        findViewById(R.id.btn3).setOnClickListener(mClick);
    }
    Button.OnClickListener mClick = new Button.OnClickListener()

    {
        public void onClick(View v)
        {
            switch(v.getId())
            {
                case R.id.btn1:
                    Intent ex1 = new Intent(MainActivity.this, MeActivity.class);
                    startActivity(ex1);
                    mp.stop();
                    break;
                case R.id.btn2:
                    Intent ex2 = new Intent(MainActivity.this, StartActivity.class);
                    startActivity(ex2);
                    mp.stop();
                    break;
                case R.id.btn3:
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("나갈겨?")
                            .setMessage("진짜 나갈겨?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                    moveTaskToBack(true);
                                    System.exit(0);
                                }
                            })
                            .setNegativeButton("취소", null)
                            .setCancelable(false);
                    AlertDialog dialog = builder.create();
                    dialog.show();
            }
        }
    };
}