package com.sapling.customview;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LoadActivity extends AppCompatActivity {

    public String[] items = new String[]{"粒子","波浪","进度条","自定义recycleview","流式布局"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        LinearLayout llMain = findViewById(R.id.llMain);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,3,0,0);
        for (int i=0;i<items.length;i++){
            String item = items[i];
            TextView tv = new TextView(this);
            tv.setText(item);
            tv.setPadding(30,30,30,30);
            tv.setBackgroundColor(Color.parseColor("#FFFFFF"));
            tv.setTextColor(Color.parseColor("#999999"));
            tv.setOnClickListener(new OnclickLintener(i));
            tv.setLayoutParams(params);
            llMain.addView(tv);
        }

    }

    public class OnclickLintener implements View.OnClickListener {
        private int position = 0;
        public OnclickLintener(int position){
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (position){
                case 0:
                    startActivity(new Intent(LoadActivity.this,ParticleActivity.class));
                    break;
                case 1:
                    startActivity(new Intent(LoadActivity.this,WaveActivity.class));
                    break;
                case 2:
                    startActivity(new Intent(LoadActivity.this,MainActivity.class));
                    break;
                case 3:
                    startActivity(new Intent(LoadActivity.this,MyRecycleViewActivity.class));
                    break;
                case 4:
                    startActivity(new Intent(LoadActivity.this,FlowTestActivity.class));
                    break;
            }
        }
    }
}
