package com.wmsj.baselibs.mvp.contract

import com.wmsj.baselibs.base.IBaseView
import com.wmsj.baselibs.base.IPresenter

import com.wmsj.baselibs.bean.StationListBean

/**
 * 所站风貌相关活动
on 2019/8/5.
 */
class StationSpotContract {

    interface View : IBaseView {

        /**
         * 模块数据
         */
        fun setModelsData(data: List<StationListBean>)


        /**
         * 显示错误信息
         */
        fun showError(msg: String, errorCode: Int)

    }


    interface Presenter : IPresenter<View> {

        /**
         * 获取模块数据
         */
        fun requestModelsData(station_id: String,num: String)


    }
}