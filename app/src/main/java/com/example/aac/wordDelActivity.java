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

public class wordDelActivity extends AppCompatActivity {
    GridView gridView;
    word1Adapter adapter;
    ArrayList<word1VO> items;
    String[] wordArr;
    String fractCode;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_del);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        wordArr = intent.getStringArrayExtra("wordArr");
        fractCode = intent.getStringExtra("fractCode");
        gridView = findViewById(R.id.gv);
        items = new ArrayList<>();
        for(int i=0;i<wordArr.length;i++) {
            items.add(new word1VO(wordArr[i]));
        }
        adapter = new word1Adapter(items, getApplicationContext());
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                if(wordArr.length > 1){
                    showDialog(Integer.toString(pos));
                }else{
                    Toast.makeText(wordDelActivity.this, "마지막 단어는 삭제 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    void showDialog(String pos) {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(wordDelActivity.this)
                .setTitle("단어 삭제")
                .setMessage("단어를 삭제하시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                wordDelActivity.ClientThread thread = new wordDelActivity.ClientThread(wordArr[Integer.parseInt(pos)]);
                                thread.start();
                                Toast.makeText(wordDelActivity.this, "삭제 성공", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(wordDelActivity.this, "삭제 취소", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }
    class ClientThread extends Thread{
        private String word;
        public ClientThread(String word){
            this.word = word;
        }
        public void run(){
            String host = "aszx1234.duckdns.org";
            int port = 6000;
            try{
                final idVO idCode = (idVO) getApplication();
                Socket socket = new Socket(host, port);

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.write("--delword--"+fractCode+"--"+word+"--"+String.valueOf(idCode.getId())+"\n");
                out.flush();
                String check = in.readLine();


            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }
}
