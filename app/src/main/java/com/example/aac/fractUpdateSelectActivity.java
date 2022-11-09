package com.example.aac;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class fractUpdateSelectActivity extends AppCompatActivity {
    GridView gridView;
    word1Adapter adapter;
    ArrayList<word1VO> items;
    String[] fractArr;
    String[] fractCodeArr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fract_update_select);
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
                Intent intent = new Intent(getApplicationContext(), fractUpdateActivity.class);
                intent.putExtra("fractCode", fractCodeArr[pos]);
                startActivity(intent);
            }
        });

    }
}
