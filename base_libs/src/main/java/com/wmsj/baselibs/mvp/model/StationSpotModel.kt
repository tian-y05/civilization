package com.wmsj.baselibs.mvp.model

import com.wmsj.baselibs.scheduler.SchedulerUtils
import com.wmsj.baselibs.retrofit.BaseResponse
import com.wmsj.baselibs.retrofit.RetrofitHomeManager
import com.wmsj.baselibs.bean.StationListBean
import io.reactivex.Observable

/**
 * Created by tian
on 2019/8/5.
 */
class StationSpotModel {
    /**
     * 获取所站风貌实践站数据
     */
    fun requestHomeData(station_id: String, page: String): Observable<BaseResponse<List<StationListBean>>> {
        var map = HashMap<String, String>()
        map.put("station_id", station_id)
        map.put("page", page)
        map.put("pagesize", "8")
        return RetrofitHomeManager.service.getStationSpotLists(map)
                .compose(SchedulerUtils.ioToMain())
    }

}