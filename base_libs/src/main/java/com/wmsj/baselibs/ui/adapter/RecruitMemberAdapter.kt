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
import com.wmsj.baselibs.bean.Member
import com.wmsj.baselibs.recyclerview.RecyclerViewHolder
import com.wmsj.baselibs.recyclerview.adapter.CommonRecyclerAdapter
import com.wmsj.baselibs.utils.StringUtils

/**
 * 首页最新活动adapter
on 2019/8/6.
 */
class RecruitMemberAdapter(mContext: Context, memberList: ArrayList<Member>, layoutId: Int) : CommonRecyclerAdapter<Member>(mContext, memberList, layoutId) {

    companion object {
        private const val ITEM_TYPE_NORMAL = 1
        private const val ITEM_TYPE_EMPTY = 2
        private const val ITEM_TYPE_JOIN = 3
    }

    private var type = ""

    override fun bindData(holder: RecyclerViewHolder, data: Member, position: Int) {
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
                holder.setText(R.id.tv_name, data.name + "\u3000" + "(" + (if (data.sex == 2) "女" else "男") + ")")
                holder.setText(R.id.tv_time, "报名日期：" + data.registertime)
                holder.setText(R.id.tv_area, "所在地：" + data.place)
                when (type) {
                    "1" -> {
                        holder.setViewVisibility(R.id.tv_cancel, View.VISIBLE)
                        holder.setViewVisibility(R.id.ll_wait, View.GONE)
                    }
                    "2" -> {
                        holder.setViewVisibility(R.id.tv_cancel, View.GONE)
                        holder.setViewVisibility(R.id.ll_wait, View.VISIBLE)
                    }
                }
                setOnChildClickListener(holder.getView<ImageView>(R.id.iv_logo), position)
                setOnChildClickListener(holder.getView<TextView>(R.id.tv_cancel), position)
                setOnChildClickListener(holder.getView<TextView>(R.id.tv_employment), position)
                setOnChildClickListener(holder.getView<TextView>(R.id.tv_refuse), position)
            }
            ITEM_TYPE_JOIN -> {
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
                data.name?.let { holder.setText(R.id.tv_name, it) }

                holder.setText(R.id.tv_time, data.registertime)
                holder.setText(R.id.tv_sign_in_time, if (StringUtils.isEmpty(data.start_time)) "——" else data.start_time)
                holder.setText(R.id.tv_sign_out_time, if (StringUtils.isEmpty(data.end_time)) "——" else data.end_time)
                holder.setText(R.id.tv_service_time, if (data.clock_type == "1") data.duration.toString() + "(代录)" else data.duration.toString() + "(自签)")
                if (!StringUtils.isEmpty(data.end_time)) {
                    holder.setText(R.id.tv_state, mContext.getString(R.string.sign_in_has))
                    holder.getView<TextView>(R.id.tv_state).setTextColor(mContext.resources.getColor(R.color.text_red))
                } else {
                    holder.setText(R.id.tv_state, mContext.getString(R.string.sign_out))
                    holder.getView<TextView>(R.id.tv_state).setTextColor(mContext.resources.getColor(R.color.text_black_color))
                }
                if (StringUtils.isEmpty(data.start_time)) {
                    holder.setText(R.id.tv_state, mContext.getString(R.string.sign_in))
                    holder.getView<TextView>(R.id.tv_state).setTextColor(mContext.resources.getColor(R.color.text_black_color))
                }
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (mData.size == 0)
            ITEM_TYPE_EMPTY
        else
            if (type == "3")
                ITEM_TYPE_JOIN
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
            ITEM_TYPE_JOIN ->
                RecyclerViewHolder(inflaterView(R.layout.acts_detail_item, parent))
            else ->
                RecyclerViewHolder(inflaterView(R.layout.recruit_member_item, parent))
        }

    }

    fun setType(type: String) {
        this.type = type
    }


}