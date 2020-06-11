package com.sapling.customview.myrecycle

import android.view.View
import android.view.ViewGroup

/**
 *  create by cral
 *  create at 2020/6/8
 **/
interface YRecycleAdapter {
    fun getCount() : Int
    fun getItemTypeCount() : Int
    fun getItemType(position : Int) : Int
    fun getHeight(index : Int) : Int
    fun onCreateViewHolder(viewType : Int,contentView : View?,parent : ViewGroup) : View
    fun onBindViewHolder(position: Int,contentView : View) : View
}