package com.wmsj.baselibs.mvp.presenter

import com.wmsj.baselibs.mvp.contract.OrgMemberContract
import com.wmsj.baselibs.mvp.model.OrgMemberManageModel
import com.wmsj.baselibs.base.BasePresenter
import com.wmsj.baselibs.exception.ExceptionHandle

/**
 * Created by tian
on 2019/8/5.
 */
class OrgMemberManagePresenter : BasePresenter<OrgMemberContract.View>(), OrgMemberContract.Presenter {


    private val model: OrgMemberManageModel by lazy {
        OrgMemberManageModel()
    }

    override fun orgMemberManage(state: String, num: String) {
        checkViewAttached()
        var disposable = model.orgMemberManage(state, num)
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

    override fun orgMemberCheck(type: String, id: String) {
        checkViewAttached()
        mRootView?.showLoading()
        var disposable = model.orgMemberCheck(type, id)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        if (response.state == "1") {
                            orgCheckResult(response.message)
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