package com.wmsj.baselibs.mvp.model

import com.wmsj.baselibs.retrofit.BaseResponse
import com.wmsj.baselibs.retrofit.RetrofitHomeManager
import com.wmsj.baselibs.bean.IdentityInfoBean
import com.wmsj.baselibs.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by tian
on 2019/7/12.
 */
class FindIdentityModel {

    /**
     * 手机号验证
     */
    fun verificationPhone(map: HashMap<String, String>): Observable<BaseResponse<Any>> {
        return RetrofitHomeManager.service.verificationPhone(map)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 删除信息
     */
    fun deleteMatchInfo(code: String): Observable<BaseResponse<Any>> {
        val map = HashMap<String, String>()
        map.put("code", code)
        return RetrofitHomeManager.service.deleteAccount(map)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 匹配身份信息
     */
    fun machIdentityInfo(map: HashMap<String, String>): Observable<BaseResponse<List<IdentityInfoBean>>> {
        return RetrofitHomeManager.service.machIdentityInfo(map)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 获取验证码
     */
    fun getObtainCode(phone: String): Observable<BaseResponse<Any>> {
        val map = HashMap<String, String>()
        map.put("mobile", phone)
        map.put("type", "zyz")
        return RetrofitHomeManager.service.getUserSend(map)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 绑定身份
     */
    fun bindingIdentity(map: HashMap<String, String>): Observable<BaseResponse<Any>> {
        return RetrofitHomeManager.service.bindIdentity(map)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 重置密码
     */
    fun resetPwd(map: HashMap<String, String>): Observable<BaseResponse<Any>> {
        return RetrofitHomeManager.service.resetPwd(map)
                .compose(SchedulerUtils.ioToMain())
    }
}