package com.wmsj.baselibs.mvp.model

import com.wmsj.baselibs.scheduler.SchedulerUtils
import com.wmsj.baselibs.retrofit.BaseResponse
import com.wmsj.baselibs.retrofit.RetrofitHomeManager
import com.wmsj.baselibs.bean.ArticileOrgList
import com.wmsj.baselibs.bean.OrgCataBean
import com.wmsj.baselibs.bean.OrgSonList
import io.reactivex.Observable

/**
 * Created by tian
on 2019/8/5.
 */
class ArticleSortModel {

    /**
     * 获取组织一级
     */
    fun requestActivityCate(): Observable<BaseResponse<List<OrgCataBean>>> {
        var map = HashMap<String, String>()
        map.put("page", "1")
        map.put("pagesize", "3")
        return RetrofitHomeManager.service.getOrgcate(map)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 获取组织二级
     */
    fun requestActivitySonCate(key: String): Observable<BaseResponse<List<OrgSonList>>> {
        var map = HashMap<String, String>()
        map.put("cate_id", key)
        return RetrofitHomeManager.service.getLastcate(map)
                .compose(SchedulerUtils.ioToMain())
    }


    /**
     * 获取组织活动数据
     */
    fun requestHomeData(key: String, page: String): Observable<BaseResponse<List<ArticileOrgList>>> {
        var map = HashMap<String, String>()
        map.put("cate", key)
        map.put("page", page)
        map.put("pagesize", "8")
        return RetrofitHomeManager.service.getArticleOrg(map)
                .compose(SchedulerUtils.ioToMain())
    }

}