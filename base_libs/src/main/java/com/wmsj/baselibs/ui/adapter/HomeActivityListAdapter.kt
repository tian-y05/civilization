package com.wmsj.baselibs.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.wmsj.baselibs.recyclerview.RecyclerViewHolder
import com.wmsj.baselibs.recyclerview.adapter.CommonRecyclerAdapter
import com.wmsj.baselibs.R
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.bean.HomeActivityListBean


/**
 * 首页最新活动adapter
on 2019/8/6.
 */
class HomeActivityListAdapter(mContext: Context, newsHomeActivityListBean: ArrayList<HomeActivityListBean>, layoutId: Int) : CommonRecyclerAdapter<HomeActivityListBean>(mContext, newsHomeActivityListBean, layoutId) {

    companion object {
        private const val ITEM_TYPE_NORMAL = 1
        private const val ITEM_TYPE_EMPTY = 2
    }

    override fun bindData(holder: RecyclerViewHolder, data: HomeActivityListBean, position: Int) {
        when (getItemViewType(position)) {
            ITEM_TYPE_NORMAL -> {
                holder.setImagePath(R.id.iv_activity, object : RecyclerViewHolder.HolderImageLoader(Const.BASE_URL + data.activity_logo) {
                    override fun loadImage(iv: ImageView, path: String) {
                        var requestOptions = RequestOptions()
                        requestOptions.error(R.mipmap.news_normal)
                                .placeholder(R.mipmap.news_normal)
                        Glide.with(mContext)
                                .load(path)
                                .apply(requestOptions)
                                .into(iv)
                    }
                })
                holder.setText(R.id.tv_title, data.activity_name)
                holder.setText(R.id.tv_num, data.sign_num.toString() + "/" + data.recruitment)
                holder.setText(R.id.tv_location, data.org_cname)
                when(data.progress_type){
                    1->holder.setImageResource(R.id.iv_progress,R.mipmap.recruit_ing)
                    2->holder.setImageResource(R.id.iv_progress,R.mipmap.activity_going)
                    3->holder.setImageResource(R.id.iv_progress,R.mipmap.recruit_before)
                    4->holder.setImageResource(R.id.iv_progress,R.mipmap.activity_over)
                }
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (mData.size == 0)
            ITEM_TYPE_EMPTY
        else
            ITEM_TYPE_NORMAL
    }

    override fun getItemCount(): Int {
        return if (mData.size == 0)
            1
        else
            mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return when (viewType) {
            ITEM_TYPE_EMPTY ->
                RecyclerViewHolder(inflaterView(R.layout.empty_view, parent))
            else ->
                RecyclerViewHolder(inflaterView(R.layout.fragment_home_news, parent))
        }

    }


}