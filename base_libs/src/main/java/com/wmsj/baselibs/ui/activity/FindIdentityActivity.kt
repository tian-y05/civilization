package com.wmsj.baselibs.ui.activity

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.FindIdentityContract
import com.wmsj.baselibs.mvp.presenter.FindIentityPresenter
import com.wmsj.baselibs.retrofit.BaseResponse
import com.wmsj.baselibs.ui.adapter.FindIdentityAdapter
import com.wmsj.baselibs.base.BaseActivity
import com.wmsj.baselibs.bean.IdentityInfoBean
import com.wmsj.baselibs.recyclerview.adapter.OnItemClickListener
import com.wmsj.baselibs.scheduler.SchedulerUtils
import com.wmsj.baselibs.utils.*
import com.wmsj.baselibs.view.CommonDialog
import com.wmsj.baselibs.view.SetSuccessDialog
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_find_identity.*
import java.util.concurrent.TimeUnit


/**
 * 找回身份
on 2019/8/17.
 */
class FindIdentityActivity : BaseActivity(), FindIdentityContract.View, View.OnClickListener {


    private var identityList = ArrayList<IdentityInfoBean>()
    private val mPresenter by lazy { FindIentityPresenter() }
    private var curPosition = -1
    private var curTel = ""
    private var curCardNumber = ""
    private var uid = ""
    private val mFindIdentityAdapter by lazy { FindIdentityAdapter(this, identityList, R.layout.activity_identity_item) }
    private var nodeCurrent = 0
    val count = 60//倒计时60秒
    override fun layoutId(): Int {
        return R.layout.activity_find_identity
    }

