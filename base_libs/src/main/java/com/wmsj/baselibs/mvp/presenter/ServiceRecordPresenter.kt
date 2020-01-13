package com.wmsj.baselibs.mvp.presenter

import com.wmsj.baselibs.mvp.contract.ServiceRecordContract
import com.wmsj.baselibs.mvp.model.ServiceRecordModel
import com.wmsj.baselibs.base.BasePresenter
import com.wmsj.baselibs.exception.ExceptionHandle

/**
 * Created by tian
on 2019/8/5.
 */
class ServiceRecordPresenter : BasePresenter<ServiceRecordContract.View>(), ServiceRecordContract.Presenter {


    private val model: ServiceRecordModel by lazy {
        ServiceRecordModel()
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