package com.wmsj.baselibs.ui.activity

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.maps.model.LatLng
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.MyActsContract
import com.wmsj.baselibs.mvp.presenter.MyActsPresenter
import com.wmsj.baselibs.base.BaseActivity
import com.wmsj.baselibs.bean.ActivityService
import com.wmsj.baselibs.bean.ClockCardBean
import com.wmsj.baselibs.bean.MyActivityBean
import com.wmsj.baselibs.utils.L
import com.wmsj.baselibs.utils.SPUtil
import com.wmsj.baselibs.utils.ToastUtils
import com.wmsj.baselibs.view.ClockCardDialog
import kotlinx.android.synthetic.main.activity_check_card.*

/**
 * 打卡
 */
class CheckCardActivity : BaseActivity(), MyActsContract.View {


    private val mPresenter by lazy { MyActsPresenter() }

    override fun layoutId(): Int {
        return R.layout.activity_check_card
    }

    override fun initData(savedInstanceState: Bundle?) {
        getLocation()
    }

    override fun initView() {
        mPresenter.attachView(this)
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, getString(R.string.check_in_card))
        ll_check_card.setOnClickListener {
            var map = HashMap<String, String>()
            map.put("uid", SPUtil.get(this, "userId", "").toString())
            map.put("aid", intent.getStringExtra("aid"))
            map.put("jwd", SPUtil.get(this, "jwd", "").toString())
            mPresenter.checkCard(map)
        }
        tv_location.setOnClickListener {
            getLocation()
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

    override fun setModelsData(data: List<MyActivityBean>) {
    }

    override fun activityServiceDetail(data: ActivityService) {
    }

    override fun checkCardResult(date: ClockCardBean) {
        ClockCardDialog.Builder(this)
                .setImageHeader(R.mipmap.card_success)
                .setTitle(date.card_time)
                .setPositiveButton(getString(R.string.sure), DialogInterface.OnClickListener { p0, p1 ->
                    p0.dismiss()
                })
                .create()
                .show()
    }

    override fun removeSignResult(date: String) {
    }

    override fun showError(msg: String, errorCode: Int) {
        when (errorCode) {
            100 -> {
                ClockCardDialog.Builder(this)
                        .setImageHeader(R.mipmap.card_fail)
                        .setTitle(msg)
                        .setPositiveButton(getString(R.string.sure), DialogInterface.OnClickListener { p0, p1 ->
                            p0.dismiss()
                        })
                        .create()
                        .show()

            }
            else -> {
                ToastUtils.showShort(this, msg)
            }
        }
    }


    fun getLocation() {
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
                val latLng = LatLng(aMapLocation.latitude, aMapLocation.longitude)
                tv_address.text = aMapLocation.city + aMapLocation.district + aMapLocation.poiName
                SPUtil.put(this, "jwd", latLng.toString())
            } else {
                L.d("定位失败，请重新定位" + aMapLocation.errorInfo + ";;" + aMapLocation.errorCode)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

}