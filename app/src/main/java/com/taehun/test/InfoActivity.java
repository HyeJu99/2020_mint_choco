package com.taehun.test;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class InfoActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ImageButton back_button_2=findViewById(R.id.back_button_2);
        back_button_2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
                overridePendingTransition(R.anim.not_move,R.anim.rightout);
            }

        });
    }

    @Override
    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.not_move,R.anim.rightout);
    }
}