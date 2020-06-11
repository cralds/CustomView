package com.sapling.customview.views;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import java.util.Random;

/**
 * create by cral
 * create at 2020/4/14
 **/
public class ParticleBean extends BaseParticle{
    private float speed = 1f;
    private float x,y;
    private float startX;
    private float startY;
    private Paint paint;
    private Bitmap bitmap;
    private Random random = new Random();
    private float xSpeed,ySpeed;

    public ParticleBean( float startX, float startY, Bitmap bitmap,float speed) {
        this.speed = speed;
        this.x = startX;
        this.y = startY;
        this.startX = startX + (random.nextInt(100)-50);
        this.startY = startY + (random.nextInt(100)-50);

        this.bitmap = bitmap;
        paint = new Paint();

        xSpeed = random.nextInt(5) * speed;
        ySpeed = random.nextInt(5) * speed;;
    }
    @Override
    public void draw(Canvas canvas,float aniValue){
        if (bitmap == null){
            return;
        }
        calculate();
        Matrix matrix = new Matrix();
        matrix.postRotate(aniValue * 260f,bitmap.getWidth()/2,bitmap.getHeight()/2);
//        matrix.postScale(aniValue,aniValue,bitmap.getWidth()/2,bitmap.getHeight()/2);
        matrix.postTranslate(startX,startY);
//        matrix.postSkew(0.5f,0.5f,bitmap.getWidth()/2,bitmap.getHeight()/2);
        canvas.drawBitmap(bitmap,matrix,paint);
    }

    public void calculate(){
        //随机上下左右移动
        if (x > startX){
            startX -= xSpeed;
        }else {
            startX += xSpeed;
        }

        if (y > startY){
            startY -= ySpeed;
        }else {
            startY += ySpeed;
        }


    }


}
