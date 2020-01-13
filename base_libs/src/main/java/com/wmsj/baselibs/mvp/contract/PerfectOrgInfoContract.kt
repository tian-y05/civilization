package com.wmsj.baselibs.mvp.contract

import android.content.Context
import com.wmsj.baselibs.base.IBaseView
import com.wmsj.baselibs.base.IPresenter
import com.wmsj.baselibs.bean.AreaBean
import com.wmsj.baselibs.bean.OrgListBean

/**
 * Created by tian
on 2019/7/12.
 */
class PerfectOrgInfoContract {

    interface View : IBaseView {

        /**
         * 完善信息
         */
        fun perfectInfoResult(data: String)


        /**
         * 主管组织
         */
        fun getOrgResult(data: List<OrgListBean>)

        /**
         * 获取地区
         */
        fun areaResult(data: List<AreaBean>, type: String)


        /**
         * 失败
         */
        fun showError(errorMsg: String, errorCode: Int)

        /**
         * 图片上传
         */
        fun setImageUploadResult(data: ArrayList<String>,type:Int)
    }

    interface Presenter : IPresenter<View> {


        /**
         * 获取地区
         */
        fun getArea(id: String, type: String)

        /**
         * 完善信息
         */
        fun doPerfectInfo(map: HashMap<String, String>)

        /**
         * 获取主管组织
         */
        fun getOrg(id: String)

        /**
         * 图片上传
         */
        fun imageUpload(mContext: Context, files: ArrayList<String>,type:Int)
    }
}