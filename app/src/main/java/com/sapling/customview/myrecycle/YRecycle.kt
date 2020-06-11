package com.sapling.customview.myrecycle

import android.view.View
import java.lang.Exception

import java.util.Stack

/**
 * create by cral
 * create at 2020/6/8
 */
class YRecycle(itemTypeCount: Int) {
    private  var stackList : Array<Stack<View>?>

    init {
        stackList = arrayOfNulls<Stack<View>>(itemTypeCount)
        for (i in 0 until itemTypeCount) {
            stackList[i] = Stack<View>()
        }
    }

    fun getRecycleView(type: Int): View? {
        try {
            return stackList[type]!!.pop()
        }catch (e : Exception){
            return null
        }

    }

    fun setRecycleView(view: View, type: Int) {
        stackList[type]!!.push(view)
    }

}
