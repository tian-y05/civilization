package com.wmsj.baselibs.ui.adapter

import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.wmsj.baselibs.R
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.bean.StationListBean
import com.wmsj.baselibs.recyclerview.RecyclerViewHolder
import com.wmsj.baselibs.recyclerview.adapter.CommonRecyclerAdapter

/**
 * Created by tian
on 2019/8/7.
 */
class StationSpotAdapter(mContext: Context, articleList: ArrayList<StationListBean>, layoutId: Int) : CommonRecyclerAdapter<StationListBean>(mContext, articleList, layoutId) {


    override fun bindData(holder: RecyclerViewHolder, data: StationListBean, position: Int) {
        holder.setImagePath(R.id.iv_image, object : RecyclerViewHolder.HolderImageLoader(Const.BASE_URL + data.sta_logo) {
            override fun loadImage(iv: ImageView, path: String) {
                Glide.with(mContext)
                        .load(path)
                        .into(iv)
            }
        })
        holder.setText(R.id.tv_title, data.sta_name)
        holder.setText(R.id.tv_activity, String.format(mContext.resources.getString(R.string.station_activity), data.activity_num))
        holder.getView<LinearLayout>(R.id.ll_introduction).setOnClickListener {
            onItemClick.onIntroductionClick(position)
        }
        holder.getView<LinearLayout>(R.id.ll_activity).setOnClickListener {
            onItemClick.onActivityClick(position)
        }
    }

    private lateinit var onItemClick: onItemLookClickListener

    fun setOnItemLookClickListener(onItemClick: onItemLookClickListener) {
        this.onItemClick = onItemClick
    }

    interface onItemLookClickListener {
        fun onIntroductionClick(position: Int)
        fun onActivityClick(position: Int)
    }
}