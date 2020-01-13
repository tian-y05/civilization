package com.wmsj.baselibs.mvp.presenter

import com.wmsj.baselibs.base.BasePresenter
import com.wmsj.baselibs.exception.ExceptionHandle
import com.wmsj.baselibs.mvp.contract.StationContentContract
import com.wmsj.baselibs.mvp.model.StationContentModel

/**
 * Created by tian
on 2019/8/5.
 */
class StationContentPresenter : BasePresenter<StationContentContract.View>(), StationContentContract.Presenter {


    private val stationModel: StationContentModel by lazy {
        StationContentModel()
    }


    override fun requestModelsData(num: String) {
        // 检测是否绑定 View
        checkViewAttached()
        mRootView?.showLoading()
        var disposable = stationModel.requestHomeData(num)
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