package com.sapling.customview.views;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * create by cral
 * create at 2020/4/14
 **/
public class ParticleValueAnimation extends ValueAnimator {
    private View container;
    private List<BaseParticle> list;
    public ParticleValueAnimation(View container) {
        setDuration(800);
        setFloatValues(0f,1f);
        this.container = container;
        this.list = new ArrayList<>();
    }

    public void addBean(BaseParticle particle){
        list.add(particle);
    }

    public void draw(Canvas canvas){
        for (BaseParticle particle : list){
            particle.draw(canvas, (Float) getAnimatedValue());
        }
        container.invalidate();
    }

    @Override
    public void start() {
        super.start();
        container.invalidate();
    }
}
