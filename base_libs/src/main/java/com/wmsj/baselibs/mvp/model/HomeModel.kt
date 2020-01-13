package com.wmsj.baselibs.mvp.model

import com.wmsj.baselibs.Const
import com.wmsj.baselibs.retrofit.BaseResponse
import com.wmsj.baselibs.retrofit.RetrofitHomeManager
import com.wmsj.baselibs.bean.BaseInfoBean
import com.wmsj.baselibs.bean.HomeActivityListBean
import com.wmsj.baselibs.bean.HomeBanner
import com.wmsj.baselibs.bean.HomeColumnsBean
import com.wmsj.baselibs.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by tian
on 2019/8/5.
 */
class HomeModel {

    /**
     * 首页轮播图
     */
    fun getHomeBanner(): Observable<BaseResponse<List<HomeBanner>>> {
        var map = HashMap<String, String>()
        map.put("key", Const.BANNER_KEY)
        map.put("page", "1")
        map.put("pagesize", "8")

        return RetrofitHomeManager.service.getHomeBanner(map)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 首页栏目列表
     */
    fun getHomeColumns(type:Int): Observable<BaseResponse<List<HomeColumnsBean>>> {
        var map = HashMap<String, Int>()
        map.put("type", type)
        return RetrofitHomeManager.service.getHomeColumns(map)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 首页栏目列表
     */
    fun getHomeApps(): Observable<BaseResponse<List<HomeColumnsBean>>> {
        return RetrofitHomeManager.service.getHomeApps()
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 首页最新活动
     */
    fun getHomeNewsActivity(page : Int): Observable<BaseResponse<List<HomeActivityListBean>>> {
        var map = HashMap<String, String>()
        map.put("page", page.toString())
        map.put("pagesize", "8")
        return RetrofitHomeManager.service.getHomeNews(map)
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
}