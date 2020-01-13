package com.wmsj.baselibs.mvp.model

import com.wmsj.baselibs.retrofit.BaseResponse
import com.wmsj.baselibs.retrofit.RetrofitManager
import com.wmsj.baselibs.BaseApplication
import com.wmsj.baselibs.bean.ServiceRecordBean
import com.wmsj.baselibs.scheduler.SchedulerUtils
import com.wmsj.baselibs.utils.SPUtil
import io.reactivex.Observable

/**
 * Created by tian
on 2019/8/5.
 */
class ServiceRecordModel {
    /**
     * 获取我的活动
     */
    fun requestHomeData(page: String): Observable<BaseResponse<ServiceRecordBean>> {
        var map = HashMap<String, String>()
        map.put("api-token", SPUtil.get(BaseApplication.appInstance, "userId", "").toString())
        map.put("page", page)
        map.put("pagesize", "8")
        return RetrofitManager.service.serviceRecordList(map)
                .compose(SchedulerUtils.ioToMain())
    }

}