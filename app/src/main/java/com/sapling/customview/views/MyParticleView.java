package com.sapling.customview.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.sapling.customview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * create by cral
 * create at 2020/4/13
 **/
public class MyParticleView extends View {
    private Paint paint;
    private float startX,startY;
    private Bitmap bitmap;
    private List<ParticleValueAnimation> animations;
    public MyParticleView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
        animations = new ArrayList<>();
       Drawable drawable = context.getResources().getDrawable(R.drawable.star_pink);
       if (drawable instanceof BitmapDrawable){
           BitmapDrawable animationDrawable = (BitmapDrawable) drawable;
           bitmap = ((BitmapDrawable)animationDrawable).getBitmap();
       }

        paint = new Paint();
        paint.setAntiAlias(true);
        attach2Activity((Activity) context);
    }
    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        for (ParticleValueAnimation animation : animations){
            animation.draw(canvas);
        }

    }

    public void start(float x, float y, Activity activity){
        startX = x;
        startY = y;
        final ParticleValueAnimation valueAnimation = new ParticleValueAnimation(this);

        for (int i=0;i<4;i++){
            ParticleBean bean = new ParticleBean(x,y,bitmap,1);
            valueAnimation.addBean(bean);
        }
        valueAnimation.start();
        animations.add(valueAnimation);
        valueAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animations.remove(valueAnimation);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    /**
     * 给Activity加上全屏覆盖的ExplosionField
     */
    private void attach2Activity(Activity activity) {
        ViewGroup rootView = activity.findViewById(Window.ID_ANDROID_CONTENT);

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rootView.addView(this, lp);
    }
}
