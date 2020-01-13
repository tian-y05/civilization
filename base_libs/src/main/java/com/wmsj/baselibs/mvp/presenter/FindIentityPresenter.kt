package com.wmsj.baselibs.mvp.presenter

import com.wmsj.baselibs.mvp.contract.FindIdentityContract
import com.wmsj.baselibs.mvp.model.FindIdentityModel
import com.wmsj.baselibs.base.BasePresenter
import com.wmsj.baselibs.exception.ExceptionHandle
import org.json.JSONObject

/**
 * Created by tian
on 2019/7/12.
 */
class FindIentityPresenter : BasePresenter<FindIdentityContract.View>(), FindIdentityContract.Presenter {


    private val model by lazy { FindIdentityModel() }

    override fun verificationPhone(map: HashMap<String, String>) {
        checkViewAttached()
        mRootView?.apply {
            showLoading()
        }
        addSubscription(disposable = model.verificationPhone(map)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        if (response.state == "1") {
                            verificationResult(response.message)
                        } else {
                            //处理异常
                            showError(response.message!!, 0)
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

    override fun machIdentityInfo(map: HashMap<String, String>) {
        checkViewAttached()
        mRootView?.apply {
            showLoading()
        }
        addSubscription(disposable = model.machIdentityInfo(map)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        machInfoResult(response)
                    }
                }, { throwable ->
                    mRootView?.apply {
                        //处理异常
                        dismissLoading()
                        showError(ExceptionHandle.handleException(throwable), ExceptionHandle.errorCode)
                    }
                }))
    }

    override fun getObtainCode(phone: String) {
        checkViewAttached()
        addSubscription(disposable = model.getObtainCode(phone)
                .subscribe({ response ->
                    mRootView?.apply {
                        if (response.state == "1") {
                            obtainCodeResult(response.message)
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

    override fun deleteMachInfo(code: String) {
        checkViewAttached()
        mRootView?.showLoading()
        addSubscription(disposable = model.deleteMatchInfo(code)
                .subscribe({ response ->
                    mRootView?.apply {
                        if (response.state == "1") {
                            deleteInfoResult(response.message)
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

    override fun bindIdentity(map: HashMap<String, String>) {
        checkViewAttached()
        mRootView?.showLoading()
        addSubscription(disposable = model.bindingIdentity(map)
                .subscribe({ response ->
                    mRootView?.apply {
                        if (response.state == "1") {
                            val jsonObject = JSONObject(response.data.toString())
                            bindingInfoResult(jsonObject.get("uid").toString())
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


    override fun resetPwd(map: HashMap<String, String>) {
        checkViewAttached()
        mRootView?.showLoading()
        addSubscription(disposable = model.resetPwd(map)
                .subscribe({ response ->
                    mRootView?.apply {
                        if (response.state == "1") {
                            resetResult(response.message)
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

}