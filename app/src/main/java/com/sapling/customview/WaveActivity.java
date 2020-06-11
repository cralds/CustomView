package com.sapling.customview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sapling.customview.views.MyWaveView;

public class WaveActivity extends AppCompatActivity {
    MyWaveView myWaveView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave);
        myWaveView = findViewById(R.id.waveView);
        myWaveView.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myWaveView.stop();
    }
}
