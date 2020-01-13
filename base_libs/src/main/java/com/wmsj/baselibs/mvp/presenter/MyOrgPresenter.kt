package com.wmsj.baselibs.mvp.presenter

import com.wmsj.baselibs.mvp.contract.MyOrgContract
import com.wmsj.baselibs.mvp.model.MyOrgModel
import com.wmsj.baselibs.base.BasePresenter
import com.wmsj.baselibs.exception.ExceptionHandle

/**
 * Created by tian
on 2019/8/5.
 */
class MyOrgPresenter : BasePresenter<MyOrgContract.View>(), MyOrgContract.Presenter {


    private val model: MyOrgModel by lazy {
        MyOrgModel()
    }

    override fun requestModelsData(type: String, num: String) {
        checkViewAttached()
        var disposable = model.requestHomeData(type, num)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        setModelsData(response.data)

                    }

                }, { t ->
                    mRootView?.apply {
                        dismissLoading()
                        showError(ExceptionHandle.handleException(t)!!, ExceptionHandle.errorCode)
                    }
                })

        addSubscription(disposable)

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


}