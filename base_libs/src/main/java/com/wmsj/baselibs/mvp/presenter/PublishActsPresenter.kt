package com.wmsj.baselibs.mvp.presenter

import android.content.Context
import com.wmsj.baselibs.mvp.contract.PublishContract
import com.wmsj.baselibs.mvp.model.PublishActsModel
import com.wmsj.baselibs.base.BasePresenter
import com.wmsj.baselibs.exception.ExceptionHandle
import com.wmsj.baselibs.scheduler.SchedulerUtils
import io.reactivex.Observable
import top.zibin.luban.Luban
import java.util.*

/**
 * Created by tian
on 2019/7/12.
 */
class PublishActsPresenter : BasePresenter<PublishContract.View>(), PublishContract.Presenter {

    private var imageLists = ArrayList<String>()

    private val model by lazy { PublishActsModel() }


    override fun publishActs(map: HashMap<String, String>) {
        checkViewAttached()
        mRootView?.showLoading()
        addSubscription(disposable = model.publishActs(map)
                .subscribe({ response ->
                    mRootView?.apply {
                        if (response.state == "1") {
                            publishResult(response.message)
                        } else {
                            //处理异常
                            showError(response.message!!, 0)
                        }
                        dismissLoading()
                    }
                }, { throwable ->
                    mRootView?.apply {
                        //处理异常
                        dismissLoading()
                        showError(ExceptionHandle.handleException(throwable), ExceptionHandle.errorCode)
                    }
                }))
    }


    override fun imageUpload(mContext: Context, files: ArrayList<String>) {
        checkViewAttached()
        mRootView?.showLoading()
        imageLists.clear()
        var disposable = Observable.fromIterable(files)
                .compose(SchedulerUtils.ioToMain())
                .flatMap { t -> model.imageUpload(Luban.with(mContext).load(t).ignoreBy(100).get()[0].absolutePath) }
                .subscribe({ disposable ->
                    mRootView?.apply {
                        if (disposable.state == "1") {
                            imageLists.add(disposable.data.file)
                        } else {
                            showError(disposable.message, 0)
                        }
                        dismissLoading()
                    }
                }, { t ->
                    mRootView?.apply {
                        showError(ExceptionHandle.handleException(t)!!, ExceptionHandle.errorCode)
                        dismissLoading()
                    }
                }, {
                    mRootView?.apply {
                        setImageUploadResult(imageLists)
                        dismissLoading()
                    }
                })
        addSubscription(disposable)
    }

    override fun getMyOrgDetails(id: String) {
        checkViewAttached()
        mRootView?.apply { showLoading() }
        addSubscription(disposable = model.getMyOrgtDetails(id)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        setOrgInfoDetails(response.data)
                    }
                }, { throwable ->
                    mRootView?.apply {
                        //处理异常
                        dismissLoading()
                        showError(ExceptionHandle.handleException(throwable), ExceptionHandle.errorCode)
                    }
                }))
    }
}