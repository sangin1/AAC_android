package com.example.aac;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class word1Socket{
    public String[] main(){
        String host = "aszx1234.duckdns.org";
        int port = 6000;
        String a="";
        Socket socket;
        String[] b;
        String[] c= new String[1];
        try {
            socket = new Socket(host, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("--1--0\n");
            out.flush();

            a = in.readLine();
            out.flush();
            Log.v("test",a);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return c;
    }




}
