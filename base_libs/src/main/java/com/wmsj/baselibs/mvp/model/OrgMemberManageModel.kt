package com.wmsj.baselibs.mvp.model

import com.wmsj.baselibs.retrofit.BaseResponse
import com.wmsj.baselibs.retrofit.RetrofitManager
import com.wmsj.baselibs.bean.OrgMemberManageBean
import com.wmsj.baselibs.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by tian
on 2019/8/5.
 */
class OrgMemberManageModel {


    /**
     * 组织成员管理列表
     */
    fun orgMemberManage(type: String, page: String): Observable<BaseResponse<List<OrgMemberManageBean>>> {
        var map = HashMap<String, String>()
        map.put("type", type)
        map.put("page", page)
        map.put("pagesize", "8")
        return RetrofitManager.service.orgManageMember(map)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 加入审核移除组织
     */
    fun orgMemberCheck(type: String, id: String): Observable<BaseResponse<Any>> {
        var map = HashMap<String, String>()
        map.put("type", type)
        map.put("id", id)
        return RetrofitManager.service.orgMemberCheck(map)
                .compose(SchedulerUtils.ioToMain())
    }

}