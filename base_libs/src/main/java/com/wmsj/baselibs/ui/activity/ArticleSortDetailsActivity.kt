package com.wmsj.baselibs.ui.activity

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.OrgDetailsContract
import com.wmsj.baselibs.mvp.presenter.OrgDeatilsPresenter
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.base.BaseActivity
import com.wmsj.baselibs.bean.HangupDetailBean
import com.wmsj.baselibs.bean.OrgBaseInfoBean
import com.wmsj.baselibs.bean.OrgDetailBean
import com.wmsj.baselibs.eventBus.EventBusMessage
import com.wmsj.baselibs.eventBus.EventBusUtils
import com.wmsj.baselibs.utils.IntentUtils
import com.wmsj.baselibs.utils.SPUtil
import com.wmsj.baselibs.utils.ToastUtils
import com.wmsj.baselibs.view.SetSuccessDialog
import kotlinx.android.synthetic.main.activity_sort_details.*

/**
 * 组织详情
 */
class ArticleSortDetailsActivity : BaseActivity(), OrgDetailsContract.View {


    private val TAG = ArticleSortDetailsActivity::class.java.simpleName
    private var id: String = ""
    private val mPresenter by lazy { OrgDeatilsPresenter() }

    override fun layoutId(): Int {
        return R.layout.activity_sort_details
    }

