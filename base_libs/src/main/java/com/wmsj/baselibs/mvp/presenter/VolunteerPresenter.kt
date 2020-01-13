package com.wmsj.baselibs.mvp.presenter

import com.wmsj.baselibs.mvp.contract.VolunteerContract
import com.wmsj.baselibs.mvp.model.VolunteerModel
import com.wmsj.baselibs.base.BasePresenter
import com.wmsj.baselibs.exception.ExceptionHandle

/**
 * Created by tian
on 2019/7/12.
 */
class VolunteerPresenter : BasePresenter<VolunteerContract.View>(), VolunteerContract.Presenter {

    private val searchModel by lazy { VolunteerModel() }


    override fun getUserCentre(uid: String) {
        checkViewAttached()
        mRootView?.apply {
            showLoading()
        }
        addSubscription(disposable = searchModel.getUserCentre(uid)
                .subscribe({ response ->
                    mRootView?.apply {
                        if (response.state == "1") {
                            userCentreResult(response.data)
                        } else {
                            //处理异常
                            showError(response.message!!, 0)
                        }
                        dismissLoading()
                    }
                }, { throwable ->
                    mRootView?.apply {
                        dismissLoading()
                        //处理异常
                        showError(ExceptionHandle.handleException(throwable), ExceptionHandle.errorCode)
                    }
                }))
    }

    override fun getOrgInfo(uid: String) {
        checkViewAttached()
        mRootView?.apply { showLoading() }
        addSubscription(disposable = searchModel.getUserInfo(uid)
                .subscribe({ response ->
                    mRootView?.apply {
                        if (response.state == "1") {
                            orgInfoResult(response.data)
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