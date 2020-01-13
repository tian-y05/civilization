package com.wmsj.baselibs.receiver;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.wmsj.baselibs.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by mardawang on 2017/7/6.
 * <p>
 * wy363681759@163.com
 */

public class SignedActivity extends AppCompatActivity implements View.OnClickListener, AMapLocationListener, LocationSource, PoiSearch.OnPoiSearchListener, AMap.OnCameraChangeListener {

    MapView mMapView;
    ListView lv_view;
    RelativeLayout rl_title;
    EditText et_key;
    Button btn_search;
    TextView tv_back;
    TextView tv_title;
    TextView tv_curdate;
    private AMap mAmap;
    private boolean isFirstLoc = true;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private String distance;
    private long firstTime;
    private ProgressDialog progDialog;
    private PoiSearch poiSearch;
    private PoiSearch.Query query;
    private PoiSearch.SearchBound searchBound;
    private String city;
    private ArrayList<PoiItem> poiItems;
    private NearbyAdapter mAdapter;
    String deepType = "";
    private LatLng latlng;
    private String key_search;
    private static final int LOCATION_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed);
//        ButterKnife.bind(this);
        mMapView = findViewById(R.id.bmapView);
        lv_view = findViewById(R.id.lv_view);
        rl_title = findViewById(R.id.rl_title);
        et_key = findViewById(R.id.et_key);
        btn_search = findViewById(R.id.btn_search);
        tv_back = findViewById(R.id.tv_back);
        tv_title = findViewById(R.id.tv_title);
        tv_curdate = findViewById(R.id.tv_curdate);

        btn_search.setOnClickListener(this);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        initMap();
        checkLocationPermission();//初始化请求权限，存储权限
    }

    private void initMap() {
        if (mAmap == null) {
            mAmap = mMapView.getMap();
            //开始定位
            initLoc();
        }
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.location));
        myLocationStyle.radiusFillColor(Color.GRAY);// 设置圆形的填充颜色
        myLocationStyle.strokeColor(Color.RED);//设置圆形的边框颜色
        mAmap.setMyLocationStyle(myLocationStyle);

        //设置定位监听
        mAmap.setLocationSource(this);
        // 是否显示定位按钮
        mAmap.getUiSettings().setMyLocationButtonEnabled(false);
        // 是否可触发定位并显示定位层
        mAmap.setMyLocationEnabled(true);
    }

    private void initLoc() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
//        //设置是否强制刷新WIFI，默认为强制刷新
//        mLocationOption.setWifiActiveScan(true);
//        //设置是否允许模拟位置,默认为false，不允许模拟位置
//        mLocationOption.setMockEnable(false);
//        //设置定位间隔,单位毫秒,默认为2000ms
//        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
//        //启动定位
//        mLocationClient.startLocation();
    }

    private void checkLocationPermission() {
        // 检查是否有定位权限
        // 检查权限的方法: ContextCompat.checkSelfPermission()两个参数分别是Context和权限名.
        // 返回PERMISSION_GRANTED是有权限，PERMISSION_DENIED没有权限
        if (ContextCompat.checkSelfPermission(SignedActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //没有权限，向系统申请该权限。
            Log.i("MY", "没有权限");
            requestPermission(LOCATION_PERMISSION_CODE);
        } else {
            //已经获得权限，则执行定位请求。
            Toast.makeText(SignedActivity.this, "已获取定位权限", Toast.LENGTH_SHORT).show();

            startLocation();

        }
    }

    private void requestPermission(int permissioncode) {
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        if (permission != null) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(SignedActivity.this,
                    permission)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                if (permissioncode == LOCATION_PERMISSION_CODE) {
//                    DialogFragment newFragment = HintDialogFragment.newInstance(R.string.location_description_title,
//                            R.string.location_description_why_we_need_the_permission,
//                            permissioncode);
//                    newFragment.show(getFragmentManager(), HintDialogFragment.class.getSimpleName());
                }


            } else {
                Log.i("MY", "返回false 不需要解释为啥要权限，可能是第一次请求，也可能是勾选了不再询问");
                ActivityCompat.requestPermissions(SignedActivity.this,
                        new String[]{permission}, permissioncode);
            }
        }
    }

    /**
     * 开始定位
     */
    private void startLocation() {
        // 启动定位
        mLocationClient.startLocation();
        Log.i("MY", "startLocation");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null && mListener != null) {
            if (amapLocation.getErrorCode() == 0) {
                // 显示我的位置
                mListener.onLocationChanged(amapLocation);
                SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm E");
                Date date = new Date(amapLocation.getTime());
                tv_curdate.setText(df.format(date) + "");
                city = amapLocation.getCity();
                //设置第一次焦点中心
                latlng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                mAmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 17), 1000, null);
                key_search = amapLocation.getPoiName();

                getNearbyInfo(key_search);

