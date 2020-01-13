package com.wmsj.baselibs.ui.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.LoginContract
import com.wmsj.baselibs.mvp.presenter.LoginPresenter
import com.wmsj.baselibs.ui.activity.FindIdentityActivity
import com.wmsj.baselibs.ui.activity.ForgetPasswordActivity
import com.wmsj.baselibs.ui.activity.PerfectOrgInfoActivity
import com.wmsj.baselibs.ui.activity.RegisterActivity
import com.wmsj.baselibs.base.BaseFragment
import com.wmsj.baselibs.bean.LoginInfoBean
import com.wmsj.baselibs.eventBus.EventBusMessage
import com.wmsj.baselibs.eventBus.EventBusUtils
import com.wmsj.baselibs.utils.IntentUtils
import com.wmsj.baselibs.utils.SPUtil
import com.wmsj.baselibs.utils.StringUtils
import com.wmsj.baselibs.utils.ToastUtils
import com.wmsj.baselibs.view.SetSuccessDialog
import kotlinx.android.synthetic.main.fragment_login_org.*

/**
 * 志愿组织登录
on 2019/8/16.
 */
class LoginOrgFragment : BaseFragment(), LoginContract.View, View.OnClickListener {

    private var isShow = false
    private val mPresenter by lazy { LoginPresenter() }

    private var mTitle: String? = null

    companion object {
        fun getInstance(title: String): LoginOrgFragment {
            val fragment = LoginOrgFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_login_org
    }

    override fun initView() {
        mPresenter.attachView(this)
//        initWhiteActionBar(-1, null, mTitle)
        tv_login.setOnClickListener(this)
        tv_register.setOnClickListener(this)
        tv_forget.setOnClickListener(this)
        tv_find.setOnClickListener(this)

    }

    override fun lazyLoad() {
    }

    override fun showLoading() {
        loadingDialog.show()
    }

    override fun dismissLoading() {
        loadingDialog.dismiss()
    }

    override fun doLoginResult(data: LoginInfoBean) {
        SPUtil.put(activity, "userId", data.id)
        if (data.completion != 1) {
            activity?.let { IntentUtils.to(it, PerfectOrgInfoActivity::class.java) }
        } else {
            ToastUtils.showShort(activity, "登录成功")
            SPUtil.put(activity, "isOrgLogin", true)
            SPUtil.put(activity, "isVolLogin", false)
            SPUtil.put(activity, "role", "org")
            SPUtil.put(activity, "username", et_tel.text.toString())
            SPUtil.put(activity, "password", et_password.text.toString())
            SPUtil.put(activity, "completion", data.completion)
            EventBusUtils.post(EventBusMessage(Const.LOGIN_ORG_SUCCESS, mTitle))
        }


    }

    override fun showError(errorMsg: String, errorCode: Int) {
        ToastUtils.showShort(activity, errorMsg)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_login -> {
                if (StringUtils.isEmpty(et_tel.text.toString())) {
                    ToastUtils.showShort(activity, getString(R.string.account_null))
                    return
                }
                if (StringUtils.isEmpty(et_password.text.toString())) {
                    ToastUtils.showShort(activity, getString(R.string.password_null))
                    return
                }
                if (SPUtil.get(activity, "isVolLogin", false) as Boolean) {
                    activity?.let {
                        SetSuccessDialog.Builder(it)
                                .setTitle(getString(R.string.tips))
                                .setMessage(getString(R.string.login_org))
                                .setPositiveButton(getString(R.string.cancel), DialogInterface.OnClickListener { p0, p1 ->
                                    p0.dismiss()
                                })
                                .setNegativeButton(getString(R.string.sure), DialogInterface.OnClickListener { p0, p1 ->
                                    p0.dismiss()
                                    mPresenter.doOrgLogin(et_tel.text.toString(), et_password.text.toString())
                                })
                                .setWith(0.77f)
                                .create()
                                .show()
                    }
                } else {
                    mPresenter.doOrgLogin(et_tel.text.toString(), et_password.text.toString())
                }
            }
            R.id.tv_register -> {
                var bundle = Bundle()
                bundle.putString("role", "org")
                activity?.let { IntentUtils.to(it, RegisterActivity::class.java, bundle) }
            }
            R.id.tv_forget -> {
                var bundle = Bundle()
                bundle.putBoolean("isEdit", false)
                bundle.putString("role", "org")
                activity?.let { IntentUtils.to(it, ForgetPasswordActivity::class.java, bundle) }
            }
            R.id.tv_find -> {
                activity?.let { IntentUtils.to(it, FindIdentityActivity::class.java) }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }


    override fun onReceiveEvent(event: EventBusMessage<Any>) {
        when (event.code) {
            Const.PERFECT_ORG_SUCCESS -> {
                if (!StringUtils.isEmpty(et_tel.text.toString()) && !StringUtils.isEmpty(et_password.text.toString())) {
                    mPresenter.doOrgLogin(et_tel.text.toString(), et_password.text.toString())
                }
            }
        }
    }
}