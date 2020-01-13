package com.wmsj.baselibs.mvp.model

import com.wmsj.baselibs.retrofit.BaseResponse
import com.wmsj.baselibs.retrofit.RetrofitManager
import com.wmsj.baselibs.BaseApplication
import com.wmsj.baselibs.bean.ActivityShowBean
import com.wmsj.baselibs.scheduler.SchedulerUtils
import com.wmsj.baselibs.utils.SPUtil
import io.reactivex.Observable

/**
 * Created by tian
on 2019/7/12.
 */
class EventDetailsModel {

    /**
     * 活动详情
     */
    fun getEventDetails(id: String): Observable<BaseResponse<ActivityShowBean>> {
        var map = HashMap<String, String>()
        map.put("id", id)
        return RetrofitManager.service.getActivityShow(map)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 活动报名
     */
    fun joinActivity(aid: String): Observable<BaseResponse<Any>> {
        var map = HashMap<String, String>()
        map.put("userid", SPUtil.get(BaseApplication.appInstance, "userId", "").toString())
        map.put("aid", aid)
        return RetrofitManager.service.joinActivity(map)
                .compose(SchedulerUtils.ioToMain())
    }
}