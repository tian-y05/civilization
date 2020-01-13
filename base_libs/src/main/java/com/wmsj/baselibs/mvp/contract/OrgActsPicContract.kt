package com.wmsj.baselibs.mvp.contract

import android.content.Context
import com.wmsj.baselibs.base.IBaseView
import com.wmsj.baselibs.base.IPresenter

/**se
 * Created by tian
on 2019/8/5.
 */
class OrgActsPicContract {

    interface View : IBaseView {

        /**
         * 图片上传
         */
        fun setImageUploadResult(data: ArrayList<String>)

        /**
         * 活动图片上传
         */
        fun pulishDataResult(data: String)


        /**
         * 显示错误信息
         */
        fun showError(msg: String, errorCode: Int)

    }


    interface Presenter : IPresenter<View> {

        /**
         * 图片上传
         */
        fun imageUpload(mContext: Context, files: ArrayList<String>)

        /**
         * 活动图片上传
         */
        fun pulishData(map: Map<String, String>)


    }
}