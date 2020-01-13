package com.wmsj.baselibs.mvp.model

import com.wmsj.baselibs.retrofit.BaseResponse
import com.wmsj.baselibs.retrofit.RetrofitHomeManager
import com.wmsj.baselibs.bean.HomeActivityListBean
import com.wmsj.baselibs.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by tian
on 2019/8/5.
 */
class StationActivityModel {
    /**
     * 获取所站风貌相关活动数据
     */
    fun requestHomeData(station_id: String, state: String, page: String): Observable<BaseResponse<List<HomeActivityListBean>>> {
        var map = HashMap<String, String>()
        map.put("station_id", station_id)
        map.put("state", state)
        map.put("page", page)
        map.put("pagesize", "8")
        return RetrofitHomeManager.service.getStationActivityLists(map)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 获取所站风貌相关活动数据
     */
    fun requestOrgActivityList(station_id: String, state: String, page: String): Observable<BaseResponse<List<HomeActivityListBean>>> {
        var map = HashMap<String, String>()
        map.put("oid", station_id)
        map.put("state", state)
        map.put("page", page)
        map.put("pagesize", "8")
        return RetrofitHomeManager.service.getOrgActivityList(map)
                .compose(SchedulerUtils.ioToMain())
    }

}