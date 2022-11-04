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

public class word2Activity extends AppCompatActivity {
    GridView gridView;
    word2Adapter adapter;
    ArrayList<word1VO> items;
    String[] wordArr;
    String[] imgArr;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word2);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        wordArr = intent.getStringArrayExtra("wordArr");
        gridView = findViewById(R.id.gv);
        items = new ArrayList<>();
        for(int i=0;i<wordArr.length;i++) {
            items.add(new word1VO(wordArr[i]));
        }
        adapter = new word2Adapter(items, getApplicationContext());
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {

            }
        });


    }
}
