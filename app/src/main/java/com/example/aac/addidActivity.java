package com.example.aac;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class addidActivity extends AppCompatActivity {
    EditText id,pw;
    String idText,pwText;
    Button btn1;
    Handler handler = new Handler();
    String checkid="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addid);
        setTitle("회원가입");
        id = findViewById(R.id.idtext);
        pw = findViewById(R.id.pwtext);
        btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idText = id.getText().toString();
                pwText = pw.getText().toString();
                checkid = "0";
                if (idText.equals("") || pwText.equals("")){
                    Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 입력해주세요.",Toast.LENGTH_LONG).show();
                }else {
                    addidActivity.ClientThread thread = new addidActivity.ClientThread();
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }
    class ClientThread extends Thread{
        public void run(){
            String host = "aszx1234.duckdns.org";
            int port = 6000;
            try {
                Socket socket = new Socket(host, port);

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.write("--addmember--"+idText+"--"+pwText+"\n");
                out.flush();
                String code = in.readLine();
                //스레드 안에서 UI 접근 -> 핸들러
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        checkid = code;
                        if (checkid.equals("1")) {
                            Toast.makeText(getApplicationContext(), "존재하는 아이디", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(getApplicationContext(), loginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}