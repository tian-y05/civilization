package com.wmsj.baselibs.mvp.model

import com.wmsj.baselibs.retrofit.BaseResponse
import com.wmsj.baselibs.retrofit.RetrofitHomeManager
import com.wmsj.baselibs.retrofit.RetrofitManager
import com.wmsj.baselibs.bean.EventsBean
import com.wmsj.baselibs.bean.HomeActivityListBean
import com.wmsj.baselibs.scheduler.SchedulerUtils
import com.wmsj.baselibs.utils.StringUtils
import io.reactivex.Observable

/**
 * Created by tian
on 2019/8/5.
 */
class EventsManagementModel {
    /**
     * 获取首页活动
     */
    fun requestHomeData(type: String, page: String, name: String): Observable<BaseResponse<List<HomeActivityListBean>>> {
        var map = HashMap<String, String>()
        map.put("state", type)
        map.put("page", page)
        map.put("pagesize", "8")
        if (!StringUtils.isEmpty(name)) {
            map.put("name", name)
        }
        return RetrofitHomeManager.service.getHomeNews(map)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 获取组织活动管理
     */
    fun requestOrgActs(type: String, page: String): Observable<BaseResponse<List<EventsBean>>> {
        var map = HashMap<String, String>()
        map.put("type", type)
        map.put("page", page)
        map.put("pagesize", "8")
        return RetrofitManager.service.getEventsData(map)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 撤回草稿
     */
    fun backDraft(map: HashMap<String, String>): Observable<BaseResponse<Any>> {
        return RetrofitManager.service.backDraft(map)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 活动发布 删除
     */
    fun actsManage(map: HashMap<String, String>): Observable<BaseResponse<Any>> {
        return RetrofitManager.service.actsManage(map)
                .compose(SchedulerUtils.ioToMain())
    }
}