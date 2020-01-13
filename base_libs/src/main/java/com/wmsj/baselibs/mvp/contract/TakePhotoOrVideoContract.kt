package com.wmsj.baselibs.mvp.contract

import android.content.Context

import com.wmsj.baselibs.base.IBaseView
import com.wmsj.baselibs.base.IPresenter

/**se
 * Created by tian
on 2019/8/5.
 */
class TakePhotoOrVideoContract {

    interface View : IBaseView {

        /**
         * 图片上传
         */
        fun setImageUploadResult(data: ArrayList<String>)

        /**
         * 视频上传
         */
        fun setVideoloadResult(data: String)

        /**
         * 发布
         */
        fun setPulishData(data: String)


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
         * 视频上传
         */
        fun videoUpload(file: String)

        /**
         * 随手拍发布
         */
        fun requestPulishData(map: Map<String, String>, file: String)

        /**
         * 活动报道发布
         */
        fun pushOrgReport(map: Map<String, String>)

    }
}