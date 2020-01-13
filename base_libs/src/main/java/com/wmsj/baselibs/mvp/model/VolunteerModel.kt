package com.wmsj.baselibs.mvp.model

import com.wmsj.baselibs.retrofit.BaseResponse
import com.wmsj.baselibs.retrofit.RetrofitManager
import com.wmsj.baselibs.bean.OrgBaseInfoBean
import com.wmsj.baselibs.bean.UserCentreBean
import com.wmsj.baselibs.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by tian
on 2019/7/12.
 */
class VolunteerModel {

    /**
     * 志愿者中心
     */
    fun getUserCentre(uid: String): Observable<BaseResponse<UserCentreBean>> {
        var map = HashMap<String, String>()
        map.put("userid", uid)
        return RetrofitManager.service.getUserCentre(map)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 组织信息
     */
    fun getUserInfo(uid: String): Observable<BaseResponse<OrgBaseInfoBean>> {
        val map = HashMap<String, String>()
        map.put("orgid", uid)
        return RetrofitManager.service.myOrgInfo(map)
                .compose(SchedulerUtils.ioToMain())
    }
}