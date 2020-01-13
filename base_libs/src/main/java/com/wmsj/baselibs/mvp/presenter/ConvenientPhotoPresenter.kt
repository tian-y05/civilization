package com.wmsj.baselibs.mvp.presenter

import com.wmsj.baselibs.mvp.contract.ConvenientPhotoContract
import com.wmsj.baselibs.mvp.model.ConvenientPhotoModel
import com.wmsj.baselibs.base.BasePresenter
import com.wmsj.baselibs.exception.ExceptionHandle

/**
 * Created by tian
on 2019/8/5.
 */
class ConvenientPhotoPresenter : BasePresenter<ConvenientPhotoContract.View>(), ConvenientPhotoContract.Presenter {


    private val photoModel: ConvenientPhotoModel by lazy {
        ConvenientPhotoModel()
    }

    override fun requestBannerData() {
        checkViewAttached()
        mRootView?.showLoading()
        var disposable = photoModel.getHomeBanner()
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        setBannerData(response.data)
                    }

                }, { t ->
                    mRootView?.apply {
                        dismissLoading()
                        showError(ExceptionHandle.handleException(t)!!, ExceptionHandle.errorCode)
                    }
                })

        addSubscription(disposable)
    }

    override fun requestModelsData(map: Map<String, String>) {
        checkViewAttached()
        var disposable = photoModel.requestHomeData(map)
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

    override fun requestMyCulture(map: Map<String, String>) {
        checkViewAttached()
        var disposable = photoModel.requestMyCulture(map)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        setMyCulture(response.data)
                    }

                }, { t ->
                    mRootView?.apply {
                        dismissLoading()
                        showError(ExceptionHandle.handleException(t)!!, ExceptionHandle.errorCode)
                    }
                })

        addSubscription(disposable)
    }

    override fun deleteMyCulture(map: Map<String, String>) {
        checkViewAttached()
        mRootView?.showLoading()
        var disposable = photoModel.deleteMyCulture(map)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        if (response.state == "1") {
                            deleteCultureResult(response.message)
                        } else {
                            //处理异常
                            showError(response.message!!, 0)
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