package com.sapling.customview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * create by cral
 * create at 2020/4/13
 **/
public abstract class BaseView extends View {

    private control thread;

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseView(Context context) {
        super(context);
    }
    //封装,构造画面,子类继承后需要重写
    protected abstract void drawsub(Canvas canvas);
    //封装移动方法,子类继承后需要重写
    protected abstract void move();
    //封装的初始化方法
    protected abstract void init();
    @Override
    protected final void onDraw(Canvas canvas) {

        //启动线程
        if (thread ==null) {
            thread = new control();
            thread.start();
        }else {
            drawsub(canvas);
        }
    }

    public class control extends Thread{
        @Override
        public void run() {
            init();
            while(true){
                move();
                //相当于刷新画布
                postInvalidate();
                try {
                    sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}