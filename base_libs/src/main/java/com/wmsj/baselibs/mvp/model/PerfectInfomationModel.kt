package com.wmsj.baselibs.mvp.model

import com.wmsj.baselibs.retrofit.BaseResponse
import com.wmsj.baselibs.retrofit.RetrofitHomeManager
import com.wmsj.baselibs.retrofit.RetrofitManager
import com.wmsj.baselibs.bean.AreaBean
import com.wmsj.baselibs.bean.UserInfo
import com.wmsj.baselibs.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by tian
on 2019/7/12.
 */
class PerfectInfomationModel {


    /**
     * 获取地区
     */
    fun getArea(area: String): Observable<BaseResponse<List<AreaBean>>> {
        val map = HashMap<String, String>()
        map.put("id", area)
        return RetrofitHomeManager.service.getArea(map)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 完善信息
     */
    fun doPerfectInfo(map: HashMap<String, String>): Observable<BaseResponse<Any>> {
        return RetrofitManager.service.completeBaseInfo(map)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 志愿者信息
     */
    fun getUserInfo(uid: String): Observable<BaseResponse<UserInfo>> {
        val map = HashMap<String, String>()
        map.put("userid", uid)
        return RetrofitManager.service.getUserInfo(map)
                .compose(SchedulerUtils.ioToMain())
    }
}