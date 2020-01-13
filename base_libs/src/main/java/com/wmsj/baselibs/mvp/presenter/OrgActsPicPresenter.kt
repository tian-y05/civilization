package com.wmsj.baselibs.mvp.presenter

import android.content.Context
import com.wmsj.baselibs.mvp.contract.OrgActsPicContract
import com.wmsj.baselibs.mvp.model.OrgActsPicModel
import com.wmsj.baselibs.base.BasePresenter
import com.wmsj.baselibs.exception.ExceptionHandle
import com.wmsj.baselibs.scheduler.SchedulerUtils
import io.reactivex.Observable
import top.zibin.luban.Luban
import java.util.*

/**
 * Created by tian
on 2019/8/5.
 */
class OrgActsPicPresenter : BasePresenter<OrgActsPicContract.View>(), OrgActsPicContract.Presenter {

    private var imageLists = ArrayList<String>()

    private val model: OrgActsPicModel by lazy {
        OrgActsPicModel()
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
                    }
                }, { t ->
                    mRootView?.apply {
                        showError(ExceptionHandle.handleException(t)!!, ExceptionHandle.errorCode)
                    }
                }, {
                    mRootView?.apply {
                        setImageUploadResult(imageLists)
                    }
                })
        addSubscription(disposable)
    }

    override fun pulishData(map: Map<String, String>) {
        checkViewAttached()
        mRootView?.showLoading()
        var disposable = model.pulishData(map)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        if (response.state == "1") {
                            pulishDataResult(response.message)
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