package com.wmsj.baselibs.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.wmsj.baselibs.R
import com.wmsj.baselibs.Const

/**
 * Created by tian
on 2019/8/5.
 */
class PhotoModelsAdapter(mContext: Context, photoLists: Array<String>) : BaseAdapter() {

    private var mContext: Context? = null
    private var photoLists: Array<String>? = null

    init {
        this.mContext = mContext
        this.photoLists = photoLists
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        if (photoLists == null) return null
        var holder: ViewHolder
        var view: View
        var photo = photoLists!![position]
        if (convertView == null) {
            view = View.inflate(mContext, R.layout.activity_photo_item, null)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

            Glide.with(mContext!!).load(Const.BASE_URL + photo).into(holder.ivImage)

        return view

    }

    override fun getItem(position: Int): Any? {
        return if (photoLists == null) null else photoLists!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return if (photoLists == null) 0 else photoLists!!.size
    }

    internal inner class ViewHolder(var view: View) {
        var ivImage: ImageView = view.findViewById(R.id.iv_image)
    }

}