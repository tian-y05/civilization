package com.wmsj.baselibs.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.lcw.library.imagepicker.ImagePicker
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.TakePhotoOrVideoContract
import com.wmsj.baselibs.mvp.presenter.TakePhotoOrVideoPresenter
import com.wmsj.baselibs.ui.adapter.TakePhotoAdapter
import com.wmsj.baselibs.base.BaseActivity
import com.wmsj.baselibs.eventBus.EventBusMessage
import com.wmsj.baselibs.eventBus.EventBusUtils
import com.wmsj.baselibs.recyclerview.adapter.OnItemClickListener
import com.wmsj.baselibs.utils.GlideLoader
import com.wmsj.baselibs.utils.SPUtil
import com.wmsj.baselibs.utils.StringUtils
import com.wmsj.baselibs.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_acts_report.*

/**
 * 活动报道
on 2019/8/12.
 */
class OrgActsReportActivity : BaseActivity(), TakePhotoOrVideoContract.View {


    private val REQUEST_SELECT_IMAGES_CODE = 1
    private var id = ""
    private var name = ""
    private var imageLists = ArrayList<String>()
    private val mTakePhotoAdapter by lazy { TakePhotoAdapter(this, imageLists, R.layout.take_photo_item) }
    private val mPresenter by lazy { TakePhotoOrVideoPresenter() }

    override fun layoutId(): Int {
        return R.layout.activity_acts_report
    }

    override fun initData(savedInstanceState: Bundle?) {
        id = intent.getStringExtra("id")
        name = intent.getStringExtra("name")
    }

    override fun initView() {
        mPresenter.attachView(this)
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, getString(R.string.activity_report))

        et_title.setText(name)

        imageLists.add("addImage")
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = mTakePhotoAdapter

        mTakePhotoAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(obj: Any?, position: Int) {
                if (imageLists[position] == "addImage") {
                    ImagePicker.getInstance()
                            .setTitle("选择图片")//设置标题
                            .showCamera(false)//设置是否显示拍照按钮
                            .showImage(true)//设置是否展示图片
                            .showVideo(false)//设置是否展示视频
                            .setSingleType(true)//设置图片视频不能同时选择
                            .setMaxCount(9 - imageLists.size + 1)//设置最大选择图片数目(默认为1，单选)
                            .setImageLoader(GlideLoader())//设置自定义图片加载器
                            .start(this@OrgActsReportActivity, REQUEST_SELECT_IMAGES_CODE)//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
                }
            }
        })

        mTakePhotoAdapter.setOnItemDeleteListener(object : TakePhotoAdapter.onItemDeleteListener {
            override fun onItemDelete(position: Int) {
                imageLists.removeAt(position)
                if (imageLists.size < 9 && !imageLists.contains("addImage")) {
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
            } else if (StringUtils.isEmpty(et_title.text.toString())) {
                ToastUtils.showShort(this, getString(R.string.no_title))
            } else if (StringUtils.isEmpty(et_content.text.toString())) {
                ToastUtils.showShort(this, getString(R.string.no_content))
            } else {
                mPresenter.imageUpload(this, imageLists)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === REQUEST_SELECT_IMAGES_CODE && resultCode === Activity.RESULT_OK) {
            var imagePaths = data?.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES)
            imageLists.addAll(0, imagePaths as ArrayList<String>)
            if (imageLists.size > 9) {
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
        map.put("aid", id)
        map.put("image", StringUtils.stringSpell(data).toString())
        map.put("title", et_title.text.toString())
        map.put("content", et_content.text.toString())
        map.put("author", "")
        map.put("oid", SPUtil.get(this, "userId", "").toString())
        mPresenter.pushOrgReport(map)
    }

    override fun setVideoloadResult(data: String) {
    }

    override fun setPulishData(data: String) {
        ToastUtils.showShort(this, data)
        EventBusUtils.post(EventBusMessage(Const.ACTS_PIC_UPLOAD, ""))
        finish()
    }

    override fun showError(msg: String, errorCode: Int) {
        ToastUtils.showShort(this, msg)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}