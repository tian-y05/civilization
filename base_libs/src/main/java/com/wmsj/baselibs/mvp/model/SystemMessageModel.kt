package com.wmsj.baselibs.mvp.model

import com.wmsj.baselibs.retrofit.BaseResponse
import com.wmsj.baselibs.retrofit.RetrofitManager
import com.wmsj.baselibs.BaseApplication
import com.wmsj.baselibs.bean.SystemNewsBean
import com.wmsj.baselibs.scheduler.SchedulerUtils
import com.wmsj.baselibs.utils.SPUtil
import io.reactivex.Observable

/**
 * Created by tian
on 2019/8/5.
 */
class SystemMessageModel {
    /**
     * 系统通知
     */
    fun requestHomeData(page: String): Observable<BaseResponse<List<SystemNewsBean>>> {
        var map = HashMap<String, String>()
        map.put("userid", SPUtil.get(BaseApplication.appInstance, "userId", "").toString())
        map.put("page", page)
        map.put("pagesize", "8")
        return RetrofitManager.service.systemNews(map)
                .compose(SchedulerUtils.ioToMain())
    }

}