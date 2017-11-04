package com.example.liujiawang.bike.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.liujiawang.bike.R;

public class UsersActivity extends AppCompatActivity {
    TextView userMessage,userMoney,userSecurity,about;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        userMessage=(TextView)findViewById(R.id.userMessage);
        userMoney=(TextView)findViewById(R.id.userMoney);
        userSecurity=(TextView)findViewById(R.id.userSecurity);
        about=(TextView)findViewById(R.id.about);
    }
}

