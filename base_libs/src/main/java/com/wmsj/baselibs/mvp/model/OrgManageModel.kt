package com.wmsj.baselibs.mvp.model

import com.wmsj.baselibs.retrofit.BaseResponse
import com.wmsj.baselibs.retrofit.RetrofitManager
import com.wmsj.baselibs.bean.HangupOrgBean
import com.wmsj.baselibs.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by tian
on 2019/8/5.
 */
class OrgManageModel {
    /**
     * 组织挂靠
     */
    fun requestHomeData(type: String): Observable<BaseResponse<List<HangupOrgBean>>> {
        var map = HashMap<String, String>()
        map.put("type", type)
        return RetrofitManager.service.hangupOrgList(map)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 组织挂靠搜索
     */
    fun hungupSearch(keyword: String): Observable<BaseResponse<List<HangupOrgBean>>> {
        var map = HashMap<String, String>()
        map.put("keyword", keyword)
        return RetrofitManager.service.hangupOrgSearch(map)
                .compose(SchedulerUtils.ioToMain())
    }


    /**
     * 组织挂靠操作
     */
    fun hangupOrgOperation(state: String, id: String): Observable<BaseResponse<Any>> {
        var map = HashMap<String, String>()
        map.put("state", state)
        map.put("id", id)
        return RetrofitManager.service.hangupOrgOperation(map)
                .compose(SchedulerUtils.ioToMain())
    }
}