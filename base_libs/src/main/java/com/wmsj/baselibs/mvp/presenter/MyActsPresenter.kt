package com.wmsj.baselibs.mvp.presenter

import com.wmsj.baselibs.mvp.contract.MyActsContract
import com.wmsj.baselibs.mvp.model.MyActsModel
import com.wmsj.baselibs.base.BasePresenter
import com.wmsj.baselibs.exception.ExceptionHandle

/**
 * Created by tian
on 2019/8/5.
 */
class MyActsPresenter : BasePresenter<MyActsContract.View>(), MyActsContract.Presenter {

    private val eventModel: MyActsModel by lazy {
        MyActsModel()
    }


    override fun requestModelsData(type: String, num: String) {
        // 检测是否绑定 View
        checkViewAttached()
        var disposable = eventModel.requestHomeData(type, num)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        if (response.data != null) {
                            setModelsData(response.data)
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

    override fun getActivityService(map: HashMap<String, String>) {
        // 检测是否绑定 View
        checkViewAttached()
        mRootView?.showLoading()
        var disposable = eventModel.getActivityService(map)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        activityServiceDetail(response.data)
                    }

                }, { t ->
                    mRootView?.apply {
                        dismissLoading()
                        showError(ExceptionHandle.handleException(t)!!, ExceptionHandle.errorCode)
                    }
                })

        addSubscription(disposable)
    }

    override fun checkCard(map: HashMap<String, String>) {
        // 检测是否绑定 View
        checkViewAttached()
        mRootView?.showLoading()
        var disposable = eventModel.checkCard(map)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        if(response.state == "1"){
                            checkCardResult(response.data)
                        }else{
                            showError(response.message!!,100)
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

    override fun removeSign(map: HashMap<String, String>) {
        // 检测是否绑定 View
        checkViewAttached()
        mRootView?.showLoading()
        var disposable = eventModel.removeSign(map)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        removeSignResult(response.message)

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