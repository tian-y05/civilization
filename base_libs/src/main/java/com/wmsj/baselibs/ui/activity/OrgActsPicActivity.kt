package com.wmsj.baselibs.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.lcw.library.imagepicker.ImagePicker
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.OrgActsPicContract
import com.wmsj.baselibs.mvp.presenter.OrgActsPicPresenter
import com.wmsj.baselibs.ui.adapter.TakePhotoAdapter
import com.wmsj.baselibs.base.BaseActivity
import com.wmsj.baselibs.eventBus.EventBusMessage
import com.wmsj.baselibs.eventBus.EventBusUtils
import com.wmsj.baselibs.utils.GlideLoader
import com.wmsj.baselibs.utils.L
import com.wmsj.baselibs.utils.StringUtils
import com.wmsj.baselibs.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_org_acts.*

/**
 * 活动图片
on 2019/9/3.
 */
class OrgActsPicActivity : BaseActivity(), OrgActsPicContract.View {

    private val REQUEST_SELECT_IMAGES_CODE = 1
    private var id = ""
    private var imageLists = ArrayList<String>()
    private val mTakePhotoAdapter by lazy { TakePhotoAdapter(this, imageLists, R.layout.take_photo_item) }
    private val mPresenter by lazy { OrgActsPicPresenter() }


    override fun layoutId(): Int {
        return R.layout.activity_org_acts
    }

    override fun initData(savedInstanceState: Bundle?) {
        id = intent.getStringExtra("id")
        if(StringUtils.split(intent.getStringExtra("pic"), ",") != null){
            imageLists.addAll(StringUtils.split(intent.getStringExtra("pic"), ","))
        }
    }

    override fun initView() {
        mPresenter.attachView(this)
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, getString(R.string.activity_pic))

        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = mTakePhotoAdapter
        rl_pic.setOnClickListener {
            if (imageLists.size > 5) {
                ToastUtils.showShort(this, getString(R.string.pic_upload))
            } else {
                ImagePicker.getInstance()
                        .setTitle("选择图片")//设置标题
                        .showCamera(false)//设置是否显示拍照按钮
                        .showImage(true)//设置是否展示图片
                        .showVideo(false)//设置是否展示视频
                        .setSingleType(true)//设置图片视频不能同时选择
                        .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                        .setImageLoader(GlideLoader())//设置自定义图片加载器
                        .start(this, REQUEST_SELECT_IMAGES_CODE)//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
            }

        }

        mTakePhotoAdapter.setOnItemDeleteListener(object : TakePhotoAdapter.onItemDeleteListener {
            override fun onItemDelete(position: Int) {
                imageLists.removeAt(position)
                var map = HashMap<String, String>()
                map.put("id", id)
                map.put("pic", StringUtils.stringSpell(imageLists).toString())
                mPresenter.pulishData(map)
            }
        })
    }

    override fun start() {
    }

    override fun showLoading() {
        loadingDialog.show()
    }

    override fun dismissLoading() {
        loadingDialog.dismiss()
    }

    override fun setImageUploadResult(data: ArrayList<String>) {
        imageLists.add(0, data[0])
        L.d("orgPic  lists:" + imageLists.toString())
        L.d("orgPic  data :" + data[0])

        var map = HashMap<String, String>()
        map.put("id", id)
        map.put("pic", StringUtils.stringSpell(imageLists).toString())
        mPresenter.pulishData(map)

    }

    override fun pulishDataResult(data: String) {
        ToastUtils.showShort(this, data)
        mTakePhotoAdapter.notifyDataSetChanged()
        EventBusUtils.post(EventBusMessage(Const.ACTS_PIC_UPLOAD,""))
    }

    override fun showError(msg: String, errorCode: Int) {
        ToastUtils.showShort(this, msg)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === REQUEST_SELECT_IMAGES_CODE && resultCode === Activity.RESULT_OK) {
            var imagePaths = data?.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES)
            L.d("orgPic imagePaths:" + imagePaths)
            imagePaths?.let {
                mPresenter.imageUpload(this, it)
                mTakePhotoAdapter.notifyDataSetChanged()
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}