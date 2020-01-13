package com.wmsj.baselibs.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.amap.api.maps.model.animation.AlphaAnimation
import com.amap.api.maps.model.animation.Animation
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.wmsj.baselibs.R
import com.wmsj.baselibs.ui.adapter.LocationMapAdapter
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.base.BaseActivity
import com.wmsj.baselibs.eventBus.EventBusMessage
import com.wmsj.baselibs.eventBus.EventBusUtils
import com.wmsj.baselibs.recyclerview.adapter.OnItemClickListener
import com.wmsj.baselibs.utils.IntentUtils
import com.wmsj.baselibs.utils.L
import com.wmsj.baselibs.view.RecycleViewDivider
import com.xbrc.myapplication.bean.PoiLocationItem
import kotlinx.android.synthetic.main.activity_location_map.*


/**
 * 高德地图地位
on 2019/8/14.
 */
class LocationMapActivity : BaseActivity(), GeocodeSearch.OnGeocodeSearchListener {


    private var mList = ArrayList<PoiLocationItem>()
    private val mLocationMapAdapter by lazy { LocationMapAdapter(this, mList, R.layout.activity_location_item) }
    private lateinit var aMap: AMap
    //判断是来自地图还是列表的点击
    private var isTouch = false //false 不进行附件搜索
    //地图中心点
    private lateinit var centerMaker: Marker
    private lateinit var geocodeSearch: GeocodeSearch
    private lateinit var latLng: LatLng
    private var currentPage = 0
    private var city = ""

    override fun layoutId(): Int {
        return R.layout.activity_location_map
    }


