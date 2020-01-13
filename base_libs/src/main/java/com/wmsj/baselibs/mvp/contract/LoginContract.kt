package com.wmsj.baselibs.mvp.contract


import com.wmsj.baselibs.base.IBaseView
import com.wmsj.baselibs.base.IPresenter
import com.wmsj.baselibs.bean.LoginInfoBean

/**
 * Created by tian
on 2019/7/12.
 */
class LoginContract {

    interface View : IBaseView{

        /**
         * 登录成功回调
         */
        fun doLoginResult(data: LoginInfoBean)


        /**
         * 失败
         */
        fun showError(errorMsg: String,errorCode:Int)
    }

    interface Presenter : IPresenter<View> {

        /**
         * 请求登录
         */
        fun doLogin(username: String,password: String)


        /**
         * 请求志愿组织登录
         */
        fun doOrgLogin(username: String,password: String)
    }
}