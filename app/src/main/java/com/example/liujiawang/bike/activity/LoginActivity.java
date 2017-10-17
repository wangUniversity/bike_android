package com.example.liujiawang.bike.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liujiawang.bike.entity.BasicNameValuePair;
import com.example.liujiawang.bike.Service.LoginPostService;
import com.example.liujiawang.bike.entity.NameValuePair;
import com.example.liujiawang.bike.R;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liujiawang on 2017/10/8.
 */

public class LoginActivity extends AppCompatActivity{

    EditText id, password;
    TextView login, register,forgetPass;
    CheckBox rememberPass;
    Handler handler;
    static int LOGIN_FAILED = 0;
    static int LOGIN_SUCCEEDED = 1;
    Dialog dialog;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        id = (EditText)findViewById(R.id.login_account);
        password = (EditText) findViewById(R.id.login_password);
        login=(TextView)findViewById(R.id.login);
        register=(TextView)findViewById(R.id.register);
        forgetPass=(TextView)findViewById(R.id.forget_pass);
        rememberPass=(CheckBox)findViewById(R.id.remember_pass);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = pref.getBoolean("remember_password",false);
        if(isRemember){
            String id2 = pref.getString("id","");
            String password2 = pref.getString("password","");
            id.setText(id2);
            password.setText(password2);
            rememberPass.setChecked(true);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(true){ //检查网络
                    if (id.getText().toString().equals(""))
                        Toast.makeText(LoginActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
                    else {
                        //启动登录Thread
                        dialog = new Dialog(LoginActivity.this);
                        dialog.setTitle("正在登录，请稍后...");
                        dialog.setCancelable(false);
                        dialog.show();
                        new LoginPostThread(id.getText().toString(),
                                password.getText().toString()).start();

                    }
                }else{
                    Toast.makeText(getApplicationContext(),
                            "网络未连接",Toast.LENGTH_SHORT).show();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        forgetPass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent2=new Intent(LoginActivity.this,ForgetPassActivity.class);
                startActivity(intent2);
            }
        });
        handler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                dialog.dismiss();
                if (msg.what == 111) {  // 处理发送线程传回的消息
                    if(msg.obj.toString().equals("SUCCEEDED")){
                        //跳转
                        editor = pref.edit();
                        if(rememberPass.isChecked()){
                            //检查复选框是否被选中
                            editor.putBoolean("remember_password",true);
                            editor.putString("id",id.getText().toString());
                            editor.putString("password",password.getText().toString());
                        }else{
                            editor.clear();
                        }
                        editor.apply();
                        Toast.makeText(LoginActivity.this, "模拟跳转", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(LoginActivity.this, "账号和密码不匹配", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
    }


    // 检测网络状态
    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
        }
        return false;
    }

    //登录Thread调用LoginPostService，返回Msg
    public class LoginPostThread extends Thread {
        public String id, password;

        public LoginPostThread(String id, String password) {
            this.id = id;
            this.password = password;
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
                // 发送数据，获取对象
                try {
                    responseInt = LoginPostService.send(params);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("tag", "LoginActivity: responseInt = " + responseInt);
                // 准备发送消息
                Message msg = handler.obtainMessage();
                // 设置消息默认值
                msg.what = 111;
                // 服务器返回信息的判断和处理
                if(responseInt == LOGIN_FAILED) {
                    msg.obj = "FAILED";
                }else if(responseInt == LOGIN_SUCCEEDED) {
                    msg.obj = "SUCCEEDED";
                }
                handler.sendMessage(msg);
            }
        }
    }
}
