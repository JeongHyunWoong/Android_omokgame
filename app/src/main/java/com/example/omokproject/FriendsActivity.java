package com.example.omokproject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class FriendsActivity extends AppCompatActivity {

    public MediaPlayer mp,mp2;

    SoundPool soundPool= new SoundPool(5, AudioManager.STREAM_MUSIC,0);
    public int bye;
    public void loadsound(){
        bye = soundPool.load(VersusActivity.context,R.raw.bye,1);
    }
    public void playbyesound(){
        soundPool.play(bye,1.0f,1.0f,0,0,1.0f);
    }

    TextView map_elem;
    GridLayout map;
    TextView[][] textViews = new TextView[13][13];
    int giveId = 0;

    int myTurn;
    public boolean gameStart = false;
    public boolean gameClose = false;

    String host;
    int port;
    boolean isEntered = true;

    ValueHandler handler = new ValueHandler(); //서버로부터 온 메시지에 해당하는 처리
    Socket socket;
    //netWork 입출력
    PrintWriter writer; //입력
    BufferedReader reader; //출력

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_friends);

        Intent intent = getIntent();
        host = intent.getStringExtra("host");
        port = intent.getIntExtra("port",0);

        //ServerSocket접속
        final ClientThread thread = new ClientThread();
        thread.start();

        //region 오목판 만들기
        map = findViewById(R.id.gridLayoutMain);
        map.setBackgroundColor(Color.parseColor("#E5CC94"));
        for(int i=0;i<13;i++) {
            for(int j=0; j<13; j++){
                map_elem = new TextView(getApplicationContext());
                map_elem.setText(" ");
                Resources res = getResources();
                Drawable shape = ResourcesCompat.getDrawable(res, R.drawable.border, getTheme());
                map_elem.setBackground(shape);
                map_elem.setMaxWidth(70);
                map_elem.setMaxHeight(70);
                map_elem.setBackground(shape);
                map_elem.setId(giveId);
                map.addView(map_elem);

                textViews[i][j] = map_elem;
                textViews[i][j] = findViewById(giveId++);
                final int finalI = i;
                final int finalJ = j;

                //region Tile 클릭 이벤트
                textViews[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!gameStart){
                            Log.d("Game", "gameStart:"+String.valueOf(gameStart));
                            Toast.makeText(FriendsActivity.this,"게임이 아직 시작되지 않았습니다.",Toast.LENGTH_SHORT).show();
                        }else{
                            new Thread(){
                                @Override
                                public void run() {
                                    thread.sendValue("VALUE:"+finalI+","+finalJ+":"+myTurn);
                                }
                            }.start();
                        }
                    }
                });
                //endregion
            }
        }
        //endregion
    }

    //클라이언트 서버 관리
    class ClientThread extends Thread {

        @Override
        public void run() {
            try {
                socket = new Socket(host, port);

                writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // 입장했을 때
                if(isEntered){
                    sendValue("ENTER:Client["+getLocalIpAddress()+"] is Entered");
                }

                while(true){
                    //데이터 받으면 handler로 전송
                    String input = reader.readLine();
                    Log.d("ClientThread","Received data: "+input);
                    Bundle bundle = new Bundle();
                    bundle.putString("value",input);
                    Message msg = new Message();
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }

            } catch (IOException e){
                e.printStackTrace();
                closeConnection();
            } catch (Exception e){
                e.printStackTrace();
                closeConnection();
            } finally {
                closeConnection();
            }
        }

        //서버에 메시지 전송
        public void sendValue(String data){
            try{
                //writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
                Log.d("ClientStream", data);
                Log.d("ClientStream","Send to server ...");
                writer.println(data);
                writer.flush();

            } catch (Exception e){
                e.printStackTrace();
            }
        }

        //연결끊기
        public void closeConnection(){
            try {
                writer.close();
                reader.close();
                socket.close();
                Log.d("ClientStream", "Close connection from server");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class ValueHandler extends Handler {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            String inputData = msg.getData().getString("value");
            String protocol = inputData.split(":")[0]; //protocol 구분

            switch (protocol){
                case "ORDER":
                    myTurn = Integer.parseInt(inputData.split(":")[1]);
                    Log.d("Input","myTurn: "+myTurn);
                    break;
                case "VALUE":
                    String value = inputData.split(":")[1];
                    int i = Integer.parseInt(value.split(",")[0]);
                    int j = Integer.parseInt(value.split(",")[1]);
                    int turn = Integer.parseInt(inputData.split(":")[2]);
                    if(turn == 0) {
                        textViews[i][j].setBackgroundColor(Color.BLACK);
                    } else {
                        textViews[i][j].setBackgroundColor(Color.WHITE);
                    }
                    break;
                case "START":
                    gameStart = true;
                    Log.d("Game", "gameStart: "+String.valueOf(gameStart));

                    Toast.makeText(FriendsActivity.this,"게임을 시작합니다.",Toast.LENGTH_SHORT).show();
                    break;
                case "RESULT":
                    gameClose = true;
                    Log.d("Game","게임이 종료되었습니다");
                    String result = inputData.split(":")[1];
                    if(result.startsWith("Win")){//이겼을때
                        if(myTurn == 0){
                            AlertDialog.Builder builder = new AlertDialog.Builder(FriendsActivity.this);
                            LayoutInflater factory = LayoutInflater.from(FriendsActivity.this);
                            final View view = factory.inflate(R.layout.alertvictory, null);
                            builder.setTitle("흑 승리!")
                                    .setMessage("흑 승리하셨습니다! 메뉴로 돌아가기")
                                    .setView(view)
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent mainIntent = new Intent(getApplicationContext(), StartActivity.class);
                                            startActivity(mainIntent);
                                        }
                                    })
                                    .show();
                            try {
                                socket.close();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(FriendsActivity.this);
                            LayoutInflater factory = LayoutInflater.from(FriendsActivity.this);
                            final View view = factory.inflate(R.layout.alertclose, null);
                            builder.setTitle("백 승리!")
                                    .setMessage("백 승리하셨습니다! 메뉴로 돌아가기")
                                    .setView(view)
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent mainIntent = new Intent(getApplicationContext(), StartActivity.class);
                                            startActivity(mainIntent);
                                        }
                                    })
                                    .show();
                            try {
                                socket.close();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }else{
                        if(myTurn == 0){
                            AlertDialog.Builder builder = new AlertDialog.Builder(FriendsActivity.this);
                            LayoutInflater factory = LayoutInflater.from(FriendsActivity.this);
                            final View view = factory.inflate(R.layout.alertvictory, null);
                            builder.setTitle("흑 승리!")
                                    .setMessage("흑 승리하셨습니다! 메뉴로 돌아가기")
                                    .setView(view)
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent mainIntent = new Intent(getApplicationContext(), StartActivity.class);
                                            startActivity(mainIntent);
                                        }
                                    })
                                    .show();
                            try {
                                socket.close();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(FriendsActivity.this);
                            LayoutInflater factory = LayoutInflater.from(FriendsActivity.this);
                            final View view = factory.inflate(R.layout.alertclose, null);
                            builder.setTitle("패배....")
                                    .setMessage("패배 했습니다....! 메뉴로 돌아가기")
                                    .setView(view)
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent mainIntent = new Intent(getApplicationContext(), StartActivity.class);
                                            startActivity(mainIntent);
                                        }
                                    })
                                    .show();
                            try {
                                socket.close();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                default:
                    Toast.makeText(FriendsActivity.this, inputData,Toast.LENGTH_SHORT).show();
            }
            loadsound();
//            int i = Integer.parseInt(value.split(",")[0]);
//            int j = Integer.parseInt(value.split(",")[1]);
//
//            if(myColor == "black"){
//                textViews[i][j].setBackgroundColor(Color.WHITE);
//                pan[i][j] = whiteTiles;
//            } else if (myColor == "white"){
//                textViews[i][j].setBackgroundColor(Color.BLACK);
//                pan[i][j] = blackTiles;
//            }
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FriendsActivity.this);
        builder.setTitle("나가게?")
                .setMessage("진짜 나갈라고??")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        playbyesound();
                        finishAffinity();
                        System.runFinalization();
                        System.exit(0);
                    }
                })
                .setNegativeButton("취소", null)
                .setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    public void onExitButtonClicked(View v){
        mp.stop();
        finish();
    }
}