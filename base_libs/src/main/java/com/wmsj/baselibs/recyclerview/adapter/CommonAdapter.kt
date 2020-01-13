package com.wmsj.baselibs.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

import com.wmsj.baselibs.recyclerview.ViewHolder

abstract class CommonAdapter<T>(var context: Context, var data: ArrayList<T>, var itemLayoutId: Int) : BaseAdapter() {
    private var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return if (data != null) {
            data!!.size
        } else 0
    }

    override fun getItem(position: Int): Any? {
        return if (data != null) {
            data!![position]
        } else null
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        val viewHolder = getViewHolder(position, convertView,
                parent)
        convert(viewHolder, getItem(position) as T, position)
        return viewHolder.convertView
    }

    abstract fun convert(helper: ViewHolder, item: T, position: Int)

    private fun getViewHolder(position: Int, convertView: View,
                              parent: ViewGroup): ViewHolder {
        return ViewHolder[context, convertView, parent, itemLayoutId, position]
    }
}
