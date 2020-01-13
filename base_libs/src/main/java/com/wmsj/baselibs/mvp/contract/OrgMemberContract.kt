package com.wmsj.baselibs.mvp.contract

import com.wmsj.baselibs.base.IBaseView
import com.wmsj.baselibs.base.IPresenter
import com.wmsj.baselibs.bean.OrgMemberManageBean

/**se
 * Created by tian
on 2019/8/5.
 */
class OrgMemberContract {

    interface View : IBaseView {


        /**
         * 我的组织列表
         */
        fun setModelsData(data: List<OrgMemberManageBean>)

        /**
         * 加入审核移除组织
         */
        fun orgCheckResult(data: String)

        /**
         * 显示错误信息
         */
        fun showError(msg: String, errorCode: Int)

    }


    interface Presenter : IPresenter<View> {

        /**
         * 组织成员管理
         */
        fun orgMemberManage(state: String, num: String)

        /**
         * 加入审核移除组织
         */
        fun orgMemberCheck(type: String, id: String)
    }
}