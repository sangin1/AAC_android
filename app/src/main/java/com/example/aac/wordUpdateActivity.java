package com.example.aac;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class wordUpdateActivity extends AppCompatActivity {
    EditText id;
    String idText,fractCode,word;
    Button btn1;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_update);
        setTitle("단어수정");
        id = findViewById(R.id.idtext);
        btn1 = findViewById(R.id.btn1);
        Intent intent = getIntent();
        fractCode = intent.getStringExtra("fractCode");
        word = intent.getStringExtra("word");
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idText = id.getText().toString();
                if (idText.equals("")){
                    Toast.makeText(getApplicationContext(), "단어를 입력해주세요.",Toast.LENGTH_LONG).show();
                }else {
                    showDialog();
                }
            }
        });

    }
    class ClientThread extends Thread{
        public void run(){
            String host = "aszx1234.duckdns.org";
            int port = 6000;
            try {
                final idVO idCode = (idVO) getApplication();
                Socket socket = new Socket(host, port);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.write("--updateword--"+fractCode+"--"+idText+"--"+idCode.getId()+"--"+word+"\n");
                out.flush();
                String code = in.readLine();
                //스레드 안에서 UI 접근 -> 핸들러
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(code.equals("0")){
                            Toast.makeText(getApplicationContext(), "단어수정 실패", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "단어수정 성공", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }

                    }
                });
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    void showDialog() {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(wordUpdateActivity.this)
                .setTitle("단어수정")
                .setMessage("단어를 수정하시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        wordUpdateActivity.ClientThread thread = new wordUpdateActivity.ClientThread();
                        thread.start();
                    }
                })
                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(wordUpdateActivity.this, "추가 취소", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }
}

