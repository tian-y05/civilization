package com.wmsj.baselibs.mvp.model

import com.wmsj.baselibs.scheduler.SchedulerUtils
import com.wmsj.baselibs.retrofit.BaseResponse
import com.wmsj.baselibs.retrofit.RetrofitHomeManager
import com.wmsj.baselibs.bean.HomeColumnsBean
import io.reactivex.Observable

/**
 * Created by tian
on 2019/8/5.
 */
class PlatformShowModel {
    /**
     * 获取组织活动数据
     */
    fun requestColumnData(type: Int): Observable<BaseResponse<List<HomeColumnsBean>>> {
        var map = HashMap<String, Int>()
        map.put("type", type)
        return RetrofitHomeManager.service.getHomeColumns(map)
                .compose(SchedulerUtils.ioToMain())
    }

}