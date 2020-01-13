package com.wmsj.baselibs.mvp.presenter

import com.wmsj.baselibs.mvp.contract.RecuritMemberContract
import com.wmsj.baselibs.mvp.model.RecruitMemberModel
import com.wmsj.baselibs.base.BasePresenter
import com.wmsj.baselibs.exception.ExceptionHandle

/**
 * Created by tian
on 2019/8/5.
 */
class RecruitMemberPresenter : BasePresenter<RecuritMemberContract.View>(), RecuritMemberContract.Presenter {


    private val eventModel: RecruitMemberModel by lazy {
        RecruitMemberModel()
    }


    override fun requestModelsData(type: String, id: String, keyword: String, page: String) {
        // 检测是否绑定 View
        checkViewAttached()
        var disposable = eventModel.requestHomeData(type, id, keyword, page)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        setModelsData(response.data!!)
                    }

                }, { t ->
                    mRootView?.apply {
                        dismissLoading()
                        showError(ExceptionHandle.handleException(t)!!, ExceptionHandle.errorCode)
                    }
                })

        addSubscription(disposable)

    }

    override fun cancelSign(id: String, state: String) {
        // 检测是否绑定 View
        checkViewAttached()
        mRootView?.apply { showLoading() }
        var disposable = eventModel.orgActsState(id, state)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        if (response.state == "1") {
                            cancelResult(response.message)
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

    override fun timeEnter(map: HashMap<String, String>) {
        // 检测是否绑定 View
        checkViewAttached()
        mRootView?.apply { showLoading() }
        var disposable = eventModel.timeEnter(map)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        if (response.state == "1") {
                            cancelResult(response.message)
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