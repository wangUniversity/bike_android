package com.example.liujiawang.bike.Service;

import android.util.Log;

import com.example.liujiawang.bike.entity.NameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by liujiawang on 2017/10/8.
 */

public class MyHttpPost {
    // 服务器地址
    private static String SERVER = "http://10.0.2.2:8080/";
    // 项目地址
    // 请求超时
    // 读取超时

    // 通过 POST 方式获取HTTP服务器数据
    public static String executeHttpPost(String servlet, List<NameValuePair> params) {
        String baseURL = SERVER;
        baseURL += servlet+"?";
        String responseMsg = "FAILED";
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        //String post="";
        for(int i=0;i<params.size();i++){
            NameValuePair pair = params.get(i);
            if(i!=0){
                baseURL +="&";
            }
            baseURL += pair.getKey()+"="+pair.getValue();
        }
        Log.i("tag", baseURL);
        try {
            URL url = new URL(baseURL);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(50000);
            connection.setReadTimeout(50000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            //PrintWriter printWriter = new PrintWriter(connection.getOutputStream());
            //printWriter.write(post);
            InputStream in = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while((line=reader.readLine())!=null){
                response.append(line);
            }
            responseMsg = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("tag", "RegisterHttpPost: responseMsg = " + responseMsg);
        return responseMsg;
    }
}
