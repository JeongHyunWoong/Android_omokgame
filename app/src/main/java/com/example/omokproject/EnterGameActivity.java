package com.example.omokproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class EnterGameActivity extends AppCompatActivity {

    Button btnEnterServer;
    EditText serverIPText, serverPortText;
    public MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_server);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();

        btnEnterServer = findViewById(R.id.btnEnterServer);
        serverIPText = findViewById(R.id.serverIPText);
        serverPortText = findViewById(R.id.serverPortText);

        btnEnterServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), FriendsActivity.class);
                String host = serverIPText.getText().toString();

                if (host.isEmpty() || serverPortText.getText().toString().isEmpty()){
                    Toast.makeText(EnterGameActivity.this, "IP주소와 포트번호를 모두 입력해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    int port = Integer.parseInt(serverPortText.getText().toString());
                    intent.putExtra("host",host);
                    intent.putExtra("port",port);
                    startActivityForResult(intent,0);
                }catch (Exception e){
                    e.printStackTrace();
                    return;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EnterGameActivity.this);
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
    public void onExitButtonClicked(View v){
        mp.stop();
        finish();
    }
}