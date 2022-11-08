package com.example.aac;

import static java.lang.System.out;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

public class word1Activity extends AppCompatActivity {
    GridView gridView;
    word1Adapter adapter;
    ArrayList<word1VO> items;
    ArrayList<String[]> wordArr;
    ArrayList<String[]> imgArr;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word1);
        ClientThread thread = new ClientThread();
        thread.start();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        gridView = findViewById(R.id.gv);
        items = new ArrayList<>();
        wordArr = new ArrayList<>();
        imgArr = new ArrayList<>();
        /*final idVO idCode = (idVO) getApplication();
        Log.v("test",String.valueOf(idCode.getId()));*/

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Intent intent = new Intent(getApplicationContext(), word2Activity.class);
                intent.putExtra("wordArr", wordArr.get(pos));
                intent.putExtra("imgArr", imgArr.get(pos));
                startActivity(intent);

            }
        });

    }
    class ClientThread extends Thread{
        public void run(){
            String host = "aszx1234.duckdns.org";
            int port = 6000;
            try{
                final idVO idCode = (idVO) getApplication();

                Socket socket = new Socket(host, port);

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.write("--1--"+String.valueOf(idCode.getId())+"\n");
                out.flush();
                String fract = in.readLine();
                String[] fractR = fract.split("-");
                //스레드 안에서 UI 접근 -> 핸들러
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        items = new ArrayList<>();
                        for(int i=0;i<fractR.length;i++) {
                            items.add(new word1VO(fractR[i]));
                        }
                        adapter = new word1Adapter(items, getApplicationContext());
                        gridView.setAdapter(adapter);
                    }
                });
                out.write("--2--"+String.valueOf(idCode.getId())+"\n");
                out.flush();
                String word = in.readLine();
                String[] wordR = word.split("@");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        wordArr.clear();
                        for(int i=0;i<wordR.length;i++) {
                            String[] wordR2 = wordR[i].split("-");
                                wordArr.add(wordR2);
                        }
                    }
                });
                out.write("--3--"+String.valueOf(idCode.getId())+"\n");
                out.flush();
                String size = in.readLine();
                out.write("--32--"+String.valueOf(idCode.getId())+"\n");
                out.flush();
                String img="";
                while (true){
                    img = img + in.readLine();
                    if(img.length() >= Integer.parseInt(size)){
                        break;
                    }
                }
                String[] imgR = img.split("@");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        imgArr.clear();
                        for(int i=0;i<imgR.length;i++) {
                            String[] imgR2 = imgR[i].split("-");
                            imgArr.add(imgR2);
                        }
                        //Log.v("test",imgArr.get(0)[0]);
                    }
                });
                socket.close();

            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }

}

