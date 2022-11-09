package com.example.aac;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class editWordActivity extends AppCompatActivity {
    GridView gridView;
    word1Adapter adapter;
    ArrayList<word1VO> items;
    String[] fractArr;
    String[] fractCodeArr;
    ArrayList<String[]> wordArr;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_word_main);
        editWordActivity.ClientThread thread = new editWordActivity.ClientThread();
        thread.start();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        wordArr = new ArrayList<>();
        gridView = findViewById(R.id.gv);
        items = new ArrayList<>();
        /*final idVO idCode = (idVO) getApplication();
        Log.v("test",String.valueOf(idCode.getId()));*/

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                if(pos==0){
                    Intent intent = new Intent(getApplicationContext(), fractCUD.class);
                    intent.putExtra("fractArr", fractArr);
                    intent.putExtra("fractCodeArr", fractCodeArr);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getApplicationContext(), wordCUD.class);
                    intent.putExtra("fractCode", fractCodeArr[pos-1]);
                    intent.putExtra("wordArr", wordArr.get(pos));
                    startActivity(intent);
                }
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
                out.write("--upin--"+String.valueOf(idCode.getId())+"\n");
                out.flush();
                String fract = in.readLine();
                String[] fractR = fract.split("-");
                //스레드 안에서 UI 접근 -> 핸들러
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        fractArr = new String[fractR.length];
                        items = new ArrayList<>();
                        items.add(new word1VO("분류 편집"));
                        if(fractR[0].equals("0")){

                        }else {
                            for (int i = 0; i < fractR.length; i++) {
                                items.add(new word1VO(fractR[i]));
                                fractArr[i] = fractR[i];
                            }
                        }
                        adapter = new word1Adapter(items, getApplicationContext());
                        gridView.setAdapter(adapter);
                    }
                });
                String fractCode = in.readLine();
                String[] fractCodeR = fractCode.split("-");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        fractCodeArr = new String[fractCodeR.length];
                        for(int i=0;i<fractCodeR.length;i++) {
                            fractCodeArr[i] = fractCodeR[i];
                        }
                    }
                });
                out.write("--upin2\n");
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

            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }
}