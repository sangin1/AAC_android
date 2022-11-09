package com.example.aac;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;

public class MainActivity extends AppCompatActivity {
    Button btn1,btn2,btn3,btn4;
    String checkid = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        final idVO idCode = (idVO) getApplication();
        checkid = idCode.getId();
        if (checkid.equals("0")){
            btn3.setText("로그인");
            btn4.setVisibility(View.GONE);
        }else {
            btn3.setText("로그아웃");
            btn4.setVisibility(View.VISIBLE);
        }

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), word1Activity.class);
                startActivity(intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkid.equals("0")){
                    Intent intent = new Intent(getApplicationContext(), loginActivity.class);
                    startActivity(intent);
                }else {
                    idCode.setId("0");
                    checkid="0";
                    try {
                        Intent intent = getIntent();
                        finish(); //현재 액티비티 종료 실시
                        overridePendingTransition(0, 0); //인텐트 애니메이션 없애기
                        startActivity(intent); //현재 액티비티 재실행 실시
                        overridePendingTransition(0, 0); //인텐트 애니메이션 없애기
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), editWordActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
/*String image ="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";
        Bitmap bitmap = null;
        byte[] byteArray = Base64.getDecoder().decode(image);
        ByteArrayInputStream stream2 = new ByteArrayInputStream(byteArray);
        bitmap = BitmapFactory.decodeStream(stream2);
        ip.setImageBitmap(bitmap);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.b);
        String image = "";
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        image = Base64.getEncoder().encodeToString(byteArray);
        //Log.v("test",image);*/