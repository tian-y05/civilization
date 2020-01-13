package com.wmsj.baselibs.mvp.presenter

import com.wmsj.baselibs.mvp.contract.StationActivityContract
import com.wmsj.baselibs.mvp.model.StationActivityModel
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.base.BasePresenter
import com.wmsj.baselibs.exception.ExceptionHandle

/**
 * Created by tian
on 2019/8/5.
 */
class StationActivityPresenter : BasePresenter<StationActivityContract.View>(), StationActivityContract.Presenter {


    private val stationModel: StationActivityModel by lazy {
        StationActivityModel()
    }


    override fun requestModelsData(station_id: String, state: String, num: String, type: String) {
        // 检测是否绑定 View
        checkViewAttached()
        mRootView?.showLoading()
        if (type == Const.POLYMERIZE) {
            var disposable = stationModel.requestOrgActivityList(station_id, state, num)
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
        } else {
            var disposable = stationModel.requestHomeData(station_id, state, num)
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

}