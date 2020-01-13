package com.wmsj.baselibs.view

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import com.wmsj.baselibs.R


/**
 * Created by tian
on 2019/8/6.
 */
class CommonLoadingDialog private constructor(context: Context, theme: Int) : Dialog(context, theme) {
    companion object {
        private lateinit var mDialog: CommonLoadingDialog


        fun buildDialog(context: Context, layoutId: Int): CommonLoadingDialog {
            //根据指定主题样式创建Dialog
            mDialog = CommonLoadingDialog(context, R.style.CommonDialogStyle)
            //设置Dialog的布局
            mDialog.setContentView(layoutId)
            //点击或按返回键时消失
            mDialog.setCancelable(true)
            //点击对话框外的部分不消失.
            mDialog.setCanceledOnTouchOutside(false)
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