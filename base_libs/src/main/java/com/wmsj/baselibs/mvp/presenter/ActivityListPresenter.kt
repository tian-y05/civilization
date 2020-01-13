package com.wmsj.baselibs.mvp.presenter

import com.wmsj.baselibs.mvp.contract.ActivityListContract
import com.wmsj.baselibs.mvp.model.ActivityListModel
import com.wmsj.baselibs.base.BasePresenter
import com.wmsj.baselibs.exception.ExceptionHandle

/**
 * Created by tian
on 2019/8/5.
 */
class ActivityListPresenter : BasePresenter<ActivityListContract.View>(), ActivityListContract.Presenter {


    private val activityModel: ActivityListModel by lazy {
        ActivityListModel()
    }

    override fun requestActivityCate() {
        checkViewAttached()
        mRootView?.showLoading()
        var disposable = activityModel.requestActivityCate()
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        setActivityCate(response.data)

                    }

                }, { t ->
                    mRootView?.apply {
                        dismissLoading()
                        showError(ExceptionHandle.handleException(t)!!, ExceptionHandle.errorCode)
                    }
                })

        addSubscription(disposable)
    }

    override fun requestActivitySonCate(key: String) {
        checkViewAttached()
        mRootView?.showLoading()
        var disposable = activityModel.requestActivitySonCate(key)
                .subscribe({ response ->
                    mRootView?.apply {
                        setActivitySonCate(response.data)

                    }

                }, { t ->
                    mRootView?.apply {
                        showError(ExceptionHandle.handleException(t)!!, ExceptionHandle.errorCode)
                    }
                })

        addSubscription(disposable)
    }

    override fun requestModelsData(type: String, num: String) {
        checkViewAttached()
        mRootView?.showLoading()
        var disposable = activityModel.requestHomeData(type, num)
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