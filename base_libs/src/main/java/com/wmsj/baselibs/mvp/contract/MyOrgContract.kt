package com.wmsj.baselibs.mvp.contract

import com.wmsj.baselibs.base.IBaseView
import com.wmsj.baselibs.base.IPresenter
import com.wmsj.baselibs.bean.MyOrgList

/**se
 * Created by tian
on 2019/8/5.
 */
class MyOrgContract {

    interface View : IBaseView {


        /**
         * 我的组织列表
         */
        fun setModelsData(data: List<MyOrgList>)
        /**
         * 退出组织
         */
        fun exitOrgResult(data: String)


        /**
         * 显示错误信息
         */
        fun showError(msg: String, errorCode: Int)

    }


    interface Presenter : IPresenter<View> {

        /**
         * 我的组织列表
         */
        fun requestModelsData(state: String, num: String)
        /**
         * 退出组织
         */
        fun exitOrg(id: String)

    }
}