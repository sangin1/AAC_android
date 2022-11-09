package com.example.aac;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class fractAddActivity extends AppCompatActivity {
    EditText id,pw;
    String idText,pwText;
    Button btn1;
    Handler handler = new Handler();
    String checkid="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fract_add);
        setTitle("분류추가");
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
                    Toast.makeText(getApplicationContext(), "분류와 첫 단어를 입력해주세요.",Toast.LENGTH_LONG).show();
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
                out.write("--addclass--"+idText+"--"+pwText+"--"+idCode.getId()+"\n");
                out.flush();
                String code = in.readLine();
                //스레드 안에서 UI 접근 -> 핸들러
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(code.equals("1")){
                            Toast.makeText(getApplicationContext(), "같은 분류가 있습니다", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "분류추가 성공", Toast.LENGTH_LONG).show();
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
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(fractAddActivity.this)
                .setTitle("분류추가")
                .setMessage("분류를 추가하시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        fractAddActivity.ClientThread thread = new fractAddActivity.ClientThread();
                        thread.start();
                    }
                })
                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(fractAddActivity.this, "추가 취소", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }
}
