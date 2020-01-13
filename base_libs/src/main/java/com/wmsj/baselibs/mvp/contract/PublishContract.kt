package com.wmsj.baselibs.mvp.contract

import android.content.Context
import com.wmsj.baselibs.base.IBaseView
import com.wmsj.baselibs.base.IPresenter
import com.wmsj.baselibs.bean.ActivityShowBean

/**
 * Created by tian
on 2019/7/12.
 */
class PublishContract {

    interface View : IBaseView {

        /**
         * 发布活动
         */
        fun publishResult(data: String)

        /**
         * 失败
         */
        fun showError(errorMsg: String, errorCode: Int)

        /**
         * 图片上传
         */
        fun setImageUploadResult(data: ArrayList<String>)

        /**
         * 组织详情
         */
        fun setOrgInfoDetails(data: ActivityShowBean)
    }

    interface Presenter : IPresenter<View> {


        /**
         * 发布活动
         */
        fun publishActs(map: HashMap<String, String>)


        /**
         * 图片上传
         */
        fun imageUpload(mContext: Context, files: ArrayList<String>)

        /**
         * 我的组织详情
         */
        fun getMyOrgDetails(id: String)
    }
}