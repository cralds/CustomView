package com.sapling.customview.myrecycle

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import com.sapling.customview.R
import java.lang.RuntimeException

/**
 * create by cral
 * create at 2020/6/8
 * 自定义recycleview
 */
class YRecycleView(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs) {
    var viewList : MutableList<View>
    var isLoadView = true
    var adapter : YRecycleAdapter? = null
    lateinit var recycle : YRecycle

    var totalWidth = 0
    var totlaHeight = 0

    var scaleTouchSlop = 0
    var onItemClickListener : OnItemClickListener? = null

    lateinit var heights : Array<Int?>

    init {
        viewList = mutableListOf()
        isLoadView = true
        scaleTouchSlop = ViewConfiguration().scaledTouchSlop
    }

    fun addAdapter(adapter : YRecycleAdapter){
        this.adapter = adapter
        isLoadView = true
        recycle = YRecycle(adapter.getItemTypeCount())
        firstRow = 0
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (isLoadView && changed){
            isLoadView = false
            viewList.clear()
            removeAllViews()
            adapter?.let {
                val rowCount = it.getCount()
                 heights = arrayOfNulls<Int>(rowCount)

                for ( i in 0..(rowCount-1)){
                    heights[i] =  it.getHeight(i)
                }

                totalWidth = r - l
                totlaHeight = b - t

                var top = 0;
                var bottom = 0;
                for (i in 0..(rowCount-1)){
                    if (top > height){
                        break
                    }
                    bottom = top + heights[i]!!
                    var view = createViewAndSetUp(i ,l,top,r,bottom)
                    viewList.add(view!!)
                    top = bottom
                }
            }

        }
    }


    override fun removeView(view: View?) {
        super.removeView(view)
        var key = view?.getTag(R.id.item_tag) as Int
        recycle.setRecycleView(view,key)
    }


    //创建view并设置view的位置
    fun createViewAndSetUp(index : Int, l: Int, t: Int, r: Int, b: Int) : View? {
        var view = obtain(index,r-l,b-t)
        view.layout(l,t,r,b)

        return view
    }

    fun obtain(index : Int,width : Int,height : Int) : View{
        var view : View? = null

        var type = adapter!!.getItemType(index)

        //先从缓存中取，没有就调adapter创建
        var recycleView = recycle.getRecycleView(type)

        if (recycleView == null){
            view = adapter!!.onCreateViewHolder(type,null,this)
            if (view == null){
                throw RuntimeException("必须初始化")
            }
            view = adapter!!.onBindViewHolder(index,view)
        }else{
            view = adapter!!.onBindViewHolder(index,recycleView)
        }

        view.setTag(R.id.item_tag,type)
        view.measure(MeasureSpec.makeMeasureSpec(width,MeasureSpec.EXACTLY)
                ,MeasureSpec.makeMeasureSpec(height,MeasureSpec.AT_MOST))

        Log.d("sssssssssssss","width==="+view.measuredHeight)

        addView(view,0)
        view.setOnClickListener{
           onItemClickListener?.let {
               it.onItemClick(index)
           }
        }
        return view
    }


    //处理滑动
    var preMoveY = 0f
    var totalScrollY = 0f;//滑动总距离
    var firstRow = 0;//当前界面上的第一条数据

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        var isIntercept = false
        when(ev!!.action){
            MotionEvent.ACTION_DOWN -> {
                preMoveY = ev!!.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                val y = Math.abs(ev.rawY - preMoveY)
                if (y > scaleTouchSlop){
                    isIntercept = true
                }
            }
        }
        return isIntercept
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if(event!!.action == MotionEvent.ACTION_MOVE){
            var diff = preMoveY - event.rawY
            preMoveY = event.rawY
            scrollBy(0,diff.toInt())
        }
        return super.onTouchEvent(event)
    }

    override fun scrollBy(x: Int, y: Int) {
        // scrollY表示 第一个可见Item的左上顶点 距离屏幕的左上顶点的距离
        totalScrollY += y.toFloat()

        //当前第一项item在顶端，仍然下滑
        if (totalScrollY <= 0 && firstRow == 0){
            totalScrollY -= y
            return
        }

        totalScrollY = scrollBounds(totalScrollY)
        // totalScrollY
        if (totalScrollY > 0) {
            // 上滑正  下滑负  边界值
            while (totalScrollY > heights[firstRow]!!) {
                // 1 上滑移除  2 上划加载  3下滑移除  4 下滑加载
                var firstView = viewList.get(0)
                removeView(firstView)
                viewList.remove(firstView)
                totalScrollY -= heights[firstRow]!!
                firstRow++;
            }
            // 上滑添加
            while ((sumArray(firstRow,firstRow + viewList.size - 1) - totalScrollY) < height) {
                var addLast = firstRow + viewList.size
                if (addLast == heights.size){
                    totalScrollY -= y
                    return
                }
                var view = obtain(addLast, width, heights[addLast]!!)
                viewList.add(viewList.size, view);
            }
        }
        else if (totalScrollY < 0) {
            // 下滑加载
            while (totalScrollY < 0) {
                var firstAddRow = firstRow - 1;
                var view = obtain(firstAddRow, width, heights[firstAddRow]!!)
                viewList.add(0, view);
                firstRow--
                totalScrollY += heights[firstRow + 1]!!
            }
            // 下滑移除
            while (sumArray(firstRow, firstRow + viewList.size - 1) - totalScrollY - heights[firstRow + viewList.size - 1]!! >= height) {
                var view = viewList.get(viewList.size - 1)
                removeView(view)
                viewList.remove(view)
            }
        }
        reLayout()
    }

    fun scrollBounds( scrollY : Float): Float {
        var result = scrollY
        if (scrollY.toInt() == 0) {
        } else if(scrollY < 0) {
            // 极限值  会取零  非极限值的情况下   scroll
            result = Math.max(scrollY.toInt(), -sumArray(0, firstRow)).toFloat();
        }else{
            result = Math.min(scrollY.toInt(),Math.max(0,sumArray(firstRow,heights.size))).toFloat()
        }
        return result;
    }

    fun reLayout(){
        var top = -totalScrollY.toInt()
        var bottom = 0;
        for (i in 0..(viewList.size-1)){
            var hei = heights[firstRow + i]!!
            var view = viewList.get(i)
            bottom = top + hei
            view.layout(0,top,width,bottom)
            top = bottom
        }
    }

    fun sumArray(firstIndex :Int,lastIndex : Int) : Int{
        var result = 0;
        for (i in firstIndex..(lastIndex-1)){
            result += heights[i]!!
        }
        return result
    }


    interface OnItemClickListener{
        fun onItemClick(position:Int)
    }
}
