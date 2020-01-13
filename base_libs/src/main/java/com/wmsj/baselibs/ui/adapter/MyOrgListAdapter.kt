package com.wmsj.baselibs.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.wmsj.baselibs.R
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.bean.MyOrgList
import com.wmsj.baselibs.recyclerview.RecyclerViewHolder
import com.wmsj.baselibs.recyclerview.adapter.CommonRecyclerAdapter

/**
 * 我的组织
on 2019/8/6.
 */
class MyOrgListAdapter(mContext: Context, activityListBean: ArrayList<MyOrgList>, layoutId: Int) : CommonRecyclerAdapter<MyOrgList>(mContext, activityListBean, layoutId) {

    companion object {
        private const val ITEM_TYPE_NORMAL = 1
        private const val ITEM_TYPE_EMPTY = 2
    }

    override fun bindData(holder: RecyclerViewHolder, data: MyOrgList, position: Int) {
        when (getItemViewType(position)) {
            ITEM_TYPE_NORMAL -> {
                holder.setImagePath(R.id.iv_image, object : RecyclerViewHolder.HolderImageLoader(Const.BASE_URL + data.logo) {
                    override fun loadImage(iv: ImageView, path: String) {
                        var requestOptions = RequestOptions()
                        requestOptions.error(R.mipmap.zhi_default).placeholder(R.mipmap.zhi_default)
                        Glide.with(mContext)
                                .load(path)
                                .apply(requestOptions)
                                .into(iv)
                    }
                })
                holder.setText(R.id.tv_title, data.org_name)
                holder.setText(R.id.tv_location, "地址:" + data.address)
                holder.setText(R.id.tv_service_duration, data.service_count)
                holder.setText(R.id.tv_member_num, data.count)
                holder.setText(R.id.tv_create_time, data.inserttime)
                holder.setViewVisibility(R.id.ll_bottom, View.VISIBLE)
                holder.setViewVisibility(R.id.tv_right, View.VISIBLE)
                holder.setViewVisibility(R.id.tv_left, View.GONE)
                when (data.state) {
                    "1" -> {
                        holder.getView<LinearLayout>(R.id.ll_content).background = mContext.resources.getDrawable(R.mipmap.activity_background)
                        holder.setText(R.id.tv_right, mContext.resources.getString(R.string.exit_org))
                    }
                    "0" -> {
                        holder.getView<LinearLayout>(R.id.ll_content).background = mContext.resources.getDrawable(R.mipmap.background_gray)
                        holder.setText(R.id.tv_right, mContext.resources.getString(R.string.apply_exit_org))
                    }
                }
                holder.getView<TextView>(R.id.tv_right).setOnClickListener { childClickListener?.onItemChidClick(it, position) }
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
                RecyclerViewHolder(inflaterView(R.layout.activity_list_item, parent))
        }

    }


}