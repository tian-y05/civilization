package com.wmsj.baselibs.mvp.presenter

import com.wmsj.baselibs.mvp.contract.OrgDetailsContract
import com.wmsj.baselibs.mvp.model.OrgDetailsModel
import com.wmsj.baselibs.base.BasePresenter
import com.wmsj.baselibs.exception.ExceptionHandle

/**
 * Created by tian
on 2019/7/12.
 */
class OrgDeatilsPresenter : BasePresenter<OrgDetailsContract.View>(), OrgDetailsContract.Presenter {

    private val model by lazy { OrgDetailsModel() }

    override fun getOrgDetails(id: String) {
        checkViewAttached()
        mRootView?.apply { showLoading() }
        addSubscription(disposable = model.getEventDetails(id)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        setOrgInfoDetails(response.data)
                    }
                }, { throwable ->
                    mRootView?.apply {
                        //处理异常
                        dismissLoading()
                        showError(ExceptionHandle.handleException(throwable), ExceptionHandle.errorCode)
                    }
                }))
    }

    override fun getMyOrgDetails(id: String) {
        checkViewAttached()
        mRootView?.apply { showLoading() }
        addSubscription(disposable = model.getMyOrgtDetails(id)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        setOrgInfoDetails(response.data)
                    }
                }, { throwable ->
                    mRootView?.apply {
                        //处理异常
                        dismissLoading()
                        showError(ExceptionHandle.handleException(throwable), ExceptionHandle.errorCode)
                    }
                }))
    }

    override fun joinOrg(id: String) {
        checkViewAttached()
        mRootView?.apply { showLoading() }
        addSubscription(disposable = model.joinOrg(id)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        if (response.state == "1") {
                            joinOrgResult(response.message)
                        } else {
                            showError(response.message!!, response.httpcode.toInt())
                        }
                    }
                }, { throwable ->
                    mRootView?.apply {
                        //处理异常
                        dismissLoading()
                        showError(ExceptionHandle.handleException(throwable), ExceptionHandle.errorCode)
                    }
                }))
    }


    override fun applyOrg(state: String, id: String) {
        checkViewAttached()
        mRootView?.apply { showLoading() }
        addSubscription(disposable = model.applyOrg(state, id)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        if (response.state == "1") {
                            joinOrgResult(response.message)
                        } else {
                            showError(response.message!!, response.httpcode.toInt())
                        }
                    }
                }, { throwable ->
                    mRootView?.apply {
                        //处理异常
                        dismissLoading()
                        showError(ExceptionHandle.handleException(throwable), ExceptionHandle.errorCode)
                    }
                }))
    }

    override fun exitOrg(id: String) {
        checkViewAttached()
        mRootView?.apply { showLoading() }
        addSubscription(disposable = model.exitOrg(id)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        if (response.state == "1") {
                            exitOrgResult(response.message)
                        } else {
                            showError(response.message!!, response.httpcode.toInt())
                        }
                    }
                }, { throwable ->
                    mRootView?.apply {
                        //处理异常
                        dismissLoading()
                        showError(ExceptionHandle.handleException(throwable), ExceptionHandle.errorCode)
                    }
                }))
    }

    override fun orgManageDetail(id: String) {
        checkViewAttached()
        mRootView?.apply { showLoading() }
        addSubscription(disposable = model.orgManageDetail(id)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        orgManageDetail(response.data)
                    }
                }, { throwable ->
                    mRootView?.apply {
                        //处理异常
                        dismissLoading()
                        showError(ExceptionHandle.handleException(throwable), ExceptionHandle.errorCode)
                    }
                }))
    }

    override fun hangupOrgOperation(state: String, id: String) {
        // 检测是否绑定 View
        checkViewAttached()
        mRootView?.showLoading()
        var disposable = model.hangupOrgOperation(state, id)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        if (response.state == "1") {
                            operationResult(response.message)
                        } else {
                            showError(response.message!!, response.httpcode.toInt())
                        }
                    }
                }, { t ->
                    mRootView?.apply {
                        dismissLoading()
                        showError(ExceptionHandle.handleException(t)!!, ExceptionHandle.errorCode)
                    }
                })

        addSubscription(disposable)
    }

}