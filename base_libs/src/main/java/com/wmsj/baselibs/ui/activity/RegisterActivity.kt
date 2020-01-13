package com.wmsj.baselibs.ui.activity

import android.os.Bundle
import android.view.View
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.RegisterContract
import com.wmsj.baselibs.mvp.presenter.RegisterPresenter
import com.wmsj.baselibs.base.BaseActivity
import com.wmsj.baselibs.bean.RegistBean
import com.wmsj.baselibs.scheduler.SchedulerUtils
import com.wmsj.baselibs.utils.*
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_register.*
import java.util.concurrent.TimeUnit


/**
 * 注册
on 2019/8/1.
 */
class RegisterActivity : BaseActivity(), View.OnClickListener, RegisterContract.View {

    private var role = ""
    private val mPresenter by lazy { RegisterPresenter() }
    val count = 60//倒计时60秒
    override fun layoutId(): Int {
        return R.layout.activity_register
    }

    override fun initData(savedInstanceState: Bundle?) {
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, getString(R.string.register))
        role = intent.getStringExtra("role")
        if (role == "zyz") {
            tv_next_perfecting.text = getString(R.string.perfecting_vol_information)
        } else {
            tv_next_perfecting.text = getString(R.string.perfecting_organizational_information)
        }
    }

    override fun initView() {
        mPresenter.attachView(this)
        tv_obtain_code.setOnClickListener(this)
        tv_next_perfecting.setOnClickListener(this)
    }

    override fun start() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_obtain_code -> {
                if (StringUtils.isEmpty(et_phone.text.toString())) {
                    ToastUtils.showShort(this, getString(R.string.phone_null))
                } else {
                    mPresenter.getObtainCode(et_phone.text.toString(), role)
                }
            }
            R.id.tv_next_perfecting -> {
                if (StringUtils.isEmpty(et_phone.text.toString())) {
                    ToastUtils.showShort(this, getString(R.string.phone_null))
                } else if (StringUtils.isEmpty(et_code.text.toString())) {
                    ToastUtils.showShort(this, getString(R.string.code_null))
                } else if (StringUtils.isEmpty(et_password.text.toString())) {
                    ToastUtils.showShort(this, getString(R.string.password_null))
                } else if (StringUtils.isEmpty(et_password_again.text.toString())) {
                    ToastUtils.showShort(this, getString(R.string.password_null_again))
                }else if (et_password_again.text.toString() != et_password.text.toString()) {
                    ToastUtils.showShort(this, getString(R.string.password_no_same))
                }else if (et_password.text.toString().length < 6) {
                    ToastUtils.showShort(this, getString(R.string.password_length))
                }else if(!PwdCheckUtil.isContainAll(et_password.text.toString())){
                    ToastUtils.showShort(this, getString(R.string.password_no_number))
                }else if(!PwdCheckUtil.isContainAll(et_password_again.text.toString())){
                    ToastUtils.showShort(this, getString(R.string.password_no_number))
                }  else {
                    val map = HashMap<String, String>()
                    if (role == "zyz") {
                        map.put("phone", et_phone.text.toString())
                        map.put("yzm", et_code.text.toString())
                        map.put("password", et_password.text.toString())
                        mPresenter.doRegist(map)
                    } else {
                        map.put("account", et_phone.text.toString())
                        map.put("yzm", et_code.text.toString())
                        map.put("pwd", et_password.text.toString())
                        mPresenter.doOrgRegist(map)
                    }

                }
            }
        }
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
        SPUtil.put(this, "userId", data.userid)
        finish()
        IntentUtils.to(this, PerfectInformationActivity::class.java)
    }

    override fun doForgetpwd(data: String) {

    }

    override fun showError(errorMsg: String, errorCode: Int) {
        ToastUtils.showShort(this, errorMsg)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }


    override fun doEditResult(data: String) {
    }

    override fun doOrgRegistResult(data: String) {
        finish()
        SPUtil.put(this, "userId", data)
        IntentUtils.to(this, PerfectOrgInfoActivity::class.java)
    }
}