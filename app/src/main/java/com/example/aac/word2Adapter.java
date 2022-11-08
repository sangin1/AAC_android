package com.example.aac;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Base64;

public class word2Adapter extends BaseAdapter {

    ArrayList<word2VO> items;
    Context context;

    public word2Adapter(ArrayList<word2VO> items, Context context){
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.word2item, viewGroup, false);

        TextView tv_name = view.findViewById(R.id.textView_name);
        tv_name.setText(items.get(i).name);
        if(items.get(i).img.equals("null")){

        }else {
            Bitmap bitmap = null;
            byte[] byteArray = Base64.getDecoder().decode(items.get(i).img);
            ByteArrayInputStream stream = new ByteArrayInputStream(byteArray);
            bitmap = BitmapFactory.decodeStream(stream);

            ImageView imgView = view.findViewById(R.id.imgView);
            imgView.setImageBitmap(bitmap);
        }

        return view;
    }

}