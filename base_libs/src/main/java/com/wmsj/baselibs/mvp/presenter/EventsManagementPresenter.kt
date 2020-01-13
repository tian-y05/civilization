package com.wmsj.baselibs.mvp.presenter

import com.wmsj.baselibs.mvp.contract.EventsManagementContract
import com.wmsj.baselibs.mvp.model.EventsManagementModel
import com.wmsj.baselibs.base.BasePresenter
import com.wmsj.baselibs.exception.ExceptionHandle

/**
 * Created by tian
on 2019/8/5.
 */
class EventsManagementPresenter : BasePresenter<EventsManagementContract.View>(), EventsManagementContract.Presenter {


    private val eventModel: EventsManagementModel by lazy {
        EventsManagementModel()
    }


    override fun requestModelsData(type: String, num: String, name: String) {
        // 检测是否绑定 View
        checkViewAttached()
        var disposable = eventModel.requestHomeData(type, num,name)
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

    override fun requestOrgActs(type: String, num: String) {
        // 检测是否绑定 View
        checkViewAttached()
        var disposable = eventModel.requestOrgActs(type, num)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        setOrgActs(response.data!!)
                    }

                }, { t ->
                    mRootView?.apply {
                        dismissLoading()
                        showError(ExceptionHandle.handleException(t)!!, ExceptionHandle.errorCode)
                    }
                })

        addSubscription(disposable)
    }


    override fun backDraft(map: HashMap<String, String>) {
        // 检测是否绑定 View
        checkViewAttached()
        mRootView?.showLoading()
        var disposable = eventModel.backDraft(map)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        if (response.state == "1") {
                            backDraftResult(response.message)
                        } else {
                            showError(response.message!!, response.state.toInt())
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

    override fun actsManage(map: HashMap<String, String>) {
        // 检测是否绑定 View
        checkViewAttached()
        mRootView?.showLoading()
        var disposable = eventModel.actsManage(map)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        if (response.state == "1") {
                            backDraftResult(response.message)
                        } else {
                            showError(response.message!!, response.state.toInt())
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