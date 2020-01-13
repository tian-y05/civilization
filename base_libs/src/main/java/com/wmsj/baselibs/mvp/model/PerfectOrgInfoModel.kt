package com.wmsj.baselibs.mvp.model

import com.wmsj.baselibs.Const
import com.wmsj.baselibs.retrofit.BaseResponse
import com.wmsj.baselibs.retrofit.RetrofitHomeManager
import com.wmsj.baselibs.retrofit.RetrofitManager
import com.wmsj.baselibs.retrofit.RetrofitResourceManager
import com.wmsj.baselibs.bean.AreaBean
import com.wmsj.baselibs.bean.ImageBean
import com.wmsj.baselibs.bean.OrgListBean
import com.wmsj.baselibs.scheduler.SchedulerUtils
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * Created by tian
on 2019/7/12.
 */
class PerfectOrgInfoModel {


    /**
     * 获取地区
     */
    fun getArea(area: String): Observable<BaseResponse<List<AreaBean>>> {
        val map = HashMap<String, String>()
        map.put("id", area)
        return RetrofitHomeManager.service.getArea(map)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 主管组织
     */
    fun getOrg(region_id: String): Observable<BaseResponse<List<OrgListBean>>> {
        val map = HashMap<String, String>()
        map.put("region_id", region_id)
        return RetrofitHomeManager.service.getOrg(map)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 完善信息
     */
    fun doPerfectInfo(map: HashMap<String, String>): Observable<BaseResponse<Any>> {
        return RetrofitManager.service.completeOrgInfo(map)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 图片上传
     */
    fun imageUpload(file: String): Observable<BaseResponse<ImageBean>> {
        var requestFile = RequestBody.create(MediaType.parse("image/*"), File(file))
        var part = MultipartBody.Part.createFormData("file", file, requestFile)
        return RetrofitResourceManager(Const.UPLOADURL).service.postImage(part)
                .compose(SchedulerUtils.ioToMain())
    }

}