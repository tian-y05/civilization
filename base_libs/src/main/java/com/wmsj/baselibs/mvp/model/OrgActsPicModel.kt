package com.wmsj.baselibs.mvp.model

import com.wmsj.baselibs.Const
import com.wmsj.baselibs.retrofit.BaseResponse
import com.wmsj.baselibs.retrofit.RetrofitManager
import com.wmsj.baselibs.retrofit.RetrofitResourceManager
import com.wmsj.baselibs.bean.ImageBean
import com.wmsj.baselibs.scheduler.SchedulerUtils
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * Created by tian
on 2019/8/5.
 */
class OrgActsPicModel {

    /**
     * 图片上传
     */
    fun imageUpload(file: String): Observable<BaseResponse<ImageBean>> {
        var requestFile = RequestBody.create(MediaType.parse("image/*"), File(file))
        var part = MultipartBody.Part.createFormData("file", file, requestFile)
        return RetrofitResourceManager(Const.UPLOADURL).service.postImage(part)
                .compose(SchedulerUtils.ioToMain())
    }


    /**
     * 活动图片上传
     */
    fun pulishData(map: Map<String, String>): Observable<BaseResponse<Any>> {
        return RetrofitManager.service.actsPicUpload(map)
                .compose(SchedulerUtils.ioToMain())
    }

}