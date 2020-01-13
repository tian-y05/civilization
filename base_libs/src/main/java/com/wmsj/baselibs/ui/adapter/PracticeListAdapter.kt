package com.wmsj.baselibs.ui.adapter

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.wmsj.baselibs.recyclerview.RecyclerViewHolder
import com.wmsj.baselibs.recyclerview.adapter.CommonRecyclerAdapter
import com.wmsj.baselibs.R
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.bean.PracticeBaseBean

/**
 * Created by tian
on 2019/8/7.
 */
class PracticeListAdapter(mContext: Context, articleList: ArrayList<PracticeBaseBean>, layoutId: Int) : CommonRecyclerAdapter<PracticeBaseBean>(mContext, articleList, layoutId) {


    override fun bindData(holder: RecyclerViewHolder, data: PracticeBaseBean, position: Int) {
        holder.setImagePath(R.id.iv_image,object : RecyclerViewHolder.HolderImageLoader(Const.BASE_URL + data.pra_logo) {
            override fun loadImage(iv: ImageView, path: String) {
                Glide.with(mContext)
                        .load(path)
                        .into(iv)
            }
        })
        holder.setText(R.id.tv_title,data.pra_name)
    }

}