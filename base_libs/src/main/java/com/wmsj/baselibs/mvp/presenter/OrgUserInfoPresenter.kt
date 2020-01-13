package com.wmsj.baselibs.mvp.presenter

import com.wmsj.baselibs.mvp.contract.OrgUserInfoContract
import com.wmsj.baselibs.mvp.model.OrgUserInfoModel
import com.wmsj.baselibs.base.BasePresenter
import com.wmsj.baselibs.exception.ExceptionHandle

/**
 * Created by tian
on 2019/8/5.
 */
class OrgUserInfoPresenter : BasePresenter<OrgUserInfoContract.View>(), OrgUserInfoContract.Presenter {


    private val eventModel: OrgUserInfoModel by lazy {
        OrgUserInfoModel()
    }


    override fun orgUserInfo(map: HashMap<String, String>) {
        checkViewAttached()
        mRootView?.apply {
            showLoading()
        }
        var disposable = eventModel.orgUserInfo(map)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        userInfoResult(response.data!!)
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