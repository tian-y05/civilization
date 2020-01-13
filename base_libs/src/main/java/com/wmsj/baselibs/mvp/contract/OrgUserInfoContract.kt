package com.wmsj.baselibs.mvp.contract

import com.wmsj.baselibs.base.IBaseView
import com.wmsj.baselibs.base.IPresenter
import com.wmsj.baselibs.bean.OrgUserInfo


/**
 * Created by tian
on 2019/7/12.
 */
class OrgUserInfoContract {

    interface View : IBaseView {

        /**
         * 志愿者信息
         */
        fun userInfoResult(data: OrgUserInfo)


        /**
         * 失败
         */
        fun showError(errorMsg: String, errorCode: Int)
    }

    interface Presenter : IPresenter<View> {

        /**
         * 志愿者信息
         */
        fun orgUserInfo(map: HashMap<String, String>)


    }
}