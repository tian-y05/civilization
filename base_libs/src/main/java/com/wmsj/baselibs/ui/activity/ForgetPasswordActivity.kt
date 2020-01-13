package com.wmsj.baselibs.ui.activity

import android.os.Bundle
import android.view.View
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.RegisterContract
import com.wmsj.baselibs.mvp.presenter.RegisterPresenter
import com.wmsj.baselibs.base.BaseActivity
import com.wmsj.baselibs.bean.RegistBean
import com.wmsj.baselibs.eventBus.EventBusMessage
import com.wmsj.baselibs.eventBus.EventBusUtils
import com.wmsj.baselibs.scheduler.SchedulerUtils
import com.wmsj.baselibs.utils.PwdCheckUtil
import com.wmsj.baselibs.utils.StringUtils
import com.wmsj.baselibs.utils.ToastUtils
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_forget_password.*
import java.util.concurrent.TimeUnit

/**
 * 修改手机号、忘记密码
on 2019/8/1.
 */
class ForgetPasswordActivity : BaseActivity(), RegisterContract.View, View.OnClickListener {

    val count = 60//倒计时60秒
    private var isEdit = false
    private var role = ""
    private val mPresenter by lazy { RegisterPresenter() }

    override fun layoutId(): Int {
        return R.layout.activity_forget_password
    }

    override fun initData(savedInstanceState: Bundle?) {
        isEdit = intent.getBooleanExtra("isEdit", false)
        role = intent.getStringExtra("role")
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, if (isEdit) getString(R.string.change_tel) else getString(R.string.password_forget))

    }

    override fun initView() {
        mPresenter.attachView(this)
        tv_obtain_code.setOnClickListener(this)
        tv_post.setOnClickListener(this)
    }

    override fun start() {

    }

    override fun showLoading() {
        loadingDialog.show()
    }

    override fun dismissLoading() {
        loadingDialog.dismiss()
    }

    override fun obtainCodeResult(data: String) {
        ToastUtils.showShort(this, data)
        //60s倒计时
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take((count + 1).toLong())
                .map { t -> count - t }
                .compose(SchedulerUtils.ioToMain())
                .doOnSubscribe { }
                .subscribe(object : Observer<Long> {
                    override fun onNext(t: Long) {
                        tv_obtain_code.text = t.toString() + "s"
                        tv_obtain_code.isClickable = false
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        //回复原来初始状态
                        tv_obtain_code.text = getString(R.string.obtain_code)
                        tv_obtain_code.isClickable = true
                    }

                    override fun onComplete() {
                        //回复原来初始状态
                        tv_obtain_code.text = getString(R.string.obtain_code)
                        tv_obtain_code.isClickable = true
                    }
                })
    }

    override fun doRegistResult(data: RegistBean) {
    }

    override fun doForgetpwd(data: String) {
        ToastUtils.showShort(this, data)
        EventBusUtils.post(EventBusMessage(Const.VOL_EDIT_PWD, role))
        finish()
    }

    override fun showError(errorMsg: String, errorCode: Int) {
        ToastUtils.showShort(this, errorMsg)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_obtain_code -> {
                if (StringUtils.isEmpty(et_phone.text.toString())) {
                    ToastUtils.showShort(this, getString(R.string.phone_null))
                } else {
                    mPresenter.getObtainCode(et_phone.text.toString(),role)
                }
            }
            R.id.tv_post -> {
                if (StringUtils.isEmpty(et_phone.text.toString())) {
                    ToastUtils.showShort(this, getString(R.string.phone_null))
                } else if (StringUtils.isEmpty(et_code.text.toString())) {
                    ToastUtils.showShort(this, getString(R.string.code_null))
                } else if (StringUtils.isEmpty(et_password.text.toString())) {
                    ToastUtils.showShort(this, getString(R.string.password_null))
                } else if (StringUtils.isEmpty(et_password_again.text.toString())) {
                    ToastUtils.showShort(this, getString(R.string.password_null_again))
                } else if (et_password.text.toString() != et_password_again.text.toString()) {
                    ToastUtils.showShort(this, getString(R.string.password_no_same))
                }else if (et_password.text.toString().length < 6) {
                    ToastUtils.showShort(this, getString(R.string.password_length))
                }else if(!PwdCheckUtil.isContainAll(et_password.text.toString())){
                    ToastUtils.showShort(this, getString(R.string.password_no_number))
                }else if(!PwdCheckUtil.isContainAll(et_password_again.text.toString())){
                    ToastUtils.showShort(this, getString(R.string.password_no_number))
                } else {
                    val map = HashMap<String, String>()
                    map.put("user", et_phone.text.toString())
                    map.put("vcode", et_code.text.toString())
                    map.put("pwd", et_password.text.toString())
                    mPresenter.doForgetpwd(map,role)
                }
            }
        }
    }

    override fun doEditResult(data: String) {
    }

    override fun doOrgRegistResult(data: String) {
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}