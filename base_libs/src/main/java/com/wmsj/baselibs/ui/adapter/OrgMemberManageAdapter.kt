package com.wmsj.baselibs.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.wmsj.baselibs.R
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.bean.OrgMemberManageBean
import com.wmsj.baselibs.recyclerview.RecyclerViewHolder
import com.wmsj.baselibs.recyclerview.adapter.CommonRecyclerAdapter

/**
 * 首页最新活动adapter
on 2019/8/6.
 */
class OrgMemberManageAdapter(mContext: Context, memberList: ArrayList<OrgMemberManageBean>, layoutId: Int) : CommonRecyclerAdapter<OrgMemberManageBean>(mContext, memberList, layoutId) {

    companion object {
        private const val ITEM_TYPE_NORMAL = 1
        private const val ITEM_TYPE_EMPTY = 2
    }

    private var type = ""

    override fun bindData(holder: RecyclerViewHolder, data: OrgMemberManageBean, position: Int) {
        when (getItemViewType(position)) {
            ITEM_TYPE_NORMAL -> {
                holder.setImagePath(R.id.iv_logo, object : RecyclerViewHolder.HolderImageLoader(Const.BASE_URL + data.logo) {
                    override fun loadImage(iv: ImageView, path: String) {
                        var requestOptions = RequestOptions()
                        requestOptions.error(R.mipmap.author_default).placeholder(R.mipmap.author_default)
                        Glide.with(mContext)
                                .load(path)
                                .apply(requestOptions)
                                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                                .into(iv)
                    }
                })
                holder.setText(R.id.tv_name, data.name + "\u3000" + "(" + (if (data.sex == "2") "女" else "男") + ")")
                holder.setText(R.id.tv_time, "报名日期：" + data.registertime)
                holder.setText(R.id.tv_area, "所在地：" + data.place)
                when (type) {
                    "1" -> {
                        holder.setViewVisibility(R.id.tv_cancel, View.GONE)
                        holder.setViewVisibility(R.id.ll_wait, View.VISIBLE)
                        holder.setText(R.id.tv_employment, mContext.getString(R.string.go_on))
                        holder.setText(R.id.tv_refuse, mContext.getString(R.string.go_out))
                        holder.getView<TextView>(R.id.tv_employment).background = mContext.resources.getDrawable(R.drawable.shape_corner_ing)
                        holder.getView<TextView>(R.id.tv_refuse).background = mContext.resources.getDrawable(R.drawable.shape_corner_check)
                    }
                    "2" -> {
                        holder.setViewVisibility(R.id.tv_cancel, View.VISIBLE)
                        holder.setText(R.id.tv_cancel, mContext.getString(R.string.move))
                        holder.setViewVisibility(R.id.ll_wait, View.GONE)
                        holder.getView<TextView>(R.id.tv_cancel).background = mContext.resources.getDrawable(R.drawable.shape_corner_going)
                    }
                }
                setOnChildClickListener(holder.getView<TextView>(R.id.tv_cancel), position)
                setOnChildClickListener(holder.getView<TextView>(R.id.tv_employment), position)
                setOnChildClickListener(holder.getView<TextView>(R.id.tv_refuse), position)
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
                RecyclerViewHolder(inflaterView(R.layout.recruit_member_item, parent))
        }

    }

    fun setType(type: String) {
        this.type = type
    }


}