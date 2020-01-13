package com.wmsj.baselibs.mvp.model

import com.wmsj.baselibs.retrofit.BaseResponse
import com.wmsj.baselibs.retrofit.RetrofitHomeManager
import com.wmsj.baselibs.bean.LoginInfoBean
import com.wmsj.baselibs.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by tian
on 2019/7/12.
 */
class LoginModel {

    /**
     * 志愿者登录
     */
    fun doLogin(username: String, password: String): Observable<BaseResponse<LoginInfoBean>> {
        var map = HashMap<String, String>()
        map.put("username", username)
        map.put("password", password)

        return RetrofitHomeManager.service.doLogin(map)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 志愿组织登录
     */
    fun doOrgLogin(username: String, password: String): Observable<BaseResponse<LoginInfoBean>> {
        var map = HashMap<String, String>()
        map.put("user", username)
        map.put("pwd", password)
        map.put("wxid", "")
        return RetrofitHomeManager.service.doOrgLogin(map)
                .compose(SchedulerUtils.ioToMain())
    }
}