package com.sapling.customview.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.Window;


import java.util.ArrayList;
import java.util.List;

/**
 * create by cral
 * create at 2020/4/13
 **/
public class Rainplay extends BaseView {
    List<RainItem> list = new ArrayList<RainItem>();
    //控制雨滴的数量
    private int num = 40;

    public Rainplay(Context context) {
        super(context);
        attach2Activity((Activity) context);
    }

    public Rainplay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void drawsub(Canvas canvas) {
        for (RainItem item : list) {
            item.draw(canvas);
        }
    }

    @Override
    protected void move() {
        for (RainItem item : list) {
            item.movestep();
        }
    }
    /**
     * 因为获取长宽是放在layout之后才可以获取,所以需要
     * 放在线程里面初始化
     */
    @Override
    protected void init() {
        for (int i = 0; i < num; i++) {
            RainItem item = new RainItem(getHeight(), getWidth());
            list.add(item);
        }
    }

    private void attach2Activity(Activity activity) {
        ViewGroup rootView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rootView.addView(this, lp);
    }
}
