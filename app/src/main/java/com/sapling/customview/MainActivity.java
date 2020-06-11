package com.sapling.customview;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.sapling.customview.views.MusicProgressView;

import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer ;
    private MusicProgressView musicProgressView;
    private ImageView ivPausePlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivPausePlay = findViewById(R.id.ivPausePlay);
        musicProgressView = findViewById(R.id.musicProgressView);
        initProgressviewListener();
        ivPausePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer.isPlaying()){
                    mMediaPlayer.pause();
                    musicProgressView.pause();
                    ivPausePlay.setImageResource(R.drawable.icon_dialog_music_pause);
                }else {
                    mMediaPlayer.start();
                    musicProgressView.continu();
                    ivPausePlay.setImageResource(R.drawable.icon_dialog_music_play);
                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                playMusic();
            }
        },1000);
    }

    private void playMusic(){

        if ( mMediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.

            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            initMediaListener();

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.test);
            try {
                mMediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
//                mMediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mMediaPlayer.prepare();
            } catch (IOException e) {
                mMediaPlayer = null;
            }
        }

    }

    private void initMediaListener(){
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                initProgressData();
                musicProgressView.setBarDuration(mMediaPlayer.getDuration());
                musicProgressView.progress(10);
                mMediaPlayer.start();
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                musicProgressView.stop();
                playMusic();
            }
        });
    }

    private void initProgressData() {
        int count = musicProgressView.getBarCount();
        String data = null;


        if (mMediaPlayer.getDuration() > 15000){
            int barcount = mMediaPlayer.getDuration()/15000*count;
            data = getRandomData(barcount);
        }else {
            data = getRandomData(count);
        }
        musicProgressView.start(data);
    }

    private String getRandomData(int size){
        String data = "";
        for (int i=0;i<size;i++){
            Random random = new Random();
            int ran = random.nextInt(32)+4;
            data += (float) ran/100;
            if (i != size-1){
                data +=",";
            }
        }
        return data;
    }

    private void initProgressviewListener(){
        musicProgressView.setiMusicProgressScroll(new MusicProgressView.IMusicProgressScroll() {
            @Override
            public void scrollEnd(int startTime) {
//                mStartTime = startTime;
                mMediaPlayer.seekTo(startTime);
                mMediaPlayer.start();
                Log.d("initProgrer",""+startTime);
            }

            @Override
            public void scrollMove() {
                mMediaPlayer.pause();
            }

            @Override
            public void progress(int progress) {
//                tvStartTime.setText(AppUtils.transDurition(progress/1000+""));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.stop();
        mMediaPlayer = null;
        musicProgressView.stop();
    }
}
