package com.wmsj.baselibs.mvp.presenter

import com.wmsj.baselibs.mvp.contract.HomeContract
import com.wmsj.baselibs.mvp.model.HomeModel
import com.wmsj.baselibs.base.BasePresenter
import com.wmsj.baselibs.exception.ExceptionHandle

/**
 * Created by tian
on 2019/8/5.
 */
class HomePresenter : BasePresenter<HomeContract.View>(), HomeContract.Presenter {


    private val homeModel: HomeModel by lazy {
        HomeModel()
    }

    override fun requestBannerData() {
        // 检测是否绑定 View
        checkViewAttached()
        mRootView?.showLoading()
        var disposable = homeModel.getHomeBanner()
                .subscribe({ response ->
                    mRootView?.apply {
                        setBannerData(response.data!!)
                    }

                }, { t ->
                    mRootView?.apply {
                        dismissLoading()
                        showError(ExceptionHandle.handleException(t)!!, ExceptionHandle.errorCode)
                    }
                })

        addSubscription(disposable)
    }


    override fun requestColumnsData(type: Int) {
        checkViewAttached()
        mRootView?.showLoading()
        var disposable = homeModel.getHomeColumns(type)
                .subscribe({ response ->
                    mRootView?.apply {
                        setColumnsData(response.data!!)
                    }

                }, { t ->
                    mRootView?.apply {
                        dismissLoading()
                        showError(ExceptionHandle.handleException(t)!!, ExceptionHandle.errorCode)
                    }
                })
        addSubscription(disposable)
    }

    override fun requestAppsData() {
        checkViewAttached()
        mRootView?.showLoading()
        var disposable = homeModel.getHomeApps()
                .subscribe({ response ->
                    mRootView?.apply {
                        setAppsData(response.data!!)
                    }

                }, { t ->
                    mRootView?.apply {
                        dismissLoading()
                        showError(ExceptionHandle.handleException(t)!!, ExceptionHandle.errorCode)
                    }
                })
        addSubscription(disposable)
    }

    override fun requestActivityData(num: Int) {
        checkViewAttached()
        mRootView?.showLoading()
        var disposable = homeModel.getHomeNewsActivity(num)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        setActivityData(response.data!!)
                    }

                }, { t ->
                    mRootView?.apply {
                        dismissLoading()
                        showError(ExceptionHandle.handleException(t)!!, ExceptionHandle.errorCode)
                    }
                })
        addSubscription(disposable)
    }

    override fun getBaseInfo(type: String) {
        checkViewAttached()
        addSubscription(disposable = homeModel.getBaseInfo(type)
                .subscribe({ response ->
                    mRootView?.apply {
                        if (response.state == "1") {
                            getBaseInfoResult(response.data, type)
                        } else {
                            //处理异常
                            showError(response.message!!, 0)
                        }
                    }
                }, { throwable ->
                    mRootView?.apply {
                        //处理异常
                        showError(ExceptionHandle.handleException(throwable), ExceptionHandle.errorCode)
                    }
                }))
    }

}