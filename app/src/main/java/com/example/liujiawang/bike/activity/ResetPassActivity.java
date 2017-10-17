package com.example.liujiawang.bike.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liujiawang.bike.R;

public class ResetPassActivity extends AppCompatActivity {
    EditText setPass1,setPass2;//新密码与确认密码
    TextView finished;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        setPass1=(EditText)findViewById(R.id.new_password_1);
        setPass2=(EditText)findViewById(R.id.new_password_2);
        finished=(TextView)findViewById(R.id.finished);
        finished.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if((setPass2.getText().toString().equals(setPass1.getText().toString()))){
                    Intent intent=new Intent(ResetPassActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(ResetPassActivity.this,"输入两次密码不相同,请重新输入",Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }
}
