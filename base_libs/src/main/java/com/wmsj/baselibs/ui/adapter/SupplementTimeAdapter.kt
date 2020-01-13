package com.wmsj.baselibs.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
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
class SupplementTimeAdapter(mContext: Context, memberList: ArrayList<Member>, layoutId: Int) : CommonRecyclerAdapter<Member>(mContext, memberList, layoutId) {

    companion object {
        private const val ITEM_TYPE_NORMAL = 1
        private const val ITEM_TYPE_EMPTY = 2
    }

//background_shape
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
                setOnChildClickListener(holder.getView<RelativeLayout>(R.id.tv_enter), position)
                if(data.isSelect){
                    holder.getView<LinearLayout>(R.id.ll_itemview).background = mContext.resources.getDrawable(R.drawable.background_shape)
                }else{
                    holder.getView<LinearLayout>(R.id.ll_itemview).background = mContext.resources.getDrawable(R.drawable.layout_border)
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
                RecyclerViewHolder(inflaterView(R.layout.supplement_time_item, parent))
        }

    }

}