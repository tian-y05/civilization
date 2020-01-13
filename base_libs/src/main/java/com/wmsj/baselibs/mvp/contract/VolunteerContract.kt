package com.wmsj.baselibs.mvp.contract

import com.wmsj.baselibs.base.IBaseView
import com.wmsj.baselibs.base.IPresenter
import com.wmsj.baselibs.bean.OrgBaseInfoBean

import com.wmsj.baselibs.bean.UserCentreBean

/**
 * Created by tian
on 2019/7/12.
 */
class VolunteerContract {

    interface View : IBaseView {

        /**
         * 志愿者信息
         */
        fun userCentreResult(data: UserCentreBean)

        /**
         * 组织信息
         */
        fun orgInfoResult(data: OrgBaseInfoBean)

        /**
         * 失败
         */
        fun showError(errorMsg: String, errorCode: Int)
    }

    interface Presenter : IPresenter<View> {

        /**
         * 志愿者中心
         */
        fun getUserCentre(uid: String)

        /**
         * 获取当前组织信息
         */
        fun getOrgInfo(uid: String)
    }
}