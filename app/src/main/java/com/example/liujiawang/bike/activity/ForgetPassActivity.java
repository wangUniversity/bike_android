package com.example.liujiawang.bike.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.liujiawang.bike.R;

import org.w3c.dom.Text;

/**
 * Created by wcl on 2017/10/14.
 */

public class ForgetPassActivity extends AppCompatActivity {
    private TimeCount time;
    EditText ac,number;
    TextView getNumber,next;
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
            }
        });//点击获取验证码
        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(true) {              //判断验证码输入是否正确，正确就跳转，否则重新输入
                    Intent intent = new Intent(ForgetPassActivity.this, ResetPassActivity.class);
                    startActivity(intent);
                }
            }
        });
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
            getNumber.setClickable(false);
            getNumber.setBackgroundColor(Color.parseColor("#4EB84A"));
        }

    }

}
