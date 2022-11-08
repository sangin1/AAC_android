package com.example.aac;

import static java.lang.System.out;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

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
import java.util.Locale;

public class word2Activity extends AppCompatActivity {
    GridView gridView;
    word2Adapter adapter;
    ArrayList<word2VO> items;
    String[] wordArr;
    String[] imgArr;
    Button btn1,btn2;
    TextView tv;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word2);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        wordArr = intent.getStringArrayExtra("wordArr");
        imgArr = intent.getStringArrayExtra("imgArr");
        gridView = findViewById(R.id.gv);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        tv = findViewById(R.id.tv);
        items = new ArrayList<>();
        for(int i=0;i<wordArr.length;i++) {
            items.add(new word2VO(wordArr[i],imgArr[i]));
        }
        adapter = new word2Adapter(items, getApplicationContext());
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                tv.setText(wordArr[pos]);
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.speak(tv.getText().toString(),TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv.setText("");
            }
        });
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.KOREAN);
                tts.setPitch(1.0f);         // 음성 톤.
                tts.setSpeechRate(0.8f);    // 읽는 속도
            }
        });


    }
}
