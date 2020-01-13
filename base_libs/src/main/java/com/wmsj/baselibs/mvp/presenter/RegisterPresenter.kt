package com.wmsj.baselibs.mvp.presenter

import com.wmsj.baselibs.mvp.contract.RegisterContract
import com.wmsj.baselibs.mvp.model.RegisterModel
import com.wmsj.baselibs.base.BasePresenter
import com.wmsj.baselibs.exception.ExceptionHandle
import org.json.JSONObject

/**
 * Created by tian
on 2019/7/12.
 */
class RegisterPresenter : BasePresenter<RegisterContract.View>(), RegisterContract.Presenter {

    private val model by lazy { RegisterModel() }


    override fun getObtainCode(phone: String, type: String) {
        checkViewAttached()
        addSubscription(disposable = model.getObtainCode(phone, type)
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

    override fun doRegist(map: HashMap<String, String>) {
        checkViewAttached()
        mRootView?.apply {
            showLoading()
        }
        addSubscription(disposable = model.doRegister(map)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        if (response.state == "1") {
                            doRegistResult(response.data)
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

    override fun doForgetpwd(map: HashMap<String, String>, type: String) {
        checkViewAttached()
        mRootView?.apply {
            showLoading()
        }
        addSubscription(disposable = model.doForgetpwd(map, type)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        if (response.state == "1") {
                            doForgetpwd(response.message)
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


    override fun doEditTel(map: HashMap<String, String>, role: String) {
        checkViewAttached()
        mRootView?.apply {
            showLoading()
        }
        addSubscription(disposable = model.doEditTel(map,role)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        if (response.state == "1") {
                            doEditResult(response.message)
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

    override fun doOrgRegist(map: HashMap<String, String>) {
        checkViewAttached()
        mRootView?.apply {
            showLoading()
        }
        addSubscription(disposable = model.doOrgRegister(map)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        if (response.state == "1") {
                            val jsonObject = JSONObject(response.data.toString())
                            doOrgRegistResult(jsonObject.get("id").toString())
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


}