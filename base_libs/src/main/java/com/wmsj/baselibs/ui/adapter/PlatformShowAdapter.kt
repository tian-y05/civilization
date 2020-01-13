package com.wmsj.baselibs.ui.adapter

import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.wmsj.baselibs.recyclerview.RecyclerViewHolder
import com.wmsj.baselibs.recyclerview.adapter.CommonRecyclerAdapter
import com.wmsj.baselibs.R
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.bean.HomeColumnsBean

/**
 * Created by tian
on 2019/8/7.
 */
class PlatformShowAdapter(mContext: Context, categoryList: ArrayList<HomeColumnsBean>, layoutId: Int) : CommonRecyclerAdapter<HomeColumnsBean>(mContext, categoryList, layoutId) {

    private var itemViewList = intArrayOf(R.drawable.half_round_corner_one, R.drawable.half_round_corner_two, R.drawable.half_round_corner_three, R.drawable.half_round_corner_four, R.drawable.half_round_corner_five, R.drawable.half_round_corner_six)

    override fun bindData(holder: RecyclerViewHolder, data: HomeColumnsBean, position: Int) {
        holder.getView<LinearLayout>(R.id.ll_content).setBackgroundResource(itemViewList[position % 6])
        holder.setImagePath(R.id.iv_image, object : RecyclerViewHolder.HolderImageLoader(Const.BASE_URL + data.logo) {
            override fun loadImage(iv: ImageView, path: String) {
                Glide.with(mContext)
                        .load(path)
                        .into(iv)
            }
        })
        data.name?.let { holder.setText(R.id.tv_name, it) }

    }
}