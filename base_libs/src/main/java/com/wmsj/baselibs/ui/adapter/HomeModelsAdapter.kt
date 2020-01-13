package com.wmsj.baselibs.ui.adapter

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.wmsj.baselibs.R
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.bean.HomeColumnsBean

/**
 * Created by tian
on 2019/8/5.
 */
class HomeModelsAdapter(mContext: Context, categoryList: ArrayList<HomeColumnsBean>) : BaseAdapter() {

    private var mContext: Context? = null
    private var categoryList: ArrayList<HomeColumnsBean>? = null

    init {
        this.mContext = mContext
        this.categoryList = categoryList
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        if (categoryList == null) return null
        var holder: ViewHolder
        var view: View
        var homeColumnsBean = categoryList!![position]
        if (convertView == null) {
            view = View.inflate(mContext, R.layout.fragment_homemodel_item, null)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }
        holder.tvName.text = if (TextUtils.isEmpty(homeColumnsBean.name)) homeColumnsBean.app_name else homeColumnsBean.name
        if (!TextUtils.isEmpty(homeColumnsBean.app_name) && homeColumnsBean.app_name.equals("平台展示")) {
            Glide.with(mContext!!).load(R.mipmap.platform_display).apply(RequestOptions.bitmapTransform(CircleCrop())).into(holder.ivImage)
        } else {
            Glide.with(mContext!!).load(Const.BASE_URL + (if (TextUtils.isEmpty(homeColumnsBean.logo)) homeColumnsBean.app_logo else homeColumnsBean.logo)).apply(RequestOptions.bitmapTransform(CircleCrop())).into(holder.ivImage)
        }

        return view

    }

    override fun getItem(position: Int): Any? {
        return if (categoryList == null) null else categoryList!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return if (categoryList == null) 0 else categoryList!!.size
    }

    internal inner class ViewHolder(var view: View) {
        var ivImage: ImageView = view.findViewById(R.id.iv_image)
        var tvName: TextView = view.findViewById(R.id.tv_name)
    }

}