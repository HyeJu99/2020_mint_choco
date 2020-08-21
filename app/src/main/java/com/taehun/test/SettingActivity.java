package com.taehun.test;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;

public class SettingActivity extends FragmentActivity {

    Switch switch1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ImageButton back_button_1=findViewById(R.id.back_button_1);
        back_button_1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
                overridePendingTransition(R.anim.not_move,R.anim.rightout);
            }

        });

        switch1 = findViewById(R.id.switch1);
        switch1.setChecked(true);
    }

    @Override
    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.not_move,R.anim.rightout);
    }
}