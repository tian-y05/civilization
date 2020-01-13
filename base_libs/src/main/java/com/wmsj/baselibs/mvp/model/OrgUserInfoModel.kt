package com.wmsj.baselibs.mvp.model

import com.wmsj.baselibs.retrofit.BaseResponse
import com.wmsj.baselibs.retrofit.RetrofitManager
import com.wmsj.baselibs.bean.OrgUserInfo
import com.wmsj.baselibs.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by tian
on 2019/8/5.
 */
class OrgUserInfoModel {

    /**
     * 志愿者信息
     */
    fun orgUserInfo(map: HashMap<String, String>): Observable<BaseResponse<OrgUserInfo>> {
        return RetrofitManager.service.orgUserInfo(map)
                .compose(SchedulerUtils.ioToMain())
    }

}