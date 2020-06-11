package com.sapling.customview.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import java.util.*

/**
 *  create by cral
 *  create at 2020/5/21
 *  不规则控件测试
 **/
class ControlView(context: Context,attr : AttributeSet) : View(context,attr) {
    lateinit var paint: Paint
    var listTop = ArrayList<PointF>()
    var listLeft = ArrayList<PointF>()
    var listRight = ArrayList<PointF>()
    var listBtm = ArrayList<PointF>()
    var regionTop : Region//判断坐标点区域
    init {
        paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.BLUE
        regionTop = Region()
    }

    fun initPath(){
        var viewWidth = width.toFloat()
        var viewHeight = height.toFloat()
        listTop.add(PointF(viewWidth/4,viewHeight/2))
        listTop.add(PointF(viewWidth/2,30f))
        listTop.add(PointF(viewWidth/4*3,viewHeight/2))
        listTop.add(PointF(viewWidth/4*3-50,viewHeight/4*3))
        listTop.add(PointF(viewWidth/4 + 50,viewHeight/4*3))

    }

    fun drawItem(canvas: Canvas,list: List<PointF>){
        var path = Path()
        path.moveTo(list.get(0).x,list.get(0).y)
        path.quadTo(list.get(1).x,list.get(1).y,list.get(2).x,list.get(2).y)
        path.lineTo(list.get(3).x,list.get(3).y)
        path.lineTo(list.get(4).x,list.get(4).y)
        path.close()
        regionTop.setPath(path, Region(0,0,1000,1000))
        canvas.drawPath(path,paint)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        initPath()
        drawItem(canvas!!,listTop)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var isInView = false
        when(event!!.action){
            MotionEvent.ACTION_DOWN -> {
                isInView = regionTop.contains(event.x.toInt(), event.y.toInt())
                if (isInView){
                    paint.color = Color.RED
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                isInView = regionTop.contains(event.x.toInt(), event.y.toInt())
                if (isInView){
                    paint.color = Color.BLUE
                    invalidate()
                }
            }
        }

        return true

    }
}