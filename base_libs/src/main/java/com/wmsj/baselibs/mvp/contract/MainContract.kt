package com.wmsj.baselibs.mvp.contract

import com.wmsj.baselibs.base.IBaseView
import com.wmsj.baselibs.base.IPresenter
import com.wmsj.baselibs.bean.BaseInfoBean
import com.wmsj.baselibs.bean.VersionBean

/**
 * Created by tian
on 2019/7/12.
 */
class MainContract {

    interface View : IBaseView {

        /**
         * 客户端应用权限
         */
        fun setClientsAuth(string: String)

        /**
         *  获取基本信息
         */
        fun getBaseInfoResult(data: List<BaseInfoBean>, type:String)

        /**
         *  获取版本信息
         */
        fun setVersionResult(data: VersionBean)

        /**
         * 失败
         */
        fun showError(errorMsg: String,errorCode:Int)
    }

    interface Presenter : IPresenter<View> {

        /**
         * 客户端应用权限
         */
        fun getClientsAuth()

        /**
         * 获取基本信息
         */
        fun getBaseInfo(type: String)

        /**
         * 获取版本信息
         */
        fun getVersion()
    }
}