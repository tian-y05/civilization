package com.wmsj.baselibs.mvp.model

import com.wmsj.baselibs.retrofit.BaseResponse
import com.wmsj.baselibs.retrofit.RetrofitHomeManager
import com.wmsj.baselibs.retrofit.RetrofitManager
import com.wmsj.baselibs.bean.RegistBean
import com.wmsj.baselibs.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by tian
on 2019/7/12.
 */
class RegisterModel {

    /**
     * 注册
     */
    fun doRegister(map: HashMap<String, String>): Observable<BaseResponse<RegistBean>> {
        return RetrofitHomeManager.service.doRegist(map)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 组织注册
     */
    fun doOrgRegister(map: HashMap<String, String>): Observable<BaseResponse<Any>> {
        return RetrofitHomeManager.service.orgRegister(map)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 获取验证码
     */
    fun getObtainCode(phone: String, type: String): Observable<BaseResponse<Any>> {
        val map = HashMap<String, String>()
        map.put("mobile", phone)
        map.put("type", type)
        return RetrofitHomeManager.service.getUserSend(map)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 忘记密码
     */
    fun doForgetpwd(map: HashMap<String, String>, type: String): Observable<BaseResponse<Any>> {
        if (type == "org") {
            return RetrofitHomeManager.service.doOrgForgetpwd(map)
                    .compose(SchedulerUtils.ioToMain())
        } else {
            return RetrofitHomeManager.service.doForgetpwd(map)
                    .compose(SchedulerUtils.ioToMain())
        }
    }

    /**
     * 修改手机号
     */
    fun doEditTel(map: HashMap<String, String>, role: String): Observable<BaseResponse<Any>> {
        return if (role == "zyz") RetrofitManager.service.editTel(map)
                .compose(SchedulerUtils.ioToMain()) else RetrofitManager.service.orgChangeMobile(map)
                .compose(SchedulerUtils.ioToMain())
    }

}