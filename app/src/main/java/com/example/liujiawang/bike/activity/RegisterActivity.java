package com.example.liujiawang.bike.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liujiawang.bike.entity.BasicNameValuePair;
import com.example.liujiawang.bike.entity.NameValuePair;
import com.example.liujiawang.bike.R;
import com.example.liujiawang.bike.Service.RegisterPostService;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private TextView register;
    private EditText id;
    private EditText password;
    private  EditText password2;
    private  EditText email;
    Handler handler;
    static int REGISTER_FAILED = 0;
    static int REGISTER_SUCCEEDED = 1;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register = (TextView) findViewById(R.id.register);
        id = (EditText) findViewById(R.id.id);
        password = (EditText) findViewById(R.id.password);
        password2 = (EditText) findViewById(R.id.password2);
        email = (EditText)findViewById(R.id.email);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isSamePassword()){
                    Toast.makeText(RegisterActivity.this,"密码不一致",Toast.LENGTH_SHORT).show();
                }else if(!isTrueEmail(email.getText().toString())){
                    Toast.makeText(getApplicationContext(),
                            "邮箱格式错误",Toast.LENGTH_SHORT).show();
                }else{
                    //启动登录Thread
                    dialog = new Dialog(RegisterActivity.this);
                    dialog.setTitle("正在注册，请稍后...");
                    dialog.setCancelable(false);
                    dialog.show();
                    new RegisterPostThread(id.getText().toString(),
                            password.getText().toString(),email.getText().toString()).start();
                }
            }
        });
        handler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                dialog.dismiss();
                if (msg.what == 111) {  // 处理发送线程传回的消息
                    if(msg.obj.toString().equals("SUCCEEDED")){
                        //跳转
                        Toast.makeText(RegisterActivity.this, "模拟跳转", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
    }
    //判断邮箱格式是否正确
    public boolean isTrueEmail(String strEmail) {
        String strPattern = "^[a-zA-Z0-9]";
        if (TextUtils.isEmpty(strPattern)) {
            return false;
        } else {
            return strEmail.matches(strPattern);
        }
    }


    //判断密码是否一致
    public boolean isSamePassword() {
        // Log.d("tag",password.toString()+" "+password2.toString());
        if(password.getText().toString().equals(password2.getText().toString())){
            return true;
        }
        return false;
    }

//    // 检测网络状态
//    public boolean isConnectingToInternet() {
//        ConnectivityManager connectivity = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connectivity != null) {
//            NetworkInfo[] info = connectivity.getAllNetworkInfo();
//            if (info != null)
//                for (int i = 0; i < info.length; i++)
//                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
//                    {
//                        return true;
//                    }
//        }
//        return false;
//    }

    //登录Thread调用RegisterPostService，返回Msg
    public class RegisterPostThread extends Thread {
        public String id, password,email;

        public RegisterPostThread(String id, String password,String email) {
            this.id = id;
            this.password = password;
            this.email = email;
        }

        @Override
        public void run() {
            // Sevice传回int
            int responseInt = 0;
            if(!id.equals("")) {
                // 要发送的数据
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", id));
                params.add(new BasicNameValuePair("password", password));
                params.add(new BasicNameValuePair("email",email));
                // 发送数据，获取对象
                try {
                    responseInt = RegisterPostService.send(params);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("tag", "LoginActivity: responseInt = " + responseInt);
                // 准备发送消息
                Message msg = handler.obtainMessage();
                // 设置消息默认值
                msg.what = 111;
                // 服务器返回信息的判断和处理
                if(responseInt == REGISTER_FAILED) {
                    msg.obj = "FAILED";
                }else if(responseInt == REGISTER_SUCCEEDED) {
                    msg.obj = "SUCCEEDED";
                }
                handler.sendMessage(msg);
            }
        }
    }
}
