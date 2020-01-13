package com.wmsj.baselibs.mvp.presenter

import android.content.Context
import com.wmsj.baselibs.mvp.contract.TakePhotoOrVideoContract
import com.wmsj.baselibs.mvp.model.TakePhotoOrVideoModel
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
class TakePhotoOrVideoPresenter : BasePresenter<TakePhotoOrVideoContract.View>(), TakePhotoOrVideoContract.Presenter {


    private var imageLists = ArrayList<String>()

    private val model: TakePhotoOrVideoModel by lazy {
        TakePhotoOrVideoModel()
    }

    override fun imageUpload(mContext: Context, files: ArrayList<String>) {
        checkViewAttached()
        mRootView?.showLoading()
        files.remove("addImage")
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
                        dismissLoading()
                        setImageUploadResult(imageLists)
                    }
                })
        addSubscription(disposable)
    }

    override fun videoUpload(file: String) {
        checkViewAttached()
        mRootView?.showLoading()
        var disposable = model.videoUpload(file)
                .subscribe({ response ->
                    mRootView?.apply {
                        if (response.state == "1") {
                            setVideoloadResult(response.data.file)
                        } else {
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

    override fun requestPulishData(map: Map<String, String>, file: String) {
        checkViewAttached()
        mRootView?.showLoading()
        var disposable = model.requestPulishData(map, file)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        if (response.state == "1") {
                            setPulishData(response.message)
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

    override fun pushOrgReport(map: Map<String, String>) {
        checkViewAttached()
        mRootView?.showLoading()
        var disposable = model.pushOrgReport(map)
                .subscribe({ response ->
                    mRootView?.apply {
                        dismissLoading()
                        if (response.state == "1") {
                            setPulishData(response.message)
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

