package com.wmsj.baselibs.mvp.model

import com.wmsj.baselibs.retrofit.BaseResponse
import com.wmsj.baselibs.retrofit.RetrofitHomeManager
import com.wmsj.baselibs.retrofit.RetrofitManager
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.bean.ConventientPhotoBean
import com.wmsj.baselibs.bean.HomeBanner
import com.wmsj.baselibs.bean.MyTakePhotoBean
import com.wmsj.baselibs.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by tian
on 2019/8/5.
 */
class ConvenientPhotoModel {

    /**
     * 轮播图
     */
    fun getHomeBanner(): Observable<BaseResponse<List<HomeBanner>>> {
        var map = HashMap<String, String>()
        map.put("key", Const.BANNER_KEY)
        map.put("page", "1")
        map.put("pagesize", "8")

        return RetrofitHomeManager.service.getHomeBanner(map)
                .compose(SchedulerUtils.ioToMain())
    }


    /**
     * 随手拍
     */
    fun requestHomeData(map: Map<String, String>): Observable<BaseResponse<List<ConventientPhotoBean>>> {
        return RetrofitHomeManager.service.getCulturePhotoLists(map)
                .compose(SchedulerUtils.ioToMain())
    }


    /**
     * 我的随手拍
     */
    fun requestMyCulture(map: Map<String, String>): Observable<BaseResponse<List<MyTakePhotoBean>>> {
        return RetrofitManager.service.myTakePhotoList(map)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 删除我的随手拍
     */
    fun deleteMyCulture(map: Map<String, String>): Observable<BaseResponse<Any>> {
        return RetrofitHomeManager.service.myTakePhotoDelete(map)
                .compose(SchedulerUtils.ioToMain())
    }
}