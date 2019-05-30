package com.gmail.muskankataria2408.chatwithbae;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {



    private Button mregbtn ,mloginbtn,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mloginbtn=(Button)findViewById(R.id.start_login_btn);
        mregbtn=(Button)findViewById(R.id.start_reg_btn);
        phone=(Button)findViewById(R.id.phone_number);

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(StartActivity.this,MobileAuthActivity.class);
                startActivity(i);
            }
        });
        mregbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg_Intent=new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(reg_Intent);
            }
        });

        mloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login_Intent=new Intent(StartActivity.this,LoginActivity.class);
                startActivity(login_Intent);
            }
        });
    }
}
