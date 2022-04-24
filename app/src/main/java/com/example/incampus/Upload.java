package com.example.incampus;

public class Upload {
    private String mName;
    private String mInageUrl;

    public Upload(){

    }

    public Upload(String name, String imageUrl){

        mName = name;
        mInageUrl = imageUrl;
    }

    public String getmName(){
        return mName;
    }

    public void setmName(String name){
        mName = name;
    }

    public void setmInageUrl(String imageUrl){
        mInageUrl = imageUrl;
    }
}
