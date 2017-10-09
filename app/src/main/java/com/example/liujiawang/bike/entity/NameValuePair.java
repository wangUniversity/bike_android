package com.example.liujiawang.bike.entity;

/**
 * Created by liujiawang on 2017/10/8.
 */
public class NameValuePair {
    private String key;
    private String value;
    NameValuePair(String key,String value){
        this.key = key;
        this.value = value;
    }
    public String getKey(){
        return key;
    }
    public String getValue(){
        return value;
    }
}
