package com.wmsj.baselibs.mvp.model

import com.wmsj.baselibs.scheduler.SchedulerUtils
import com.wmsj.baselibs.retrofit.BaseResponse
import com.wmsj.baselibs.retrofit.RetrofitHomeManager
import com.wmsj.baselibs.bean.ArticleBean
import io.reactivex.Observable

/**
 * Created by tian
on 2019/8/5.
 */
class ArticleListModel {
    /**
     * 获取组织活动数据
     */
    fun requestHomeData(key: String, page: String): Observable<BaseResponse<ArticleBean>> {
        var map = HashMap<String, String>()
        map.put("key", key)
        map.put("page", page)
        map.put("pagesize", "8")
        return RetrofitHomeManager.service.getArticleLists(map)
                .compose(SchedulerUtils.ioToMain())
    }

}