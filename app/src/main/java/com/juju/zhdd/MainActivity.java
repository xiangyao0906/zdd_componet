package com.juju.zhdd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.billy.cc.core.component.CC;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.logInBtn).setOnClickListener(v -> CC.obtainBuilder("user_component").setActionName("showActivity").build().call());


    }
}