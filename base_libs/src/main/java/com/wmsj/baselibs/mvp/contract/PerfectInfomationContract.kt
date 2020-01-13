package com.wmsj.baselibs.mvp.contract

import com.wmsj.baselibs.bean.AreaBean
import com.wmsj.baselibs.base.IBaseView
import com.wmsj.baselibs.base.IPresenter

import com.wmsj.baselibs.bean.UserInfo

/**
 * Created by tian
on 2019/7/12.
 */
class PerfectInfomationContract {

    interface View : IBaseView {

        /**
         * 完善信息
         */
        fun perfectInfoResult(data: String)

        /**
         * 志愿者信息
         */
        fun userInfoResult(data: UserInfo)

        /**
         * 获取地区
         */
        fun areaResult(data: List<AreaBean>, type: String)


        /**
         * 失败
         */
        fun showError(errorMsg: String, errorCode: Int)
    }

    interface Presenter : IPresenter<View> {


        /**
         * 获取当前用户信息
         */
        fun getUserInfo(uid: String)

        /**
         * 获取地区
         */
        fun getArea(id: String, type: String)

        /**
         * 完善信息
         */
        fun doPerfectInfo(map: HashMap<String, String>)

    }
}