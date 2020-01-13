package com.wmsj.baselibs.mvp.model

import com.wmsj.baselibs.retrofit.BaseResponse
import com.wmsj.baselibs.retrofit.RetrofitManager
import com.wmsj.baselibs.bean.ActsPerson
import com.wmsj.baselibs.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by tian
on 2019/8/5.
 */
class RecruitMemberModel {
    /**
     * 组织招募待审核人数  //1:招募人数，2：待审核人数，3:参与人数
     */
    fun requestHomeData(type: String, id: String, keyword: String, page: String): Observable<BaseResponse<ActsPerson>> {
        var map = HashMap<String, String>()
        map.put("type", type)
        map.put("id", id)
        map.put("keyword", keyword)
        map.put("page", page)
        map.put("pagesize", "8")
        return RetrofitManager.service.orgActsPerson(map)
                .compose(SchedulerUtils.ioToMain())
    }


    /**
     * 组织取消报名 录用操作
     */
    fun orgActsState(id: String, state: String): Observable<BaseResponse<Any>> {
        var map = HashMap<String, String>()
        map.put("type", state)
        map.put("id", id)
        return RetrofitManager.service.orgActsState(map)
                .compose(SchedulerUtils.ioToMain())
    }


    /**
     * 时长录入
     */
    fun timeEnter(map: HashMap<String, String>): Observable<BaseResponse<Any>> {
        return RetrofitManager.service.timeEnter(map)
                .compose(SchedulerUtils.ioToMain())
    }
}