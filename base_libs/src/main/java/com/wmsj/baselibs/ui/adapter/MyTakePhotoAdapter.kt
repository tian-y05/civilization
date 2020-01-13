package com.wmsj.baselibs.ui.adapter

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cn.jzvd.JzvdStd
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.wmsj.baselibs.R
import com.wmsj.baselibs.ui.activity.PhotoShowActivity
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.bean.MyTakePhotoBean
import com.wmsj.baselibs.recyclerview.RecyclerViewHolder
import com.wmsj.baselibs.recyclerview.adapter.CommonRecyclerAdapter
import com.wmsj.baselibs.utils.IntentUtils
import com.wmsj.baselibs.utils.StringUtils
import com.wmsj.baselibs.view.MyGridView

/**
 * Created by tian
on 2019/8/12.
 */
class MyTakePhotoAdapter(mContext: Context, photoList: ArrayList<MyTakePhotoBean>, layoutId: Int) : CommonRecyclerAdapter<MyTakePhotoBean>(mContext, photoList, layoutId) {

    companion object {
        private const val ITEM_TYPE_NORMAL = 1
        private const val ITEM_TYPE_EMPTY = 2
    }

    override fun bindData(holder: RecyclerViewHolder, data: MyTakePhotoBean, position: Int) {
        when (getItemViewType(position)) {
            ITEM_TYPE_NORMAL -> {
                holder.setImagePath(R.id.iv_logo, object : RecyclerViewHolder.HolderImageLoader(Const.BASE_URL + data.user_logo) {
                    override fun loadImage(iv: ImageView, path: String) {
                        var requestOptions = RequestOptions()
                        requestOptions.error(R.mipmap.author_default).placeholder(R.mipmap.author_default)
                        Glide.with(mContext)
                                .load(path)
                                .apply(requestOptions)
                                .into(iv)
                    }
                })
                data.username?.let { holder.setText(R.id.tv_author, it) }
                if (StringUtils.isEmpty(data.cul_content)) {
                    holder.setViewVisibility(R.id.tv_content, View.GONE)
                } else {
                    holder.setViewVisibility(R.id.tv_content, View.VISIBLE)
                    data.cul_content?.let { holder.setText(R.id.tv_content, it) }
                }

                if (data.cul_address != null && !TextUtils.isEmpty(data.cul_address)) {
                    holder.setViewVisibility(R.id.tv_address, View.VISIBLE)
                    holder.setText(R.id.tv_address, data.cul_address!!)
                }else{
                    holder.setViewVisibility(R.id.tv_address, View.GONE)
                }
                data.cul_createtime?.let { holder.setText(R.id.tv_time, it) }
                if (data.cul_video != null && data.cul_video.isNotEmpty()) {
                    holder.setViewVisibility(R.id.gv_photos, View.GONE)
                    holder.setViewVisibility(R.id.jz_video, View.VISIBLE)
                    holder.getView<JzvdStd>(R.id.jz_video).setUp(Const.BASE_URL + data.cul_video[0], "", JzvdStd.SCREEN_NORMAL)
                    Glide.with(mContext).load(Const.BASE_URL + data.cover).into(holder.getView<JzvdStd>(R.id.jz_video).thumbImageView)
                } else {
                    data.cul_images?.let {
                        holder.setViewVisibility(R.id.jz_video, View.GONE)
                        holder.setViewVisibility(R.id.gv_photos, View.VISIBLE)
                        holder.getView<MyGridView>(R.id.gv_photos).adapter = PhotoModelsAdapter(mContext, it.toTypedArray())
                        holder.getView<MyGridView>(R.id.gv_photos).setOnItemClickListener { parent, view, position, id ->
                            var bundle = Bundle()
                            bundle.putStringArray("photos", it.toTypedArray())
                            IntentUtils.to(mContext, PhotoShowActivity::class.java, bundle)
                        }
                    }
                }

                when (data.cul_state) {
                    1 -> {
                        holder.setViewVisibility(R.id.tv_delete, View.GONE)
                    }
                    0, -1 -> {
                        holder.setViewVisibility(R.id.tv_delete, View.VISIBLE)
                        holder.getView<TextView>(R.id.tv_delete).setOnClickListener { clickListener?.onDelete(position) }
                    }
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
                RecyclerViewHolder(inflaterView(R.layout.convent_photo_item, parent))
        }

    }

    var clickListener: onItemDeleteListener? = null
    fun setOnItemDeleteListener(clickListener: onItemDeleteListener) {
        this.clickListener = clickListener
    }

    interface onItemDeleteListener {
        fun onDelete(position: Int)
    }
}