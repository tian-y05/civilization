package com.wmsj.baselibs.mvp.presenter

import com.wmsj.baselibs.mvp.contract.PerfectInfomationContract
import com.wmsj.baselibs.mvp.model.PerfectInfomationModel
import com.wmsj.baselibs.base.BasePresenter
import com.wmsj.baselibs.exception.ExceptionHandle

/**
 * Created by tian
on 2019/7/12.
 */
class PerfectInfomationPresenter : BasePresenter<PerfectInfomationContract.View>(), PerfectInfomationContract.Presenter {


    private val model by lazy { PerfectInfomationModel() }


    override fun getArea(id: String, type: String) {
        checkViewAttached()
        addSubscription(disposable = model.getArea(id)
                .subscribe({ response ->
                    mRootView?.apply {
                        if (response.state == "1") {
                            areaResult(response.data, type)
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

    override fun doPerfectInfo(map: HashMap<String, String>) {
        checkViewAttached()
        mRootView?.apply { showLoading() }
        addSubscription(disposable = model.doPerfectInfo(map)
                .subscribe({ response ->
                    mRootView?.apply {
                        if (response.state == "1") {
                            perfectInfoResult(response.message)
                        } else {
                            //处理异常
                            showError(response.message!!, 0)
                        }
                        dismissLoading()
                    }
                }, { throwable ->
                    mRootView?.apply {
                        //处理异常
                        dismissLoading()
                        showError(ExceptionHandle.handleException(throwable), ExceptionHandle.errorCode)
                    }
                }))
    }

    override fun getUserInfo(uid: String) {
        checkViewAttached()
        mRootView?.apply { showLoading() }
        addSubscription(disposable = model.getUserInfo(uid)
                .subscribe({ response ->
                    mRootView?.apply {
                        if (response.state == "1") {
                            userInfoResult(response.data)
                        } else {
                            //处理异常
                            showError(response.message!!, 0)
                        }
                        dismissLoading()
                    }
                }, { throwable ->
                    mRootView?.apply {
                        //处理异常
                        dismissLoading()
                        showError(ExceptionHandle.handleException(throwable), ExceptionHandle.errorCode)
                    }
                }))
    }

}