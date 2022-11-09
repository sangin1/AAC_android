package com.example.aac;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class wordPicSelectActivity extends AppCompatActivity {
    GridView gridView;
    word1Adapter adapter;
    ArrayList<word1VO> items;
    String[] wordArr;
    String fractCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_pic_select);
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
                Intent intent = new Intent(getApplicationContext(), wordPicActivity.class);
                intent.putExtra("fractCode", fractCode);
                intent.putExtra("word", wordArr[pos]);
                startActivity(intent);
            }
        });

    }
}