    override fun initData(savedInstanceState: Bundle?) {

        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, getString(R.string.publish))
        map_view.onCreate(savedInstanceState)
        aMap = map_view.map
        var uiSettings = aMap.uiSettings
        uiSettings.isZoomControlsEnabled = false
        uiSettings.isMyLocationButtonEnabled = true
        uiSettings.isScaleControlsEnabled = true
        aMap.isMyLocationEnabled = false

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mLocationMapAdapter
        recyclerView.addItemDecoration(RecycleViewDivider(
                this, LinearLayoutManager.VERTICAL, 2, resources.getColor(R.color.under_line)))
    }

    override fun initView() {
        //设置地图拖动监听
        aMap.setOnMapTouchListener {
            isTouch = true
        }

        geocodeSearch = GeocodeSearch(this)
        geocodeSearch.setOnGeocodeSearchListener(this)

        aMap.setOnCameraChangeListener(object : AMap.OnCameraChangeListener {
            override fun onCameraChangeFinish(cameraPosition: CameraPosition) {
                if (isTouch) {
                    latLng = cameraPosition.target
                    //查找附近数据
                    // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
                    val query = RegeocodeQuery(LatLonPoint(latLng.latitude, latLng.longitude), 200f, GeocodeSearch.GPS)
                    geocodeSearch.getFromLocationAsyn(query)

                }
            }

            override fun onCameraChange(p0: CameraPosition?) {
            }

        })
        //获取当前位置
        getCurrentLocation { locationSuccess(it) }


        //.provinceName + mList[position].cityName + mList[position].address)
        mLocationMapAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(obj: Any?, position: Int) {
                EventBusUtils.post(EventBusMessage(Const.LOCATION, mList[position]))
                finish()
            }

        })

        ll_search.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("city",city)
            IntentUtils.to(this, SearchLocationActivity::class.java,bundle)
        }
    }


    override fun start() {

    }

    override fun onDestroy() {
        super.onDestroy()
        map_view.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        map_view.onResume()
    }

    override fun onPause() {
        super.onPause()
        map_view.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        map_view.onSaveInstanceState(outState)
    }

    //查找附近的数据
    private fun searchNearby(latLng: LatLng, city: String) {
        //keyWord表示搜索字符串，
        //第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
        //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        val query = PoiSearch.Query("", "", city)
        query.pageSize = 20// 设置每页最多返回多少条poiitem
        query.pageNum = currentPage
        val search = PoiSearch(this, query)
        //设置周边搜索的中心点以及半径
        search.bound = PoiSearch.SearchBound(LatLonPoint(latLng.latitude, latLng.longitude), 1000)
        search.setOnPoiSearchListener(object : PoiSearch.OnPoiSearchListener {
            override fun onPoiItemSearched(p0: PoiItem?, p1: Int) {}

            override fun onPoiSearched(result: PoiResult?, code: Int) {
                //1000为成功，其他为失败
                if (code == 1000) {
                    mList.clear()
                    val pois = result?.pois
                    //重新构造数据
                    pois?.forEach {
                        mList.add(PoiLocationItem(it.poiId, it.latLonPoint, it.title, it.snippet, it.provinceName, it.cityName, it.adName, it.cityCode))
                    }
                    //如果有当前定位的数据的话，就直接选中，否则用返回的数据的第一条，选中
                    if (mList.isEmpty() || !mList[0].isMyLocation) {
                        mList[0].isSelect = true
                    }
                    //设置数据
                    mLocationMapAdapter.notifyDataSetChanged()
                } else {
                }
            }
        })
        //发送请求
        search.searchPOIAsyn()
    }

    //获取当前位置
    private fun getCurrentLocation(success: (AMapLocation) -> Unit) {

        //清空列表数据
        mList.clear()
        mLocationMapAdapter.notifyDataSetChanged()
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
                success(aMapLocation)
            } else {
                L.d("定位失败，请重新定位" + aMapLocation.errorInfo + ";;" + aMapLocation.errorCode)
            }
        }
    }

    //定位成功之后，构造Marker对象和数据
    private fun locationSuccess(location: AMapLocation) {
        val latLng = LatLng(location.latitude, location.longitude)
        //定位蓝点
        val marker_location_point = LayoutInflater.from(this).inflate(R.layout.marker_location_point, map_view, false)
        val marker = aMap.addMarker(MarkerOptions().position(latLng).title("").snippet("").icon(BitmapDescriptorFactory.fromView(marker_location_point)))
        //蓝点动画
        val alpha = AlphaAnimation(1f, 0.5f)//新建透明度动画
        alpha.setDuration(1000)//设置动画持续时间
        alpha.repeatCount = -1
        alpha.repeatMode = Animation.REVERSE
        marker.setAnimation(alpha)//图片设置动画
        marker.startAnimation()//开始动画
        //定位针
        val marker_position_needle = LayoutInflater.from(this).inflate(R.layout.marker_position_needle, map_view, false)
        centerMaker = aMap.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromView(marker_position_needle)).position(latLng).draggable(true).title("").snippet(""))
        //定位针一直在中心
        centerMaker.setPositionByPixels(map_view.width / 2, map_view.height / 2)
        //移动地图
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f))
        //构造列表的第一个定位数据
        val poiItem =
                PoiLocationItem("当前位置", LatLonPoint(location.latitude, location.longitude), location.poiName, location.address)
        poiItem.provinceName = location.province
        poiItem.cityName = location.city
        poiItem.adName = location.adCode
        poiItem.cityCode = location.cityCode
        poiItem.isSelect = true
        poiItem.isMyLocation = true
        mList.add(poiItem)
        city = location.city
        //查找附近的数据
        searchNearby(latLng, location.city)
    }

    override fun onRegeocodeSearched(result: RegeocodeResult?, code: Int) {
        if (code == 1000) {
            result?.let {
                city = it.regeocodeAddress.city
                searchNearby(latLng, it.regeocodeAddress.city)
            }
        } else {
            searchNearby(latLng, "")
        }
    }

    override fun onGeocodeSearched(p0: GeocodeResult?, p1: Int) {

    }

    override fun onReceiveEvent(event: EventBusMessage<Any>) {
        when (event.code) {
            1 -> finish()
        }
    }

}