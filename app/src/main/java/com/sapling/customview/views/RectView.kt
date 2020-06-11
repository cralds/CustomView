package com.sapling.customview.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

/**
 * create by cral
 * create at 2020/5/21
 */
class RectView constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val paint: Paint

    init {
        paint = Paint()
        paint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        canvas.drawARGB(255, 139, 197, 186)

        val canvasWidth = canvas.width

        val canvasHeight = canvas.height

        val layerId = canvas.saveLayer(0f, 0f, canvasWidth.toFloat(), canvasHeight.toFloat(), null, Canvas.ALL_SAVE_FLAG)

        val r = canvasWidth / 3

        //正常绘制黄色的圆形

        paint.color = -0x33bc

        canvas.drawCircle(r.toFloat(), r.toFloat(), r.toFloat(), paint)

        //使用CLEAR作为PorterDuffXfermode绘制蓝色的矩形

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

        paint.color = -0x995501

        canvas.drawRect(r.toFloat(), r.toFloat(), r * 2.7f, r * 2.7f, paint)

        //最后将画笔去除Xfermode

        paint.xfermode = null

        canvas.restoreToCount(layerId)


    }
}
