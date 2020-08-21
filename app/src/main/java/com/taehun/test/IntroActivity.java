package com.taehun.test;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class IntroActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro); //xml , java 소스 연결
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent = new Intent (getApplicationContext(), MainActivity.class);
                startActivity(intent); //다음화면으로 넘어감

                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        },500); //3초 뒤에 Runner객체 실행하도록 함
    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
}