package com.wmsj.baselibs.ui.adapter

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import cn.jzvd.JzvdStd
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.wmsj.baselibs.R
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.recyclerview.RecyclerViewHolder
import com.wmsj.baselibs.recyclerview.adapter.CommonRecyclerAdapter
import com.wmsj.baselibs.utils.FileUtils
import java.net.URLConnection


/**
 * Created by tian
on 2019/8/12.
 */
class TakePhotoAdapter(mContext: Context, photoList: ArrayList<String>, layoutId: Int) : CommonRecyclerAdapter<String>(mContext, photoList, layoutId) {


    override fun bindData(holder: RecyclerViewHolder, data: String, position: Int) {
        if (data == "addImage") {
            holder.setViewVisibility(R.id.jz_video, View.GONE)
            holder.setViewVisibility(R.id.iv_image, View.VISIBLE)
            holder.setImageResource(R.id.iv_image, R.mipmap.add_image)
            holder.setViewVisibility(R.id.iv_delete, View.GONE)
        } else {
            holder.setViewVisibility(R.id.iv_delete, View.VISIBLE)
            holder.setImagePath(R.id.iv_image, object : RecyclerViewHolder.HolderImageLoader(if (data.contains("/uploads/")) Const.BASE_URL + data else data) {
                override fun loadImage(iv: ImageView, path: String) {
                    var requestOptions = RequestOptions()
                    requestOptions.error(R.mipmap.icon_image_default).placeholder(R.mipmap.icon_image_default)
                    Glide.with(mContext)
                            .load(path)
                            .apply(requestOptions)
                            .into(iv)
                }
            })
            holder.getView<ImageView>(R.id.iv_delete).setOnClickListener {
                onItemDelete.onItemDelete(position)
            }

            if (isVedioFile(data)) {
                holder.setViewVisibility(R.id.iv_image, View.INVISIBLE)
                holder.setViewVisibility(R.id.jz_video, View.VISIBLE)
                holder.getView<JzvdStd>(R.id.jz_video).setUp(data, "", JzvdStd.SCREEN_NORMAL)
                holder.getView<JzvdStd>(R.id.jz_video).thumbImageView.setImageBitmap(FileUtils.getVideoThumbnail(data))
            } else {
                holder.setViewVisibility(R.id.jz_video, View.GONE)
                holder.setViewVisibility(R.id.iv_image, View.VISIBLE)
            }

        }
    }

    private lateinit var onItemDelete: onItemDeleteListener

    fun setOnItemDeleteListener(onItemDelete: onItemDeleteListener) {
        this.onItemDelete = onItemDelete
    }

    interface onItemDeleteListener {
        fun onItemDelete(position: Int)
    }

    private val PREFIX_VIDEO = "video/"

    /**
     * Get the Mime Type from a File
     * @param fileName 文件名
     * @return 返回MIME类型
     * thx https://www.oschina.net/question/571282_223549
     * add by fengwenhua 2017年5月3日09:55:01
     */
    private fun getMimeType(fileName: String): String {
        val fileNameMap = URLConnection.getFileNameMap()
        return fileNameMap.getContentTypeFor(fileName)
    }

    /**
     * 根据文件后缀名判断 文件是否是视频文件
     * @param fileName 文件名
     * @return 是否是视频文件
     */
    fun isVedioFile(fileName: String): Boolean {
        val mimeType = getMimeType(fileName)
        return !TextUtils.isEmpty(fileName) && mimeType.contains(PREFIX_VIDEO)
    }
}