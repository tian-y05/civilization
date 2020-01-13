package com.wmsj.baselibs.mvp.model

import com.wmsj.baselibs.Const
import com.wmsj.baselibs.retrofit.BaseResponse
import com.wmsj.baselibs.retrofit.RetrofitHomeManager
import com.wmsj.baselibs.bean.BaseInfoBean
import com.wmsj.baselibs.bean.VersionBean
import com.wmsj.baselibs.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by tian
on 2019/7/12.
 */
class MainModel {

    /**
     * 客户端应用权限
     */
    fun getClientsAuth(): Observable<BaseResponse<Any>> {

        return RetrofitHomeManager.service.getClientsAuth()
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 获取基本信息
     */
    fun getBaseInfo(type: String): Observable<BaseResponse<List<BaseInfoBean>>> {
        val map = HashMap<String, String>()
        map.put("type", type)
        return RetrofitHomeManager.service.getBaseInfo(map)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 获取版本信息
     */
    fun getVersion(): Observable<BaseResponse<VersionBean>> {
        val map = HashMap<String, String>()
        map.put("client", Const.API_TOKEN)
        return RetrofitHomeManager.service.getVersion(map)
                .compose(SchedulerUtils.ioToMain())
    }
}