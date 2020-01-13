package com.wmsj.baselibs.mvp.model

import com.wmsj.baselibs.Const
import com.wmsj.baselibs.retrofit.BaseResponse
import com.wmsj.baselibs.retrofit.RetrofitHomeManager
import com.wmsj.baselibs.retrofit.RetrofitManager
import com.wmsj.baselibs.retrofit.RetrofitResourceManager
import com.wmsj.baselibs.bean.ImageBean
import com.wmsj.baselibs.scheduler.SchedulerUtils
import com.wmsj.baselibs.utils.StringUtils
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * Created by tian
on 2019/8/5.
 */
class TakePhotoOrVideoModel {

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
     * 视频上传
     */
    fun videoUpload(file: String): Observable<BaseResponse<ImageBean>> {
        var requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), File(file))
        var part = MultipartBody.Part.createFormData("file", file, requestFile)
        return RetrofitResourceManager(Const.BASE_URL).service.postVideo(part)
                .compose(SchedulerUtils.ioToMain())
    }


    /**
     * 随手拍发布
     */
    fun requestPulishData(map: Map<String, String>, file: String): Observable<BaseResponse<Any>> {
        var requestFile = RequestBody.create(MediaType.parse("image/jpg"), File(file))
        var part = MultipartBody.Part.createFormData("cover", file, requestFile)
        return if (StringUtils.isEmpty(file)) {
            RetrofitManager.service.culturePhotoCreate(map)
                    .compose(SchedulerUtils.ioToMain())
        } else {
            RetrofitManager.service.cultureVideoCreate(map, part)
                    .compose(SchedulerUtils.ioToMain())
        }
    }


    /**
     * 活动报道发布
     */
    fun pushOrgReport(map: Map<String, String>): Observable<BaseResponse<Any>> {
        return RetrofitHomeManager.service.pushOrgReport(map)
                .compose(SchedulerUtils.ioToMain())
    }
}