//                //设置缩放级别
                mAmap.moveCamera(CameraUpdateFactory.zoomTo(17));
                //点击定位按钮 能够将地图的中心移动到定位点
                mListener.onLocationChanged(amapLocation);
                //将地图移动到定位点
                mAmap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));

                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    et_key.setHint(key_search);

                    //添加图钉
                    mAmap.addMarker(getMarkerOptions(amapLocation));
                    isFirstLoc = false;
                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());

                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    //自定义一个图钉，并且设置图标，当我们点击图钉时，显示设置的信息
    private MarkerOptions getMarkerOptions(AMapLocation amapLocation) {
        //设置图钉选项
        MarkerOptions options = new MarkerOptions();

        //图标
        options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.location));
        //位置
        options.position(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()));
        StringBuffer buffer = new StringBuffer();
        buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
        //标题
        options.title(buffer.toString());
        //子标题
//        options.snippet("这里好火");
        //设置多少帧刷新一次图片资源
//        options.period(60);
        return options;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocationClient.startLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationClient.startLocation();
    }

    private void getNearbyInfo(String keyWord) {
        if ((keyWord == null || keyWord.isEmpty())) {
            return;
        }
//        showProgressDialog();
        mAmap.setOnMapClickListener(null);//搜索时清除掉map点击事件
        //第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query = new PoiSearch.Query(keyWord, deepType, city);
        query.setPageSize(15);// 设置每页最多返回多少条poiitem
        query.setPageNum(0);// 设置查第一页
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);//设置回调数据的监听器
        //点附近2000米内的搜索结果
        LatLonPoint lp = new LatLonPoint(latlng.latitude, latlng.longitude);
        poiSearch.setBound(new PoiSearch.SearchBound(lp, 5000, true));
        poiSearch.searchPOIAsyn();//开始搜索
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_search) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive() && getCurrentFocus() != null) {
                if (getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
            key_search = et_key.getText().toString().trim();
            getNearbyInfo(key_search);
        }
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        if (i == 1000) {
            Log.i("---", "查询结果:" + i);
            if (poiResult != null && poiResult.getQuery() != null) {// 搜索poi的结果
                poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                if (poiItems != null && poiItems.size() > 0) {
                    mAdapter = new NearbyAdapter(this, poiItems);
                    lv_view.setAdapter(mAdapter);

                }
            }
        } else if (i == 27) {
            Log.e("---", "error_network");
        } else if (i == 32) {
            Log.e("---", "error_key");
        } else {
            Log.e("---", "error_other：" + i);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        latlng = cameraPosition.target;
        mAmap.clear();
        mAmap.addMarker(new MarkerOptions().position(latlng));
        getNearbyInfo(key_search);
    }

    private String getDistance() {
//        LatLng target_local = new LatLng(39.909604, 116.39722);//天安门
        LatLng target_local = new LatLng(40.109604, 116.29722);
        float realDistance = AMapUtils.calculateLineDistance(target_local, latlng);
        if (realDistance < 1000) {
            distance = realDistance + " 米";
        } else {
            DecimalFormat df = new DecimalFormat("#.0");
            distance = df.format(realDistance / 1000) + " km";
        }
        return distance;
    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("正在搜索中。。。");
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        mLocationClient.onDestroy();
        super.onDestroy();
    }
}
