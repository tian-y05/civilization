package com.wmsj.baselibs.weight

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.wmsj.baselibs.R

/**
 * Created by tian
on 2019/8/12.
 */
class TakePhotoDialog : Dialog {
    private lateinit var mContext: Context

    constructor(context: Context?, themeResId: Int) : super(context, themeResId) {
        context?.let { this.mContext = it }
        initView()
    }

    fun initView() {
        var view = LayoutInflater.from(mContext).inflate(R.layout.take_photo_dialog, null)
        var iv_photo = view.findViewById<ImageView>(R.id.iv_photo)
        var iv_video = view.findViewById<ImageView>(R.id.iv_video)
        view.findViewById<LinearLayout>(R.id.ll_photo).setOnClickListener {
            onItemClick?.let {
                Glide.with(mContext).load(R.mipmap.photo_select).into(iv_photo)
                Glide.with(mContext).load(R.mipmap.photo_unselect).into(iv_video)
                it.onPhotoClick()
            }
        }
        view.findViewById<LinearLayout>(R.id.ll_video).setOnClickListener {
            onItemClick?.let {
                it.onVideoClick()
                Glide.with(mContext).load(R.mipmap.photo_unselect).into(iv_photo)
                Glide.with(mContext).load(R.mipmap.photo_select).into(iv_video)
            }
        }
        setContentView(view)
        var layoutParams = window.attributes
        layoutParams.gravity = Gravity.CENTER
        layoutParams.width = mContext.resources.getDimensionPixelSize(R.dimen.dp_200)
        layoutParams.height = mContext.resources.getDimensionPixelSize(R.dimen.dp_100)

        window!!.attributes = layoutParams
    }


    private var onItemClick: onItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: onItemClickListener) {
        this.onItemClick = onItemClickListener
    }

    interface onItemClickListener {
        fun onPhotoClick()
        fun onVideoClick()
    }
}