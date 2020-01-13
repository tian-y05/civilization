package com.wmsj.baselibs.mvp.presenter

import com.wmsj.baselibs.mvp.contract.OrgSearchContract
import com.wmsj.baselibs.mvp.model.OrgSearchModel
import com.wmsj.baselibs.base.BasePresenter
import com.wmsj.baselibs.exception.ExceptionHandle

/**
 * Created by tian
on 2019/8/5.
 */
class OrgSearchPresenter : BasePresenter<OrgSearchContract.View>(), OrgSearchContract.Presenter {

    private val model: OrgSearchModel by lazy {
        OrgSearchModel()
    }

    override fun requestModelsData(type: String, num: String) {
        // 检测是否绑定 View
        checkViewAttached()
        mRootView?.showLoading()
        var disposable = model.requestHomeData(type, num)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        if(response.data != null){
                            setModelsData(response.data)
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