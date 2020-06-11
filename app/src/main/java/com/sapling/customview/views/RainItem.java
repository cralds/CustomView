package com.sapling.customview.views;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

/**
 * create by cral
 * create at 2020/4/13
 **/
public class RainItem {
    private int height;
    private int width;
    private float startX;
    private float startY;
    private float stopX;
    private float stopY;
    private float sizeX;
    private float sizeY;
    private float of = 0.1f;
    private Paint paint;
    private Random random = new Random();

    public RainItem(int height,int width) {
        this.height = height;
        this.width = width;
        init();
    }

    public void init() {

        //startx和y对应的分别是起止位置
        sizeX = 10 + random.nextInt(10);
        sizeY = 20 + random.nextInt(20);
//        sizeX = 10 ;
//        sizeY = 30;
        startX = random.nextInt(width);
        startY = random.nextInt(height);
        stopX = startX + sizeX;
        stopY = startY + sizeY;
//        of = (float) (0.2 + random.nextFloat());
        paint = new Paint();
    }
    /**
     * 绘画雨滴
     * @param canvas
     */
    public void draw(Canvas canvas) {
        paint.setARGB(255, random.nextInt(255), random.nextInt(255), random.nextInt(255));
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }
    /**
     * 雨滴的移动行为
     */
    public void movestep() {
        //size*of这个是用来控制速度,所谓的速度就是线条增加的速度
        startX += sizeX*of;
        stopX += sizeX*of;

        startY += sizeY*of;
        stopY += sizeY*of;
        //如果超出边界则重新运行
        if (startY>height) {
            init();
        }
    }
}