    override fun initData(savedInstanceState: Bundle?) {
        mPresenter.attachView(this)
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, getString(R.string.find_password))
    }

    override fun initView() {
        node_view.setCurentNode(nodeCurrent)
        tv_next.setOnClickListener(this)
        tv_obtain_code.setOnClickListener(this)
        tv_register.setOnClickListener(this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mFindIdentityAdapter
        mFindIdentityAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(obj: Any?, position: Int) {
                mFindIdentityAdapter.setCurrentPos(position)
                curPosition = position
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_next -> {
                when (nodeCurrent) {
                    0 -> {
                        if (StringUtils.isEmpty(et_phone.text.toString())) {
                            ToastUtils.showShort(this, getString(R.string.phone_null))
                        } else if (StringUtils.isEmpty(et_code.text.toString())) {
                            ToastUtils.showShort(this, getString(R.string.code_null))
                        } else {
                            var map = HashMap<String, String>()
                            map.put("mobile", et_phone.text.toString())
                            map.put("yzm", et_code.text.toString())
                            mPresenter.verificationPhone(map)
                        }

                    }
                    1 -> {
                        if (StringUtils.isEmpty(et_card.text.toString())) {
                            ToastUtils.showShort(this, getString(R.string.card_null))
                        } else if (StringUtils.isEmpty(et_name.text.toString())) {
                            ToastUtils.showShort(this, getString(R.string.name_null))
                        } else {
                            if (IDCardValidate(et_card.text.toString()).isCorrect == 0) {
                                var map = HashMap<String, String>()
                                map.put("code", et_card.text.toString())
                                map.put("name", et_name.text.toString())
                                map.put("page", "1")
                                map.put("name", "8")
                                mPresenter.machIdentityInfo(map)
                            } else {
                                ToastUtils.showShort(this, getString(R.string.card_true))
                            }

                        }

                    }
                    2 -> {
                        if (curPosition == -1) {
                            ToastUtils.showShort(this, getString(R.string.choose_account))
                            return
                        }
                        CommonDialog.Builder(this).
                                setImageHeader(R.mipmap.find_identity_dialog)
                                .setTitle(getString(R.string.delete_info))
                                .setMessage(getString(R.string.please_check))
                                .setPositiveButton(getString(R.string.wait), DialogInterface.OnClickListener { p0, p1 ->
                                    p0?.dismiss()

                                })
                                .setNegativeButton(getString(R.string.sure_to_set), DialogInterface.OnClickListener { p0, p1 ->
                                    p0?.dismiss()
                                    var map = HashMap<String, String>()
                                    map.put("mobile", curTel)
                                    map.put("code", curCardNumber)
                                    map.put("id", identityList[curPosition].id)
                                    mPresenter.bindIdentity(map)
                                })
                                .setWith(0.77f)
                                .create()
                                .show()
                    }
                    3 -> {
                        if (StringUtils.isEmpty(et_password.text.toString())) {
                            ToastUtils.showShort(this, getString(R.string.password_null))
                        } else if (StringUtils.isEmpty(et_password_again.text.toString())) {
                            ToastUtils.showShort(this, getString(R.string.password_null_again))
                        } else if (et_password.text.toString() != et_password_again.text.toString()) {
                            ToastUtils.showShort(this, getString(R.string.password_no_same))
                        } else if (et_password.text.toString().length < 6) {
                            ToastUtils.showShort(this, getString(R.string.password_length))
                        } else if (!PwdCheckUtil.isContainAll(et_password.text.toString())) {
                            ToastUtils.showShort(this, getString(R.string.password_no_number))
                        } else if (!PwdCheckUtil.isContainAll(et_password_again.text.toString())) {
                            ToastUtils.showShort(this, getString(R.string.password_no_number))
                        } else {
                            var map = HashMap<String, String>()
                            map.put("uid", uid)
                            map.put("pwd", et_password.text.toString())
                            mPresenter.resetPwd(map)
                        }

                    }
                }
            }
            R.id.tv_obtain_code -> {
                if (StringUtils.isEmpty(et_phone.text.toString())) {
                    ToastUtils.showShort(this, getString(R.string.phone_null))
                } else {
                    mPresenter.getObtainCode(et_phone.text.toString())
                }
            }
            R.id.tv_register -> {
                SetSuccessDialog.Builder(this)
                        .setTitle(getString(R.string.tips))
                        .setMessage(getString(R.string.delete_message))
                        .setPositiveButton(getString(R.string.cancel), DialogInterface.OnClickListener { p0, p1 ->
                            p0.dismiss()
                        })
                        .setNegativeButton(getString(R.string.sure), DialogInterface.OnClickListener { p0, p1 ->
                            p0.dismiss()
                            mPresenter.deleteMachInfo(curCardNumber)
                        })
                        .setWith(0.77f)
                        .create()
                        .show()

            }
        }
    }

    private fun switchLayout(nodeCurrent: Int) {
        node_view.setCurentNode(nodeCurrent)
        node_view.reDraw()
        when (nodeCurrent) {
            0 -> {
                ll_code_one.visibility = View.VISIBLE
                ll_code_two.visibility = View.GONE
                ll_code_three.visibility = View.GONE
                ll_code_four.visibility = View.GONE
                tv_register.visibility = View.GONE
            }
            1 -> {
                ll_code_one.visibility = View.GONE
                ll_code_two.visibility = View.VISIBLE
                ll_code_three.visibility = View.GONE
                ll_code_four.visibility = View.GONE
                tv_register.visibility = View.GONE
            }
            2 -> {
                ll_code_one.visibility = View.GONE
                ll_code_two.visibility = View.GONE
                ll_code_three.visibility = View.VISIBLE
                ll_code_four.visibility = View.GONE
                tv_register.visibility = View.VISIBLE
            }
            3 -> {
                ll_code_one.visibility = View.GONE
                ll_code_two.visibility = View.GONE
                ll_code_three.visibility = View.GONE
                ll_code_four.visibility = View.VISIBLE
                tv_register.visibility = View.GONE
            }
        }
    }

    override fun start() {
    }


    override fun showLoading() {
        loadingDialog.show()
    }

    override fun dismissLoading() {
        loadingDialog.dismiss()
    }

    override fun verificationResult(data: String) {
        curTel = et_phone.text.toString()
        ToastUtils.showShort(this, data)
        nodeCurrent = 1
        switchLayout(nodeCurrent)
    }

    override fun bindingInfoResult(data: String) {
        uid = data
        nodeCurrent = 3
        switchLayout(nodeCurrent)
    }

    override fun deleteInfoResult(data: String) {
        ToastUtils.showShort(this, data)
        finish()
        var bundle = Bundle()
        bundle.putString("role", "zyz")
        IntentUtils.to(this, RegisterActivity::class.java, bundle)
    }

    override fun resetResult(data: String) {
        SetSuccessDialog.Builder(this)
                .setImageHeader(R.mipmap.success)
                .setMessage(getString(R.string.pwd_success))
                .setPositiveButton(getString(R.string.perfect_information), DialogInterface.OnClickListener { p0, p1 ->
                    p0?.dismiss()
                    finish()
                    SPUtil.put(this, "userId", data)
                    IntentUtils.to(this, PerfectInformationActivity::class.java)
                })
                .setWith(0.77f)
                .create()
                .show()
    }

    override fun machInfoResult(data: BaseResponse<List<IdentityInfoBean>>) {
        curCardNumber = et_card.text.toString()
        if (data.state == "0") {
            CommonDialog.Builder(this).
                    setImageHeader(R.mipmap.find_identity_dialog)
                    .setTitle(getString(R.string.no_find))
                    .setMessage(getString(R.string.reregister_or_sure))
                    .setPositiveButton(getString(R.string.back_check), DialogInterface.OnClickListener { p0, p1 ->
                        p0?.dismiss()

                    })
                    .setNegativeButton(getString(R.string.to_register), DialogInterface.OnClickListener { p0, p1 ->
                        finish()
                        var bundle = Bundle()
                        bundle.putString("role", "zyz")
                        IntentUtils.to(this, RegisterActivity::class.java, bundle)

                    })
                    .setWith(0.77f)
                    .create()
                    .show()
        } else {
            CommonDialog.Builder(this).
                    setImageHeader(R.mipmap.find_identity_dialog)
                    .setTitle(getString(R.string.find_success))
                    .setMessage(getString(R.string.claim))
                    .setPositiveButton(getString(R.string.back_check), DialogInterface.OnClickListener { p0, p1 ->
                        p0?.dismiss()

                    })
                    .setNegativeButton(getString(R.string.claim_account), DialogInterface.OnClickListener { p0, p1 ->
                        p0?.dismiss()
                        nodeCurrent = 2
                        switchLayout(nodeCurrent)
                        identityList.addAll(data.data)
                        mFindIdentityAdapter.notifyDataSetChanged()
                    })
                    .setWith(0.77f)
                    .create()
                    .show()
        }
    }

    override fun showError(errorMsg: String, errorCode: Int) {
        ToastUtils.showShort(this, errorMsg)
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

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}