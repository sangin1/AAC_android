package com.example.aac;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class fractDelActivity extends AppCompatActivity {
    GridView gridView;
    word1Adapter adapter;
    ArrayList<word1VO> items;
    String[] fractArr;
    String[] fractCodeArr;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_word_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        fractArr = intent.getStringArrayExtra("fractArr");
        fractCodeArr = intent.getStringArrayExtra("fractCodeArr");
        gridView = findViewById(R.id.gv);
        items = new ArrayList<>();
        for(int i=0;i<fractArr.length;i++) {
            items.add(new word1VO(fractArr[i]));
        }
        adapter = new word1Adapter(items, getApplicationContext());
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                showDialog(Integer.toString(pos));
                                }
        });

    }
    void showDialog(String pos) {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(fractDelActivity.this)
                .setTitle("분류삭제")
                .setMessage("분류를 삭제하시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                fractDelActivity.ClientThread thread = new fractDelActivity.ClientThread(pos);
                                thread.start();
                                Toast.makeText(fractDelActivity.this, "삭제 성공", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                    }
                })
                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(fractDelActivity.this, "삭제 취소", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }
    class ClientThread extends Thread{
        private String pos;
        public ClientThread(String pos){
            this.pos = pos;
        }
        public void run(){
            String host = "aszx1234.duckdns.org";
            int port = 6000;
            try{
                final idVO idCode = (idVO) getApplication();
                Socket socket = new Socket(host, port);

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.write("--delclass--"+String.valueOf(idCode.getId())+"--"+fractCodeArr[Integer.parseInt(pos)]+"\n");
                out.flush();
                String fract = in.readLine();


            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }
}
