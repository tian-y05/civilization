package com.wmsj.baselibs.mvp.contract

import com.wmsj.baselibs.base.IBaseView
import com.wmsj.baselibs.base.IPresenter
import com.wmsj.baselibs.bean.BaseInfoBean

import com.wmsj.baselibs.bean.HomeActivityListBean
import com.wmsj.baselibs.bean.HomeBanner
import com.wmsj.baselibs.bean.HomeColumnsBean

/**
 * Created by tian
on 2019/8/5.
 */
class HomeContract {
    interface View : IBaseView {
        /**
         * 轮播图数据
         */
        fun setBannerData(data: List<HomeBanner>)

        /**
         * 首页栏目列表
         */
        fun setColumnsData(data: List<HomeColumnsBean>)

        /**
         * 首页应用列表
         */
        fun setAppsData(data: List<HomeColumnsBean>)

        /**
         * 首页活动数据
         */
        fun setActivityData(data: List<HomeActivityListBean>)

        /**
         *  获取基本信息
         */
        fun getBaseInfoResult(data: List<BaseInfoBean>, type: String)

        /**
         * 显示错误信息
         */
        fun showError(msg: String, errorCode: Int)

    }


    interface Presenter : IPresenter<View> {

        /**
         * 获取轮播图数据
         */
        fun requestBannerData()

        /**
         * 获取首页栏目列表
         */
        fun requestColumnsData(type: Int)

        /**
         * 获取首页应用列表
         */
        fun requestAppsData()

        /**
         * 获取首页活动数据
         */
        fun requestActivityData(num: Int)


        /**
         * 获取基本信息
         */
        fun getBaseInfo(type: String)
    }
}