package com.wmsj.baselibs.mvp.model

import com.wmsj.baselibs.retrofit.BaseResponse
import com.wmsj.baselibs.retrofit.RetrofitManager
import com.wmsj.baselibs.bean.OrgMemberBean
import com.wmsj.baselibs.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by tian
on 2019/8/5.
 */
class EmployMemberModel {

    /**
     * 组织成员
     */
    fun requestHomeData(map: HashMap<String, String>): Observable<BaseResponse<List<OrgMemberBean>>> {
        return RetrofitManager.service.orgMember(map)
                .compose(SchedulerUtils.ioToMain())
    }


    /**
     * 组织录用
     */
    fun orgEmployment(map: HashMap<String, String>): Observable<BaseResponse<Any>> {
        return RetrofitManager.service.orgEmployment(map)
                .compose(SchedulerUtils.ioToMain())
    }
}