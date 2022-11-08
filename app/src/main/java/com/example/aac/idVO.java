package com.example.aac;

import android.app.Application;

public class idVO extends Application {
    private String id="0";
    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }
}
