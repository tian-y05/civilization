package com.wmsj.baselibs.mvp.presenter

import com.wmsj.baselibs.mvp.contract.SystemMessageContract
import com.wmsj.baselibs.mvp.model.SystemMessageModel
import com.wmsj.baselibs.base.BasePresenter
import com.wmsj.baselibs.exception.ExceptionHandle

/**
 * Created by tian
on 2019/8/5.
 */
class SystemMessagePresenter : BasePresenter<SystemMessageContract.View>(), SystemMessageContract.Presenter {


    private val model: SystemMessageModel by lazy {
        SystemMessageModel()
    }


    override fun requestModelsData( num: String) {
        // 检测是否绑定 View
        checkViewAttached()
        mRootView?.showLoading()
        var disposable = model.requestHomeData(num)
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

}