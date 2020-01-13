package com.wmsj.baselibs.mvp.presenter

import com.wmsj.baselibs.base.BasePresenter
import com.wmsj.baselibs.exception.ExceptionHandle
import com.wmsj.baselibs.mvp.contract.PracticeListContract
import com.wmsj.baselibs.mvp.model.PracticeListModel

/**
 * Created by tian
on 2019/8/5.
 */
class PracticeListPresenter : BasePresenter<PracticeListContract.View>(), PracticeListContract.Presenter {


    private val practiceModel: PracticeListModel by lazy {
        PracticeListModel()
    }


    override fun requestModelsData(num: String) {
        // 检测是否绑定 View
        checkViewAttached()
        mRootView?.showLoading()
        var disposable = practiceModel.requestHomeData(num)
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