    override fun initData(savedInstanceState: Bundle?) {
        id = intent.getStringExtra("id")
        mPresenter.attachView(this)
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, getString(R.string.organize_detail))

    }

    override fun initView() {
    }

    override fun start() {
        intent.getStringExtra("type")?.let {
            if (it == "myorg") {
                mPresenter.getMyOrgDetails(id)
            } else {
                mPresenter.getOrgDetails(id)
            }
        }

    }


    override fun showLoading() {
        loadingDialog.show()
    }

    override fun dismissLoading() {
        loadingDialog.dismiss()
    }

    override fun setOrgDetails(data: OrgDetailBean) {
        data?.let {
            var requestOptions = RequestOptions()
            requestOptions.error(R.mipmap.zhi_default).placeholder(R.mipmap.zhi_default)
            Glide.with(this)
                    .load(Const.BASE_URL + data.logo)
                    .apply(requestOptions)
                    .into(iv_logo)
            tv_name.text = data.org_cname
            tv_area.text = "地址:" + data.zgarea
            tv_service_duration.text = data.time
            tv_member_num.text = data.membernum
            tv_create_time.text = data.inserttime
            tv_leader.text = data.duty_name
            tv_contacts_phone.text = data.account
            tv_organize_activity.text = data.allactivitynum
            tv_desc.text = "\u3000\u3000" + data.desc
            if (data.allactivitynum == "0") {
                ll_organize_activity.visibility = View.INVISIBLE
            } else {
                ll_organize_activity.visibility = View.VISIBLE
            }

            ll_organize_activity.setOnClickListener {
                var bundle = Bundle()
                bundle.putString("stationId", data.id)
                bundle.putString("type", Const.POLYMERIZE)
                IntentUtils.to(this, StationSpotActivity::class.java, bundle)
            }
        }
    }

    override fun setOrgInfoDetails(data: OrgBaseInfoBean) {
        data?.let {
            var requestOptions = RequestOptions()
            requestOptions.error(R.mipmap.zhi_default).placeholder(R.mipmap.zhi_default)
            Glide.with(this)
                    .load(Const.BASE_URL + data.logo)
                    .apply(requestOptions)
                    .into(iv_logo)
            tv_name.text = data.org_cname
            tv_area.text = "地址:" + data.zgarea
            tv_service_duration.text = data.time
            tv_member_num.text = data.membernum
            tv_create_time.text = data.inserttime
            tv_leader.text = data.duty_name
            tv_contacts_phone.text = data.account
            tv_organize_activity.text = data.allactivitynum
            tv_desc.text = "\u3000\u3000" + data.desc
            if (data.allactivitynum == "0") {
                ll_organize_activity.visibility = View.INVISIBLE
            } else {
                ll_organize_activity.visibility = View.VISIBLE
            }
            ll_organize_activity.setOnClickListener {
                var bundle = Bundle()
                bundle.putString("stationId", data.id)
                bundle.putString("type", Const.POLYMERIZE)
                IntentUtils.to(this, StationSpotActivity::class.java, bundle)
            }
            tv_join_organize.setOnClickListener {
                if (SPUtil.get(this, "isVolLogin", false) as Boolean || SPUtil.get(this, "isOrgLogin", false) as Boolean) {
                    mPresenter.joinOrg(data.id)
                } else {
                    SetSuccessDialog.Builder(this)
                            .setTitle(getString(R.string.tips))
                            .setMessage(getString(R.string.sure_to_login))
                            .setPositiveButton(getString(R.string.cancel), DialogInterface.OnClickListener { p0, p1 ->
                                p0.dismiss()
                            })
                            .setNegativeButton(getString(R.string.sure), DialogInterface.OnClickListener { p0, p1 ->
                                p0.dismiss()
                                EventBusUtils.post(EventBusMessage(Const.GO_LOGIN_VOL, ""))
                                finish()
                            })
                            .setWith(0.77f)
                            .create()
                            .show()
                }

            }

            tv_apply_for_registration.setOnClickListener {
                if (SPUtil.get(this, "isOrgLogin", false) as Boolean) {
                    SetSuccessDialog.Builder(this)
                            .setTitle(getString(R.string.tips))
                            .setMessage(getString(R.string.sure_to_applay))
                            .setPositiveButton(getString(R.string.cancel), DialogInterface.OnClickListener { p0, p1 ->
                                p0.dismiss()
                            })
                            .setNegativeButton(getString(R.string.sure), DialogInterface.OnClickListener { p0, p1 ->
                                p0.dismiss()
                                mPresenter.applyOrg("1", data.id)
                            })
                            .setWith(0.77f)
                            .create()
                            .show()

                } else {
                    SetSuccessDialog.Builder(this)
                            .setTitle(getString(R.string.tips))
                            .setMessage(getString(R.string.sure_to_login))
                            .setPositiveButton(getString(R.string.cancel), DialogInterface.OnClickListener { p0, p1 ->
                                p0.dismiss()
                            })
                            .setNegativeButton(getString(R.string.sure), DialogInterface.OnClickListener { p0, p1 ->
                                p0.dismiss()
                                EventBusUtils.post(EventBusMessage(Const.GO_LOGIN_ORG, ""))
                                finish()
                            })
                            .setWith(0.77f)
                            .create()
                            .show()
                }

            }

            if (SPUtil.get(this, "isVolLogin", false) as Boolean || SPUtil.get(this, "isOrgLogin", false) as Boolean) {
                when (data.if_join) {
                    -1, 2 -> {
                        if (SPUtil.get(this, "role", "").toString() == "org") {
                            tv_join_organize.visibility = View.GONE
                            ll_bottom.visibility = View.VISIBLE
                            tv_apply_for_registration.visibility = View.VISIBLE
                            tv_join_wait.visibility = View.GONE
                        } else {
                            ll_bottom.visibility = View.VISIBLE
                            tv_join_organize.visibility = View.VISIBLE
                            tv_apply_for_registration.visibility = View.GONE
                            tv_join_wait.visibility = View.GONE
                        }
                    }
                    0 -> {
                        if (intent.getStringExtra("type") == "myorg") {
                            ll_bottom.visibility = View.GONE
                            tv_join_wait.visibility = View.VISIBLE
                            tv_join_wait.text = getString(R.string.apply_exit_org)
                            tv_join_wait.setOnClickListener {
                                mPresenter.exitOrg(data.id)
                            }
                        } else {
                            ll_bottom.visibility = View.GONE
                            tv_join_wait.visibility = View.VISIBLE
                        }

                    }
                    1 -> {
                        if (intent.getStringExtra("type") == "myorg") {
                            ll_bottom.visibility = View.GONE
                            tv_join_wait.visibility = View.VISIBLE
                            tv_join_wait.text = getString(R.string.exit_org)
                            tv_join_wait.setOnClickListener {
                                mPresenter.exitOrg(data.id)
                            }
                        } else {
                            ll_bottom.visibility = View.GONE
                            tv_join_wait.visibility = View.GONE
                        }

                    }
                }
            } else {
                ll_bottom.visibility = View.VISIBLE
                tv_join_wait.visibility = View.GONE
            }

        }
    }

    override fun showError(errorMsg: String, errorCode: Int) {
        ToastUtils.showShort(this, errorMsg)
    }

    override fun joinOrgResult(data: String) {
        ToastUtils.showShort(this, data)
        ll_bottom.visibility = View.GONE
        tv_join_wait.visibility = View.VISIBLE
    }

    override fun exitOrgResult(data: String) {
        ToastUtils.showShort(this, data)
        EventBusUtils.post(EventBusMessage(Const.EXIT_ORG, intent.getIntExtra("state", -1)))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun orgManageDetail(data: HangupDetailBean) {
    }

    override fun operationResult(data: String) {
    }

}