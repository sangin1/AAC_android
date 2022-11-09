package com.example.aac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class fractCUD extends AppCompatActivity {
    Button btn1,btn2,btn3;
    String[] fractArr, fractCodeArr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fract_cud);
        Intent intent = getIntent();
        fractArr = intent.getStringArrayExtra("fractArr");
        fractCodeArr = intent.getStringArrayExtra("fractCodeArr");
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), fractAddActivity.class);
                startActivity(intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), fractUpdateSelectActivity.class);
                intent.putExtra("fractArr", fractArr);
                intent.putExtra("fractCodeArr", fractCodeArr);
                startActivity(intent);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), fractDelActivity.class);
                intent.putExtra("fractArr", fractArr);
                intent.putExtra("fractCodeArr", fractCodeArr);
                startActivity(intent);
            }
        });
    }
}