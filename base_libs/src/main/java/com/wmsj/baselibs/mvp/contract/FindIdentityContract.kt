package com.wmsj.baselibs.mvp.contract

import com.wmsj.baselibs.retrofit.BaseResponse
import com.wmsj.baselibs.base.IBaseView
import com.wmsj.baselibs.base.IPresenter

import com.wmsj.baselibs.bean.IdentityInfoBean

/**
 * Created by tian
on 2019/7/12.
 */
class FindIdentityContract {

    interface View : IBaseView {

        /**
         *  验证码获取成功
         */
        fun obtainCodeResult(data: String)

        /**
         *  手机号验证
         */
        fun verificationResult(data: String)

        /**
         *  删除信息
         */
        fun deleteInfoResult(data: String)

        /**
         *  绑定身份
         */
        fun bindingInfoResult(data: String)
        /**
         * 匹配身份信息
         */
        fun machInfoResult(data: BaseResponse<List<IdentityInfoBean>>)

        /**
         * 重置密码
         */
        fun resetResult(data: String)


        /**
         * 失败
         */
        fun showError(errorMsg: String, errorCode: Int)
    }

    interface Presenter : IPresenter<View> {

        /**
         * 获取验证码
         */
        fun getObtainCode(phone: String)

        /**
         * 手机号验证
         */
        fun verificationPhone(map: HashMap<String, String>)

        /**
         * 匹配身份信息
         */
        fun machIdentityInfo(map: HashMap<String, String>)

        /**
         * 删除匹配身份信息
         */
        fun deleteMachInfo(code: String)

        /**
         * 绑定身份
         */
        fun bindIdentity(map: HashMap<String, String>)

        /**
         * 重置密码
         */
        fun resetPwd(map: HashMap<String, String>)


    }
}