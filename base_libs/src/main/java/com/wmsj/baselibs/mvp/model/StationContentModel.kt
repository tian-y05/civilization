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
class StationContentModel {
    /**
     * 获取组织活动数据
     */
    fun requestHomeData(page: String): Observable<BaseResponse<List<StationListBean>>> {
        var map = HashMap<String, String>()
        map.put("page", page)
        map.put("pagesize", "8")
        return RetrofitHomeManager.service.getStationLists(map)
                .compose(SchedulerUtils.ioToMain())
    }

}