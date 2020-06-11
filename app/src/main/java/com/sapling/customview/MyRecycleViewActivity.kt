package com.sapling.customview

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.sapling.customview.myrecycle.DataBean
import com.sapling.customview.myrecycle.YRecycleAdapter
import com.sapling.customview.myrecycle.YRecycleView
import kotlinx.android.synthetic.main.activity_my_recycle_view.*

class MyRecycleViewActivity : AppCompatActivity() {

    var data = mutableListOf<DataBean>()
    var count = 30
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_recycle_view)


        for (i in 1..count){
            if (i%2 == 0){
                data.add(DataBean("我是"+i,R.mipmap.ic_launcher,0))
            }else{
                data.add(DataBean("我是"+i,R.mipmap.ic_launcher,1))
            }
        }


        var adapter = MyAdapter(data,this)
        recycleView.addAdapter(adapter)
        recycleView.onItemClickListener = object : YRecycleView.OnItemClickListener{
            override fun onItemClick(position: Int) {
                Toast.makeText(this@MyRecycleViewActivity,"click"+position,Toast.LENGTH_LONG).show()
            }

        }
    }

    class MyAdapter(data : MutableList<DataBean>,context: Context) : YRecycleAdapter{
        val data = data
        val context = context
        override fun getCount(): Int {
            return data.size
        }

        override fun getItemTypeCount(): Int {
            return 2
        }

        override fun getItemType(position: Int): Int {
            return  data.get(position).type
        }

        override fun getHeight(index: Int): Int {
            return 200
        }

        override fun onCreateViewHolder(viewType: Int, contentView: View?, parent: ViewGroup): View {
            if (viewType == 0){
                return LayoutInflater.from(context).inflate(R.layout.content_title,parent,false)
            }else{
                return LayoutInflater.from(context).inflate(R.layout.content_image,parent,false)
            }
        }

        override fun onBindViewHolder(position: Int, contentView: View): View {
            val tv = contentView.findViewById<TextView>(R.id.contentTv)
            tv?.let {
                tv.text = data.get(position).title
                it.setOnClickListener {
                    Toast.makeText(it.context,tv.text,Toast.LENGTH_LONG).show()
                }
            }
            return contentView
        }

    }
}
