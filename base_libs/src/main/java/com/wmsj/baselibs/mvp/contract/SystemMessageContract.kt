package com.wmsj.baselibs.mvp.contract

import com.wmsj.baselibs.base.IBaseView
import com.wmsj.baselibs.base.IPresenter
import com.wmsj.baselibs.bean.SystemNewsBean

/**
 * Created by tian
on 2019/8/5.
 */
class SystemMessageContract {

    interface View : IBaseView {

        /**
         * 系统通知
         */
        fun setModelsData(data: List<SystemNewsBean>?)


        /**
         * 显示错误信息
         */
        fun showError(msg: String, errorCode: Int)

    }


    interface Presenter : IPresenter<View> {

        /**
         * 系统通知
         */
        fun requestModelsData(num: String)


    }
}