package com.wmsj.baselibs.mvp.presenter

import com.wmsj.baselibs.mvp.contract.EmployMemberContract
import com.wmsj.baselibs.mvp.model.EmployMemberModel
import com.wmsj.baselibs.base.BasePresenter
import com.wmsj.baselibs.exception.ExceptionHandle

/**
 * Created by tian
on 2019/8/5.
 */
class EmployMemberPresenter : BasePresenter<EmployMemberContract.View>(), EmployMemberContract.Presenter {


    private val eventModel: EmployMemberModel by lazy {
        EmployMemberModel()
    }


    override fun requestModelsData(map: HashMap<String, String>) {// 检测是否绑定 View
        checkViewAttached()
        var disposable = eventModel.requestHomeData(map)
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

    override fun orgEmployment(map: HashMap<String, String>) { // 检测是否绑定 View
        checkViewAttached()
        mRootView?.apply { showLoading() }
        var disposable = eventModel.orgEmployment(map)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        if (response.state == "1") {
                            employmentResult(response.message)
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