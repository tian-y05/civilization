package com.wmsj.baselibs.mvp.contract

import com.wmsj.baselibs.base.IBaseView
import com.wmsj.baselibs.base.IPresenter

import com.wmsj.baselibs.bean.RegistBean

/**
 * Created by tian
on 2019/7/12.
 */
class RegisterContract {

    interface View : IBaseView {

        /**
         *  验证码获取成功
         */
        fun obtainCodeResult(data: String)

        /**
         * 注册
         */
        fun doRegistResult(data: RegistBean)

        /**
         * 组织注册
         */
        fun doOrgRegistResult(data: String)

        /**
         *  忘记密码
         */
        fun doForgetpwd(data: String)

        /**
         *  修改返回结果
         */
        fun doEditResult(data: String)

        /**
         * 失败
         */
        fun showError(errorMsg: String, errorCode: Int)
    }

    interface Presenter : IPresenter<View> {

        /**
         * 获取验证码
         */
        fun getObtainCode(phone: String, type: String)

        /**
         * 志愿者注册
         */
        fun doRegist(map: HashMap<String, String>)

        /**
         * 忘记密码
         */
        fun doForgetpwd(map: HashMap<String, String>, type: String)

        /**
         * 修改号码
         */
        fun doEditTel(map: HashMap<String, String>, role: String)

        /**
         * 组织注册
         */
        fun doOrgRegist(map: HashMap<String, String>)
    }
}