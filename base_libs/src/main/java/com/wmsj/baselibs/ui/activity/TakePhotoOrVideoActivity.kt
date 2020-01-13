package com.wmsj.baselibs.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.amap.api.services.help.Tip
import com.lcw.library.imagepicker.ImagePicker
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.TakePhotoOrVideoContract
import com.wmsj.baselibs.mvp.presenter.TakePhotoOrVideoPresenter
import com.wmsj.baselibs.ui.adapter.TakePhotoAdapter
import com.wmsj.baselibs.base.BaseActivity
import com.wmsj.baselibs.eventBus.EventBusMessage
import com.wmsj.baselibs.recyclerview.adapter.OnItemClickListener
import com.wmsj.baselibs.utils.*
import com.xbrc.myapplication.bean.PoiLocationItem
import kotlinx.android.synthetic.main.activity_photo_video.*


/**
 * 发布视频或图片
on 2019/8/12.
 */
class TakePhotoOrVideoActivity : BaseActivity(), TakePhotoOrVideoContract.View {


    private val REQUEST_SELECT_IMAGES_CODE = 1

    private var imageLists = ArrayList<String>()
    private val mTakePhotoAdapter by lazy { TakePhotoAdapter(this, imageLists, R.layout.take_photo_item) }
    private val mPresenter by lazy { TakePhotoOrVideoPresenter() }
    private var isVideo = false
    private var city = ""
    private var latLonPoint = ""
    private var videoThumbnail = ""

    override fun layoutId(): Int {
        return R.layout.activity_photo_video
    }

    override fun initData(savedInstanceState: Bundle?) {

        mPresenter.attachView(this)
        isVideo = intent.getBooleanExtra("isVideo", false)
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, getString(R.string.publish))

        imageLists.add("addImage")
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = mTakePhotoAdapter
    }

    override fun initView() {
        tv_location.text = getString(R.string.please_location)
        mTakePhotoAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(obj: Any?, position: Int) {
                if (imageLists[position] == "addImage") {
                    ImagePicker.getInstance()
                            .setTitle(if (isVideo) "选择视频" else "选择图片")//设置标题
                            .showCamera(false)//设置是否显示拍照按钮
                            .showImage(!isVideo)//设置是否展示图片
                            .showVideo(isVideo)//设置是否展示视频
                            .setSingleType(true)//设置图片视频不能同时选择
                            .setMaxCount(if (isVideo) 1 else (9 - imageLists.size + 1))//设置最大选择图片数目(默认为1，单选)
                            .setImageLoader(GlideLoader())//设置自定义图片加载器
                            .start(this@TakePhotoOrVideoActivity, REQUEST_SELECT_IMAGES_CODE)//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
                }
            }
        })

        mTakePhotoAdapter.setOnItemDeleteListener(object : TakePhotoAdapter.onItemDeleteListener {
            override fun onItemDelete(position: Int) {
                imageLists.removeAt(position)
                if (isVideo || (imageLists.size < 9 && !isVideo && !imageLists.contains("addImage"))) {
                    imageLists.add("addImage")
                }
                mTakePhotoAdapter.notifyDataSetChanged()
            }

        })
    }

    override fun start() {
        tv_publish.setOnClickListener {
            if ((imageLists.contains("addImage") && imageLists.size == 1) || imageLists.size < 1) {
                ToastUtils.showShort(this, getString(R.string.no_file))
            } else {
                if (isVideo)
                    mPresenter.videoUpload(imageLists[0])
                else
                    mPresenter.imageUpload(this, imageLists)

            }
        }
        rl_location.setOnClickListener {
            IntentUtils.to(this, LocationMapActivity::class.java)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === REQUEST_SELECT_IMAGES_CODE && resultCode === Activity.RESULT_OK) {
            var imagePaths = data?.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES)
            imageLists.addAll(0, imagePaths as ArrayList<String>)
            if (imageLists.size > 9 || isVideo) {
                imageLists.remove("addImage")
            }
            mTakePhotoAdapter.notifyDataSetChanged()
        }
    }


    override fun showLoading() {
        postDialog.show()
    }

    override fun dismissLoading() {
        postDialog.dismiss()
    }

    override fun setImageUploadResult(data: ArrayList<String>) {
        var map = HashMap<String, String>()
        map.put("userid", SPUtil.get(this, "userId", "").toString())
        map.put("file", StringUtils.stringSpell(data).toString())
        map.put("role", SPUtil.get(this, "role", "").toString())
        map.put("type", if (isVideo) "video" else "image")
        map.put("content", et_content.text.toString())
        map.put("city", city)
        map.put("gps", latLonPoint)
        map.put("address", tv_location.text.toString())
        mPresenter.requestPulishData(map, "")
    }

    override fun setVideoloadResult(data: String) {
        var map = HashMap<String, String>()
        map.put("userid", SPUtil.get(this, "userId", "").toString())
        map.put("file", data)
        map.put("role", if (SPUtil.get(this, "role", "").toString() == "volunteer") "zyz" else "org")
        map.put("type", if (isVideo) "video" else "image")
        map.put("content", et_content.text.toString())
        map.put("city", city)
        map.put("gps", latLonPoint)
        map.put("address", tv_location.text.toString())
        videoThumbnail = FileUtils.bitmap2File(FileUtils.getVideoThumbnail(imageLists[0]), System.currentTimeMillis().toString() + ".jpeg")
        mPresenter.requestPulishData(map, videoThumbnail)
    }

    override fun setPulishData(data: String) {
        FileUtils.deleteFile(videoThumbnail)
        ToastUtils.showShort(this, data)
        finish()
    }

    override fun showError(msg: String, errorCode: Int) {
        ToastUtils.showShort(this, msg)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }


    override fun onReceiveEvent(event: EventBusMessage<Any>) {
        when (event.code) {
            Const.LOCATION -> {
                var data = event.data as PoiLocationItem
                tv_location.text = data.provinceName + data.cityName + data.address
                city = data.cityName
                latLonPoint = data.latLonPoint.toString()
            }
            Const.SEARCH_LOCATION -> {
                var data = event.data as Tip
                tv_location.text = data.district + data.address
                city = data.district
                latLonPoint = data.point.toString()
            }
        }
    }
}