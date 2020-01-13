package com.wmsj.baselibs.mvp.presenter

import com.wmsj.baselibs.mvp.contract.ArticleListContract
import com.wmsj.baselibs.mvp.model.ArticleListModel
import com.wmsj.baselibs.base.BasePresenter
import com.wmsj.baselibs.exception.ExceptionHandle

/**
 * Created by tian
on 2019/8/5.
 */
class ArticleListPresenter : BasePresenter<ArticleListContract.View>(), ArticleListContract.Presenter {



    private val articleModel: ArticleListModel by lazy {
        ArticleListModel()
    }


    override fun requestModelsData(type: String, num: String) {
        // 检测是否绑定 View
        checkViewAttached()
        mRootView?.showLoading()
        var disposable = articleModel.requestHomeData(type, num)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        setModelsData(response.data.list!!)

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