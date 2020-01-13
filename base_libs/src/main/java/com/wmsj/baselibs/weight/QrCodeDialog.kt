package com.wmsj.baselibs.weight

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.wmsj.baselibs.R

/**
 * Created by tian
on 2019/8/6.
 */
class QrCodeDialog private constructor(context: Context, theme: Int) : Dialog(context, theme) {
    companion object {
        private lateinit var mDialog: QrCodeDialog


        fun buildDialog(context: Context, url: String): QrCodeDialog {
            //根据指定主题样式创建Dialog
            mDialog = QrCodeDialog(context, R.style.CommonDialogStyle)

            //设置Dialog的布局
            mDialog.setContentView(R.layout.qr_code_dialog)
            //点击或按返回键时消失
            mDialog.setCancelable(true)
            //点击对话框外的部分不消失.
            mDialog.setCanceledOnTouchOutside(true)
            var imageView = mDialog.findViewById<ImageView>(R.id.iv_code)
            Glide.with(context).load(url).into(imageView)
            //设置对话框居中
            mDialog.window.attributes.gravity = Gravity.CENTER
            val lp = mDialog.window.attributes
            lp.dimAmount = 0.2f
            //设置属性
            mDialog.window.attributes = lp
            return mDialog
        }
    }

}