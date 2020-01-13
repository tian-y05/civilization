package com.wmsj.baselibs.ui.activity

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.OrgUserInfoContract
import com.wmsj.baselibs.mvp.presenter.OrgUserInfoPresenter
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.base.BaseActivity
import com.wmsj.baselibs.bean.OrgUserInfo
import com.wmsj.baselibs.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_org_info.*

/**
 * 志愿者信息详情页
on 2019/9/4.
 */
class OrgUserInfoActivity : BaseActivity(), OrgUserInfoContract.View {

    private val mPresenter by lazy { OrgUserInfoPresenter() }
    private var id = ""

    override fun layoutId(): Int {
        return R.layout.activity_org_info
    }

    override fun initData(savedInstanceState: Bundle?) {
        id = intent.getStringExtra("id")
    }

    override fun initView() {
        mPresenter.attachView(this)
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, getString(R.string.org_user_info))
    }

    override fun start() {
        var map = HashMap<String, String>()
        map.put("id", id)
        mPresenter.orgUserInfo(map)
    }


    override fun showError(errorMsg: String, errorCode: Int) {
        ToastUtils.showShort(this, errorMsg)
    }

    override fun showLoading() {
        loadingDialog.show()
    }

    override fun dismissLoading() {
        loadingDialog.dismiss()
    }

    override fun userInfoResult(data: OrgUserInfo) {
        var requestOptions = RequestOptions()
        requestOptions.error(R.mipmap.author_default).placeholder(R.mipmap.author_default)
        Glide.with(this)
                .load(Const.BASE_URL + data.pic)
                .apply(requestOptions)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(iv_logo)
        tv_name.text = data.name
        rating_bar.setStar(data.org_count.toFloat())
        tv_org_num.text = "NO:" + data.zyzcode
        tv_tel.text = data.phone
        tv_service_all.text = data.all.servicetime.toString() + "h"
        tv_service_month.text = data.onemonth.servicetime.toString() + "h"
        tv_service_quarter.text = data.threeyear.servicetime.toString() + "h"
        tv_service_year.text = data.oneyear.servicetime.toString() + "h"

        tv_acts_all.text = data.all.join.toString()
        tv_acts_month.text = data.onemonth.join.toString()
        tv_acts_quarter.text = data.threeyear.join.toString()
        tv_acts_year.text = data.oneyear.join.toString()
        tv_sex.text = "性别：" + if (data.sex == 1) "男" else "女"
        tv_birth.text = "出生年月：" + data.birthday
        tv_place.text = "地址：" + data.area_list
        tv_zzmm.text = "政治面貌：" + data.political
        tv_whcd.text = "文化程度：" + data.edu
        tv_mz.text = "民族：" + data.ethnic
        tv_grjn.text = "个人技能：" + data.skills
        tv_zylb.text = "志愿类别：" + data.servicearea
        tv_fwsj.text = "服务时间：" + data.servicetime
        tv_fwdx.text = "服务对象：" + data.serviceobj

    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}