package com.wmsj.baselibs.ui.adapter

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.wmsj.baselibs.recyclerview.RecyclerViewHolder
import com.wmsj.baselibs.recyclerview.adapter.CommonRecyclerAdapter
import com.wmsj.baselibs.R
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.bean.StationListBean

/**
 * Created by tian
on 2019/8/7.
 */
class StationContentAdapter(mContext: Context, articleList: ArrayList<StationListBean>, layoutId: Int) : CommonRecyclerAdapter<StationListBean>(mContext, articleList, layoutId) {


    override fun bindData(holder: RecyclerViewHolder, data: StationListBean, position: Int) {
        holder.setImagePath(R.id.iv_image,object : RecyclerViewHolder.HolderImageLoader(Const.BASE_URL + data.sta_logo) {
            override fun loadImage(iv: ImageView, path: String) {
                Glide.with(mContext)
                        .load(path)
                        .into(iv)
            }
        })
        holder.setText(R.id.tv_title,data.sta_name)
    }

}