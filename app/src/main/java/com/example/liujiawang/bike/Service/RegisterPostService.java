package com.example.liujiawang.bike.Service;

import android.util.Log;

import com.example.liujiawang.bike.entity.NameValuePair;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by liujiawang on 2017/10/9.
 */

public class RegisterPostService {
    static int REGISTER_FAILED = 0;
    static int REGISTER_SUCCEEDED = 1;

    public static int send(List<NameValuePair> params) throws JSONException {
        // 返回值
        int responseInt = REGISTER_FAILED;
        // 定位服务器的Servlet
        String servlet = "register";
        // 通过 POST 方式获取 HTTP 服务器数据
        String responseMsg;
        responseMsg = MyHttpPost.executeHttpPost(servlet, params);
        Log.i("tag", "LoginService: responseMsg = " + responseMsg);
        JSONObject jsonObject = new JSONObject(responseMsg);
        String msg = jsonObject.getString("msg");
        // 解析服务器数据，返回相应 Long 值
        if(msg.equals("true")) {
            responseInt = REGISTER_SUCCEEDED;
        }
        return responseInt;
    }
}

