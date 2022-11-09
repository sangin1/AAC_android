package com.example.aac;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Base64;

public class wordPicActivity extends AppCompatActivity {
    String fractCode, word;
    Button btn1, btn2;
    ImageView imageView;
    Handler handler = new Handler();
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_pic);
        setTitle("사진 추가");
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        imageView = findViewById(R.id.imgView);
        Intent intent = getIntent();
        fractCode = intent.getStringExtra("fractCode");
        word = intent.getStringExtra("word");
        Log.v("test",word);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityResult.launch(intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                String image = "";
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] byteArray = stream.toByteArray();
                image = Base64.getEncoder().encodeToString(byteArray);
                showDialog(image);
            }
        });
    }
    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()==RESULT_OK&&result.getData()!=null){
                        uri = result.getData().getData();
                        try{
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            imageView.setImageBitmap(bitmap);
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }else if(result.getResultCode()==RESULT_CANCELED){
                        Toast.makeText(wordPicActivity.this, "사진 선택 취소", Toast.LENGTH_SHORT).show();

                    }
                }
            });
    class ClientThread extends Thread{
        private String image;
        public ClientThread(String img){
            this.image = img;
        }
        public void run(){
            String host = "aszx1234.duckdns.org";
            int port = 6000;
            try {
                final idVO idCode = (idVO) getApplication();
                Socket socket = new Socket(host, port);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.write("--imgadd--"+fractCode+"--"+image+"--"+idCode.getId()+"--"+word+"\n");
                out.flush();
                String code = in.readLine();
                //스레드 안에서 UI 접근 -> 핸들러
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(code.equals("0")){
                            Toast.makeText(getApplicationContext(), "사진수정 실패", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "사진수정 성공", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }

                    }
                });
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    void showDialog(String image) {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(wordPicActivity.this)
                .setTitle("사진 추가")
                .setMessage("사진을 추가하시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        wordPicActivity.ClientThread thread = new wordPicActivity.ClientThread(image);
                        thread.start();
                    }
                })
                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(wordPicActivity.this, "추가 취소", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }
    public static File bitmapToFile(Context context, Bitmap bitmap, String fileNameToSave) { // File name like "image.png"
        //create a file to write bitmap data
        File file = null;
        try {
            file = new File(Environment.getExternalStorageDirectory() + File.separator + fileNameToSave);
            file.createNewFile();

//Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 , bos); // YOU can also save it in JPEG
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return file;
        }catch (Exception e){
            e.printStackTrace();
            return file; // it will return null
        }
    }
}