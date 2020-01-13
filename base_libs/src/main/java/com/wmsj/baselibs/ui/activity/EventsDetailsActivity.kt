package com.wmsj.baselibs.ui.activity

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Poi
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.EventDetailsContract
import com.wmsj.baselibs.mvp.presenter.EventDeatilsPresenter
import com.wmsj.baselibs.weight.NaviDialog
import com.wmsj.baselibs.base.BaseActivity
import com.wmsj.baselibs.bean.ActivityShowBean
import com.wmsj.baselibs.eventBus.EventBusMessage
import com.wmsj.baselibs.eventBus.EventBusUtils
import com.wmsj.baselibs.utils.*
import com.wmsj.baselibs.view.SetSuccessDialog
import kotlinx.android.synthetic.main.activity_events_details.*


/**
 * 活动详情
 */
class EventsDetailsActivity : BaseActivity(), EventDetailsContract.View {


    private val TAG = EventsDetailsActivity::class.java.simpleName
    private var id: String = ""
    private val mPresenter by lazy { EventDeatilsPresenter() }

    override fun layoutId(): Int {
        return R.layout.activity_events_details
    }

    override fun initData(savedInstanceState: Bundle?) {
        id = intent.getStringExtra("id")
        mPresenter.attachView(this)
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, getString(R.string.activity_detail))
        var auth = SPUtil.get(this, "auth", "")

        if ((auth as String).contains(Const.POLYMERIZE)) {
            ll_activity_report.visibility = View.VISIBLE
        } else {
            ll_activity_report.visibility = View.GONE
        }
    }

    override fun initView() {
    }

    override fun start() {
        mPresenter.getEventDetails(id)
    }

    fun getLocation(pace: String, jwd: Array<String>) {
        var mEndPoi = Poi(pace,
                LatLng(jwd[0].toDouble(), jwd[1].toDouble()), "")

        val mLocationClient = AMapLocationClient(this)
        val mLocationOption = AMapLocationClientOption()
        //高精度模式
        mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        //定位请求超时时间
        mLocationOption.httpTimeOut = 50000
        // 关闭缓存机制
        mLocationOption.isLocationCacheEnable = false
        // 设置是否只定位一次
        mLocationOption.isOnceLocation = true
        //设置参数
        mLocationClient.setLocationOption(mLocationOption)
        // 启动定位
        mLocationClient.startLocation()
        //定位监听
        mLocationClient.setLocationListener { aMapLocation ->
            //定位成功之后取消定位
            mLocationClient.stopLocation()
            if (aMapLocation != null && aMapLocation.errorCode == 0) {
                L.d("地址成功===" + aMapLocation.toStr())
                var mStartPoi = Poi(aMapLocation.city + aMapLocation.district + aMapLocation.poiName,
                        LatLng(aMapLocation.latitude, aMapLocation.longitude), "")
                NaviDialog().showView(this, mStartPoi, mEndPoi, 102)
            } else {
                L.d("定位失败，请重新定位" + aMapLocation.errorInfo + ";;" + aMapLocation.errorCode)
            }
        }
    }

    override fun showLoading() {
        loadingDialog.show()
    }

    override fun dismissLoading() {
        loadingDialog.dismiss()
    }

    override fun setEventDetails(data: ActivityShowBean) {
        data?.let {
            var requestOptions = RequestOptions()
            requestOptions.error(R.mipmap.news_normal)
                    .placeholder(R.mipmap.news_normal)
                    .transforms(RoundedCorners(30))
            Glide.with(this).load(Const.BASE_URL + data.activity_logo).apply(requestOptions).into(iv_content)
            if (SPUtil.get(this, "role", "").toString() == "org") {
                tv_sign.visibility = View.INVISIBLE
            }
            when (data.status) {
                1 -> {
                    number_of_applicants.text = getString(R.string.number_of_sign)
                    tv_number_of_applicants.text = data.sign_num.toString() + "/" + data.recruitment
                    iv_state.setImageResource(R.mipmap.recruit_before)
                    tv_sign.text = getString(R.string.activity_before)
                    tv_sign.background = resources.getDrawable(R.drawable.shape_corner_gray)
                }
                2 -> {
                    when (data.sign_up) {
                        1 -> {
                            tv_sign.text = data.bottominfo
                            tv_sign.background = resources.getDrawable(R.drawable.shape_corner_gray)
                        }
                        2 -> {
                            tv_sign.text = getString(R.string.sign_up)
                            tv_sign.background = resources.getDrawable(R.drawable.shape_corner_green)
                            tv_sign.setOnClickListener {
                                L.d("立即报名")
                                if (SPUtil.get(this, "isVolLogin", false) as Boolean) {
                                    mPresenter.joinActivity(id)
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
                        }
                    }
                    number_of_applicants.text = getString(R.string.number_of_sign)
                    tv_number_of_applicants.text = data.sign_num.toString() + "/" + data.recruitment
                    iv_state.setImageResource(R.mipmap.recruit_ing)

                }
                4 -> {
                    number_of_applicants.text = getString(R.string.number_of_applicants)
                    tv_number_of_applicants.text = data.in_num + "/" + data.recruitment
                    iv_state.setImageResource(R.mipmap.activity_going)
                    tv_sign.text = getString(R.string.activity_going)
                    tv_sign.background = resources.getDrawable(R.drawable.shape_corner_gray)
                }
                5 -> {
                    number_of_applicants.text = getString(R.string.number_of_applicants)
                    tv_number_of_applicants.text = data.in_num + "/" + data.recruitment
                    iv_state.setImageResource(R.mipmap.activity_over)
                    tv_sign.text = getString(R.string.activity_over)
                    tv_sign.background = resources.getDrawable(R.drawable.shape_corner_gray)
                }
                else -> {
                }
            }
            tv_title.text = data.activity_name
            tv_recruit_time.text = data.recruit_starttime + "至" + data.recruit_endtime
            tv_activity_time.text = data.start_time + "至" + data.end_time
            tv_activity_place.text = data.activity_pace
            tv_service_time.text = data.service_time + "小时"
            tv_service_type.text = data.activity_type
            tv_service_object.text = data.activity_object
            tv_initiating_organization.text = data.org_name
            tv_contacts_people.text = data.activity_contacts
            tv_contacts_phone.text = data.tel
            tv_content.text = "\u3000\u3000" + data.activity_introduce
            tv_activity_pic.text = data.img_count.toString()
            tv_activity_report.text = data.report_num
            ll_location.setOnClickListener {
                var jwd = StringUtils.split(data.jwd, ",")
                getLocation(data.activity_pace, jwd)

            }

            ll_photos.setOnClickListener {
                if(data.activity_images != null && data.activity_images.isNotEmpty()){
                    var bundle = Bundle()
                    bundle.putStringArray("photos", data.activity_images.toTypedArray())
                    IntentUtils.to(this, PhotoShowActivity::class.java, bundle)
                }

            }
            ll_activity_report.setOnClickListener {
                if(data.report_num.toInt() > 0){
                    var bundle = Bundle()
                    bundle.putString("oid", data.report_id)
                    bundle.putString("type", Const.ACT_REPORT)
                    IntentUtils.to(this, WebViewDetailActivity::class.java,bundle)
                }
            }
        }

    }

    override fun showError(errorMsg: String, errorCode: Int) {
        ToastUtils.showShort(this, errorMsg)
    }

    override fun joinResult(data: String) {
        tv_sign.text = data
        tv_sign.background = resources.getDrawable(R.drawable.shape_corner_gray)
        tv_sign.setOnClickListener(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}