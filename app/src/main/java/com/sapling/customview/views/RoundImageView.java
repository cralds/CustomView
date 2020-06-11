package com.sapling.customview.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import java.lang.reflect.Array;

public class RoundImageView extends android.support.v7.widget.AppCompatImageView{
    public RoundImageView(Context context) {
        super(context);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        // 获取当前控件的 drawable
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        // 这里 get 回来的宽度和高度是当前控件相对应的宽度和高度（在 ml 设置）
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        // 画笔
        Paint paint = new Paint();
        // 颜色设置
        paint.setColor(0xff424242);
        // 抗锯齿
        paint.setAntiAlias(true);
        // 获取 bitmap，即传入 imageview 的 bitmap
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        canvas.saveLayer(0, 0, getWidth(), getHeight(), null);
        // 画遮罩，画出来就是一个和空间大小相匹配的圆
        float radius = getWidth() / 2 >= getHeight() / 2 ? getHeight() / 2 : getWidth() / 2;
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, paint);
        // Paint 的 Xfermode，PorterDuff.Mode.SRC_IN 取两层图像的交集部门, 只显示上层图像。
        PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        paint.setXfermode(xfermode);
        // draw 上去
        canvas.drawBitmap(bitmap, 0, 0, paint);
        // paint.setXfermode(null);
        canvas.restore();

    }

}
