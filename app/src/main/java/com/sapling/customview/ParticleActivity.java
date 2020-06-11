package com.sapling.customview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.sapling.customview.views.MyParticleView;

public class ParticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particle);

        final MyParticleView myParticleView = new MyParticleView(ParticleActivity.this);
        findViewById(R.id.llMain).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){

                    myParticleView.start(event.getX(),event.getY(),ParticleActivity.this);
//                    Rainplay rainplay = new Rainplay(ParticleActivity.this);
                }
                else if (event.getAction() == MotionEvent.ACTION_MOVE){
                    myParticleView.start(event.getX(),event.getY(),ParticleActivity.this);
                }
                return true;
            }
        });
    }
}
