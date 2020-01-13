package com.wmsj.baselibs.mvp.contract


import com.wmsj.baselibs.base.IBaseView
import com.wmsj.baselibs.base.IPresenter
import com.wmsj.baselibs.bean.HangupDetailBean
import com.wmsj.baselibs.bean.OrgBaseInfoBean
import com.wmsj.baselibs.bean.OrgDetailBean

/**
 * Created by tian
on 2019/7/12.
 */
class OrgDetailsContract {

    interface View : IBaseView {

        /**
         * 组织详情
         */
        fun setOrgDetails(data: OrgDetailBean)

        /**
         * 组织详情
         */
        fun setOrgInfoDetails(data: OrgBaseInfoBean)

        /**
         * 加入组织
         */
        fun joinOrgResult(data: String)

        /**
         * 退出组织
         */
        fun exitOrgResult(data: String)

        /**
         * 挂靠组织详情
         */
        fun orgManageDetail(data: HangupDetailBean)

        /**
         * 操作结果
         */
        fun operationResult(data: String)

        /**
         * 失败
         */
        fun showError(errorMsg: String, errorCode: Int)
    }

    interface Presenter : IPresenter<View> {

        /**
         * 首页的组织详情
         */
        fun getOrgDetails(id: String)

        /**
         * 我的组织详情
         */
        fun getMyOrgDetails(id: String)

        /**
         * 加入组织
         */
        fun joinOrg(id: String)

        /**
         * 挂靠组织
         */
        fun applyOrg(state: String, id: String)

        /**
         * 退出组织
         */
        fun exitOrg(id: String)

        /**
         * 挂靠组织详情
         */
        fun orgManageDetail(id: String)

        /**
         * 挂靠组织操作
         */
        fun hangupOrgOperation(state: String, id: String)

    }
}