package com.wmsj.baselibs.mvp.model

import com.wmsj.baselibs.retrofit.BaseResponse
import com.wmsj.baselibs.retrofit.RetrofitManager
import com.wmsj.baselibs.BaseApplication
import com.wmsj.baselibs.bean.ActivityService
import com.wmsj.baselibs.bean.ClockCardBean
import com.wmsj.baselibs.bean.MyActivityBean
import com.wmsj.baselibs.scheduler.SchedulerUtils
import com.wmsj.baselibs.utils.SPUtil
import io.reactivex.Observable

/**
 * Created by tian
on 2019/8/5.
 */
class MyActsModel {
    /**
     * 获取我的活动
     */
    fun requestHomeData(type: String, page: String): Observable<BaseResponse<List<MyActivityBean>>> {
        var map = HashMap<String, String>()
        map.put("userid", SPUtil.get(BaseApplication.appInstance, "userId", "").toString())
        map.put("status", type)
        map.put("page", page)
        map.put("pagesize", "8")
        return RetrofitManager.service.getMyActivity(map)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 签到
     */
    fun checkCard(map: HashMap<String, String>): Observable<BaseResponse<ClockCardBean>> {
        return RetrofitManager.service.checkCard(map)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 志愿者取消报名
     */
    fun removeSign(map: HashMap<String, String>): Observable<BaseResponse<Any>> {
        return RetrofitManager.service.removeSign(map)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 服务详情
     */
    fun getActivityService(map: HashMap<String, String>): Observable<BaseResponse<ActivityService>> {
        return RetrofitManager.service.getActivityService(map)
                .compose(SchedulerUtils.ioToMain())
    }
}