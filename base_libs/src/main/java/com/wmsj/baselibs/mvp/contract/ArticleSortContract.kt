package com.wmsj.baselibs.mvp.contract

import com.wmsj.baselibs.base.IBaseView
import com.wmsj.baselibs.base.IPresenter
import com.wmsj.baselibs.bean.ArticileOrgList
import com.wmsj.baselibs.bean.OrgCataBean
import com.wmsj.baselibs.bean.OrgSonList

/**se
 * Created by tian
on 2019/8/5.
 */
class ArticleSortContract {

    interface View : IBaseView {

        /**
         * 活动一级列表
         */
        fun setActivityCate(data: List<OrgCataBean>)

        /**
         * 活动二级列表
         */
        fun setActivitySonCate(data: List<OrgSonList>)

        /**
         * 模块数据
         */
        fun setModelsData(data: List<ArticileOrgList>)


        /**
         * 显示错误信息
         */
        fun showError(msg: String, errorCode: Int)

    }


    interface Presenter : IPresenter<View> {

        /**
         * 活动一级列表
         */
        fun requestActivityCate()

        /**
         * 活动二级列表
         */
        fun requestActivitySonCate(key: String)

        /**
         * 获取模块数据
         */
        fun requestModelsData(key: String, num: String)

    }
}