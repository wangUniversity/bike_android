package com.example.liujiawang.bike.activity;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liujiawang.bike.R;
import com.example.liujiawang.bike.Service.ForgetPassService;
import com.example.liujiawang.bike.entity.BasicNameValuePair;
import com.example.liujiawang.bike.entity.NameValuePair;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wcl on 2017/10/14.
 */

public class ForgetPassActivity extends AppCompatActivity {
    private TimeCount time;
    EditText ac,number;
    TextView getNumber,next;
    Handler handler;
    Dialog dialog;
    String email;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        ac=(EditText)findViewById(R.id.forget_account);
        number=(EditText)findViewById(R.id.number);
        getNumber=(TextView) findViewById(R.id.send_number);
        next=(TextView) findViewById(R.id.next);//跳到重置密码界面
        time=new TimeCount(60000,1000);
        getNumber.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                time.start();
//                dialog = new Dialog(ForgetPassActivity.this);
//                dialog.setTitle("发送验证码，请稍后...");
//                dialog.setCancelable(false);
//                dialog.show();
                new ForgetPostThread(ac.getText().toString()).start();

            }
        });//点击获取验证码
        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(true) {              //判断验证码输入是否正确，正确就跳转，否则重新输入
                    Intent intent = new Intent(ForgetPassActivity.this, ResetPassActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(ForgetPassActivity.this,"验证失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
        handler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //dialog.dismiss();
                if (msg.what == 111) {  // 处理发送线程传回的消息
                    email = msg.obj.toString();
                }
            }
        };

    }
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onTick(long millisUntilFinished) {
            getNumber.setBackgroundColor(Color.parseColor("#B6B6D8"));
            getNumber.setClickable(false);
            getNumber.setText("("+millisUntilFinished/1000+")秒后可重新发送");
        }
        public void onFinish(){
            getNumber.setText("重新获取验证码");
            getNumber.setClickable(true);
            getNumber.setBackgroundColor(Color.parseColor("#4EB84A"));
        }

    }
    public class ForgetPostThread extends Thread{
        public String email;
        public ForgetPostThread(String email){
            this.email = email;
        }

        @Override
        public void run() {
            String msge = null;
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            try {
                msge = ForgetPassService.send(params);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Message msg = handler.obtainMessage();
            // 设置消息默认值
            msg.what = 111;
            // 服务器返回信息的判断和处理
            msg.obj = msge;
            handler.sendMessage(msg);
        }
    }

}
