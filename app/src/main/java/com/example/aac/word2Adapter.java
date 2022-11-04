package com.example.aac;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class word2Adapter extends BaseAdapter {

    ArrayList<word1VO> items;
    Context context;

    public word2Adapter(ArrayList<word1VO> items, Context context){
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.word2item, viewGroup, false);

        TextView tv_name = view.findViewById(R.id.textView_name);
        tv_name.setText(items.get(i).name);

        return view;
    }

}