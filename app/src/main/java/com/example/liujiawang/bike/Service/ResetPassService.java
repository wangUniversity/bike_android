package com.example.liujiawang.bike.Service;

import android.util.Log;

import com.example.liujiawang.bike.entity.NameValuePair;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by liujiawang on 2017/10/23.
 */

public class ResetPassService {
    public static String send(List<NameValuePair> params) throws JSONException {
        String servlet = "changepassword";
        // 通过 POST 方式获取 HTTP 服务器数据
        String responseMsg;
        responseMsg = MyHttpPost.executeHttpPost(servlet, params);
        Log.i("tag", "ForgetPassService: responseMsg = " + responseMsg);
        JSONObject jsonObject = new JSONObject(responseMsg);
        String msg = jsonObject.getString("msg");
        // 解析服务器数据，返回相应 Long 值
        return msg;
    }
}
