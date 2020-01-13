package com.wmsj.baselibs.mvp.model

import com.wmsj.baselibs.retrofit.BaseResponse
import com.wmsj.baselibs.retrofit.RetrofitManager
import com.wmsj.baselibs.BaseApplication
import com.wmsj.baselibs.bean.MyOrgList
import com.wmsj.baselibs.scheduler.SchedulerUtils
import com.wmsj.baselibs.utils.SPUtil
import io.reactivex.Observable

/**
 * Created by tian
on 2019/8/5.
 */
class MyOrgModel {


    /**
     * 获取组织活动数据
     */
    fun requestHomeData(key: String, page: String): Observable<BaseResponse<List<MyOrgList>>> {
        var map = HashMap<String, String>()
        map.put("state", key)
        map.put("userid", SPUtil.get(BaseApplication.appInstance, "userId", "").toString())
        map.put("page", page)
        map.put("pagesize", "8")
        return RetrofitManager.service.myOrgList(map)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 退出组织
     */
    fun exitOrg(id: String): Observable<BaseResponse<Any>> {
        var map = HashMap<String, String>()
        map.put("orgid", id)
        return RetrofitManager.service.exitOrg(map)
                .compose(SchedulerUtils.ioToMain())
    }

}