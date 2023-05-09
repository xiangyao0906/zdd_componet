package com.juju.zhdd;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.billy.cc.core.component.CC;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.logInBtn).setOnClickListener(v -> CC.obtainBuilder("user_component").setActionName("showActivity").build().call());


    }
}