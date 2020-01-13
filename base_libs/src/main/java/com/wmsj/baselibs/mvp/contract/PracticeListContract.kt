package com.wmsj.baselibs.mvp.contract

import com.wmsj.baselibs.base.IBaseView
import com.wmsj.baselibs.base.IPresenter

import com.wmsj.baselibs.bean.PracticeBaseBean

/**
 * Created by tian
on 2019/8/5.
 */
class PracticeListContract {

    interface View : IBaseView {

        /**
         * 模块数据
         */
        fun setModelsData(data: List<PracticeBaseBean>)


        /**
         * 显示错误信息
         */
        fun showError(msg: String, errorCode: Int)

    }


    interface Presenter : IPresenter<View> {

        /**
         * 获取模块数据
         */
        fun requestModelsData( num: String)


    }
}