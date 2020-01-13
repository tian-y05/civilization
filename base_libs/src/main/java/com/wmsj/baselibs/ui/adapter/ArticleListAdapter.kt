package com.wmsj.baselibs.ui.adapter

import android.content.Context
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.wmsj.baselibs.R
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.bean.ArticleListBean
import com.wmsj.baselibs.recyclerview.RecyclerViewHolder
import com.wmsj.baselibs.recyclerview.adapter.CommonRecyclerAdapter

/**
 * Created by tian
on 2019/8/7.
 */
class ArticleListAdapter(mContext: Context, articleList: ArrayList<ArticleListBean>) : CommonRecyclerAdapter<ArticleListBean>(mContext, articleList, -1) {


    companion object {
        private const val ITEM_TYPE_PLAIN_TEXT = 0 //纯文本
        private const val ITEM_TYPE_PIC_TEXT = 1    //左图右文
        private const val ITEM_TYPE_BIG_VIDEO = 2    //大图视频
        private const val ITEM_TYPE_SMALL_VIDEO = 3    //小图视频

    }


    override fun bindData(holder: RecyclerViewHolder, data: ArticleListBean, position: Int) {
        when (getItemViewType(position)) {
            ITEM_TYPE_PLAIN_TEXT -> {
                holder.setText(R.id.tv_title, data.con_title)
                holder.setText(R.id.tv_source, data.con_source)
                holder.setText(R.id.tv_time, data.time)
            }

            ITEM_TYPE_PIC_TEXT, ITEM_TYPE_BIG_VIDEO, ITEM_TYPE_SMALL_VIDEO -> {
                holder.setText(R.id.tv_title, data.con_title)
                holder.setText(R.id.tv_source, data.con_source)
                holder.setText(R.id.tv_time, data.time)
                holder.setImagePath(R.id.iv_image, object : RecyclerViewHolder.HolderImageLoader(Const.BASE_URL + data.con_images) {
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
            }

            else -> {

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (!TextUtils.isEmpty(mData[position].con_video) || !TextUtils.isEmpty(mData[position].con_videourl)) {
            if (mData[position].con_type == 0) {
                ITEM_TYPE_SMALL_VIDEO
            } else {
                ITEM_TYPE_BIG_VIDEO
            }
        } else if (!TextUtils.isEmpty(mData[position].con_images)) {
            ITEM_TYPE_PIC_TEXT
        } else {
            ITEM_TYPE_PLAIN_TEXT
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return when (viewType) {
            ITEM_TYPE_PLAIN_TEXT ->
                RecyclerViewHolder(inflaterView(R.layout.article_plain_text, parent))
            ITEM_TYPE_PIC_TEXT ->
                RecyclerViewHolder(inflaterView(R.layout.article_pic_text, parent))
            ITEM_TYPE_BIG_VIDEO ->
                RecyclerViewHolder(inflaterView(R.layout.article_big_video, parent))
            ITEM_TYPE_SMALL_VIDEO ->
                RecyclerViewHolder(inflaterView(R.layout.article_small_video, parent))
            else ->
                RecyclerViewHolder(inflaterView(R.layout.empty_view, parent))
        }

    }
}