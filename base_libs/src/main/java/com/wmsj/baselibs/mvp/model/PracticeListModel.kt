package com.wmsj.baselibs.mvp.model

import com.wmsj.baselibs.scheduler.SchedulerUtils
import com.wmsj.baselibs.retrofit.BaseResponse
import com.wmsj.baselibs.retrofit.RetrofitHomeManager
import com.wmsj.baselibs.bean.PracticeBaseBean
import io.reactivex.Observable

/**
 * Created by tian
on 2019/8/5.
 */
class PracticeListModel {
    /**
     * 获取实践基地
     */
    fun requestHomeData(page: String): Observable<BaseResponse<List<PracticeBaseBean>>> {
        var map = HashMap<String, String>()
        map.put("page", page)
        map.put("pagesize", "8")
        return RetrofitHomeManager.service.getPracticeLists(map)
                .compose(SchedulerUtils.ioToMain())
    }

}