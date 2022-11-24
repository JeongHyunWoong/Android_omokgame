package com.example.omokproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

public class LogActivity extends AppCompatActivity {
    private static int progress_percent;

    ImageView horse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        horse = (ImageView) findViewById(R.id.jjang);

        Glide.with(LogActivity.this).load(R.drawable.horse).into(horse);

        ProgressBar progressBar = findViewById(R.id.progressBar);
        progress_percent = 0;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable()

        {
            @Override
            public void run() {
                Intent intent = new Intent(LogActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 5000);

        new Thread() {
            public  void run() {
                while(true) {
                    try {
                        while(!Thread.currentThread().isInterrupted()){
                            progress_percent += 20;
                            Thread.sleep(1000);
                            ProgressBar progressBar = findViewById(R.id.progressBar);
                            progressBar.setProgress(progress_percent);

                            if (progress_percent >= 100) {
                                Intent intent = new Intent(LogActivity.this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                currentThread().interrupt();
                            }
                        }
                    } catch (Throwable t){
                    } finally {
                    }
                }
            }
        }.start();
    }
}