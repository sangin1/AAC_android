package com.example.aac;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class wordCUD extends AppCompatActivity {
    Button btn1,btn2,btn3,btn4;
    String fractCode;
    String[] wordArr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_cud);
        Intent intent = getIntent();
        fractCode = intent.getStringExtra("fractCode");
        wordArr = intent.getStringArrayExtra("wordArr");
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), wordAddActivity.class);
                intent.putExtra("fractCode", fractCode);
                startActivity(intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), wordUpdateSelectActivity.class);
                intent.putExtra("wordArr", wordArr);
                intent.putExtra("fractCode", fractCode);
                startActivity(intent);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), wordDelActivity.class);
                intent.putExtra("wordArr", wordArr);
                intent.putExtra("fractCode", fractCode);
                startActivity(intent);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), wordPicSelectActivity.class);
                intent.putExtra("wordArr", wordArr);
                intent.putExtra("fractCode", fractCode);
                startActivity(intent);
            }
        });
    }

}