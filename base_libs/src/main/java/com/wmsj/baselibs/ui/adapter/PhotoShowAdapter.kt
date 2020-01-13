package com.wmsj.baselibs.ui.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.chrisbanes.photoview.PhotoView
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.R


/**
 * Created by tian
on 2019/8/12.
 */
class PhotoShowAdapter(context: Context, photoLists: Array<String>) : PagerAdapter() {

    private var context: Context = context
    private var photoLists: Array<String> = photoLists

    override fun isViewFromObject(view: View, p1: Any): Boolean {
        return view == p1
    }

    override fun getCount(): Int {
        return photoLists.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var view = LayoutInflater.from(context).inflate(R.layout.photo_show_item, null)
        var requestOptions = RequestOptions()
        requestOptions.error(R.mipmap.news_normal)
                .placeholder(R.mipmap.news_normal)
        Glide.with(context).load(Const.BASE_URL + photoLists[position]).apply(requestOptions).into(view.findViewById<PhotoView>(R.id.photo_view))
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, p: Any) {
        container.removeView(p as View)
    }
}