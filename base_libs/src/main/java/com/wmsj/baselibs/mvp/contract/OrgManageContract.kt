package com.wmsj.baselibs.mvp.contract


import com.wmsj.baselibs.base.IBaseView
import com.wmsj.baselibs.base.IPresenter
import com.wmsj.baselibs.bean.HangupOrgBean

/**
 * Created by tian
on 2019/8/5.
 */
class OrgManageContract {

    interface View : IBaseView {

        /**
         * 模块数据
         */
        fun setModelsData(data: List<HangupOrgBean>)


        /**
         * 操作结果
         */
        fun operationResult(data: String)

        /**
         * 显示错误信息
         */
        fun showError(msg: String, errorCode: Int)

    }


    interface Presenter : IPresenter<View> {

        /**
         * 挂靠组织列表
         */
        fun requestModelsData(type: String)

        /**
         * 挂靠组织操作
         */
        fun hangupOrgOperation(state: String, id: String)

        /**
         * 挂靠组织搜索
         */
        fun hangupOrgSearch(keyword: String)

    }
}