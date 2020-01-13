package com.wmsj.baselibs.mvp.model

import com.wmsj.baselibs.retrofit.BaseResponse
import com.wmsj.baselibs.retrofit.RetrofitHomeManager
import com.wmsj.baselibs.bean.ArticileOrgList
import com.wmsj.baselibs.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by tian
on 2019/8/5.
 */
class OrgSearchModel {
    /**
     * 查找组织
     */
    fun requestHomeData(key: String, page: String): Observable<BaseResponse<List<ArticileOrgList>>> {
        var map = HashMap<String, String>()
        map.put("keyword", key)
        map.put("page", page)
        map.put("pagesize", "8")
        return RetrofitHomeManager.service.searchOrgList(map)
                .compose(SchedulerUtils.ioToMain())
    }

}