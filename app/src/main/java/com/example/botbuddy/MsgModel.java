package com.example.botbuddy;

public class MsgModel {  // this class will hold the data which we are getting from the api
    private String cnt;

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }

    public MsgModel(String cnt) {
        this.cnt = cnt;
    }
}
