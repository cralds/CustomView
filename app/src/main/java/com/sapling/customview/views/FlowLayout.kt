package com.sapling.customview.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

/**
 *  create by cral
 *  create at 2020/6/11
 **/
class FlowLayout(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    var viewList = mutableListOf<MutableList<View>>()
    var rowHeight = mutableListOf<Int>()//每一行的最大高度


//    override fun generateDefaultLayoutParams(): LayoutParams {
//        return MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
//    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(getContext(),attrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        viewList.clear()
        rowHeight.clear()
        var realWidht = 0;//布局最终宽度
        var realHeight = 0;//布局最终高度

        var width = MeasureSpec.getSize(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)
        var widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var heightMode = MeasureSpec.getMode(heightMeasureSpec)

        var count = childCount -1

        var rowTotalWidth = 0//每行的总宽度
        var maxHeight = 0//每行的最大高度

        //每行的view
        var rowList = mutableListOf<View>()
        for (x in 0..count){
            var view = getChildAt(x)
            view.measure(MeasureSpec.makeMeasureSpec(width,MeasureSpec.AT_MOST),
                    MeasureSpec.makeMeasureSpec(height,MeasureSpec.AT_MOST))
            val childWidth = view.measuredWidth
            val childHeight = view.measuredHeight



            var layoutParams = view.layoutParams as MarginLayoutParams
            var leftMargin = layoutParams.leftMargin
            var topMargin = layoutParams.topMargin

            rowTotalWidth += childWidth + leftMargin

                    if (rowTotalWidth > width){//需要换行
                rowHeight.add(maxHeight)//记录每行最大高度
                realHeight += maxHeight + topMargin

                rowTotalWidth = 0
                maxHeight = childHeight //下一行第一个view高度设置给下一行的最大高度

                viewList.add(rowList)
                rowList = mutableListOf()
                rowList.add(view)
                rowTotalWidth += childWidth + leftMargin


                if (x == count){
                    viewList.add(rowList)
                    rowHeight.add(childHeight)
                    realHeight += childHeight + topMargin
                }

            }else{
                rowList.add(view)
                maxHeight = Math.max(maxHeight,childHeight)

                realWidht = Math.max(realWidht,rowTotalWidth)

                if (x == count){
                    realHeight += maxHeight + topMargin
                    viewList.add(rowList)
                    rowHeight.add(maxHeight)
                }

            }

        }

       setMeasuredDimension(realWidht,realHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left = 0
        var right = 0
        var top = 0
        var bottom = 0

        for ((index,itemViewList) in viewList.withIndex()){

            var maxTop = 0;
            for (view in itemViewList){
                var params =  view.layoutParams as MarginLayoutParams
                maxTop = Math.max(maxTop,params.topMargin)
                right = left + view.measuredWidth
                bottom = top + view.measuredHeight
                view.layout(left + params.marginStart,top + params.topMargin,right + params.marginStart,bottom + params.topMargin)


                left = right + params.marginStart
            }
            left = 0
            top += rowHeight[index] + maxTop

        }
    }

}