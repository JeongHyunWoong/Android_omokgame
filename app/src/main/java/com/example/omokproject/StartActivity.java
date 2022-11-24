package com.example.omokproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    Button btnGameStart;

    public MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_start);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        mp= MediaPlayer.create(this,R.raw.run);
        mp.start();

        btnGameStart = findViewById(R.id.btnGameStart);

        btnGameStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(StartActivity.this, "StartClicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), EnterGameActivity.class);
                startActivity(intent);
            }
        });
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