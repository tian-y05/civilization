package com.wmsj.baselibs.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.FrameLayout
import com.amap.api.services.help.Tip
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lcw.library.imagepicker.ImagePicker
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.PublishContract
import com.wmsj.baselibs.mvp.presenter.PublishActsPresenter
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.base.BaseActivity
import com.wmsj.baselibs.bean.ActivityShowBean
import com.wmsj.baselibs.bean.BaseInfoBean
import com.wmsj.baselibs.bean.ReviewActsBean
import com.wmsj.baselibs.eventBus.EventBusMessage
import com.wmsj.baselibs.utils.*
import com.wmsj.baselibs.view.BottomListDialog
import com.xbrc.myapplication.bean.PoiLocationItem
import kotlinx.android.synthetic.main.activity_publish_acts.*
import java.util.*

/**
 * 发布活动
on 2019/8/29.
 */
class PublishActsActivity : BaseActivity(), View.OnClickListener, PublishContract.View {

    private val IMAGE_CHOOSE = 1
    private val mPresenter by lazy { PublishActsPresenter() }
    var map = HashMap<String, String>()

    override fun layoutId(): Int {
        return R.layout.activity_publish_acts
    }

    override fun initData(savedInstanceState: Bundle?) {
        mPresenter.attachView(this)
        var id = intent.getStringExtra("id")
        if (!StringUtils.isEmpty(id)) {
            mPresenter.getMyOrgDetails(id)
        }
    }

    override fun initView() {
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, getString(R.string.publish_activity))
        et_recruit_start.setOnClickListener(this)
        et_recruit_end.setOnClickListener(this)
        et_acts_start.setOnClickListener(this)
        et_acts_end.setOnClickListener(this)
        rl_acts_place.setOnClickListener(this)
        rl_acts_service_area.setOnClickListener(this)
        rl_acts_object.setOnClickListener(this)
        rl_open_scope.setOnClickListener(this)
        iv_logo.setOnClickListener(this)
        ll_save.setOnClickListener(this)
        ll_see.setOnClickListener(this)
        ll_publish.setOnClickListener(this)
        ll_logo_delete.setOnClickListener(this)
        et_contacts_people.setText(intent.getStringExtra("contact"))
        et_time_recorder.setText(intent.getStringExtra("contact"))
        et_contacts_phone.setText(intent.getStringExtra("contact_mobile"))
    }

    override fun start() {
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_logo -> {
                ImagePicker.getInstance()
                        .setTitle("选择图片")//设置标题
                        .showCamera(false)//设置是否显示拍照按钮
                        .showImage(true)//设置是否展示图片
                        .showVideo(false)//设置是否展示视频
                        .setSingleType(true)//设置图片视频不能同时选择
                        .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                        .setImageLoader(GlideLoader())//设置自定义图片加载器
                        .start(this, IMAGE_CHOOSE)//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
            }
            R.id.et_recruit_start -> {
                //时间选择器
                var pvTime = TimePickerBuilder(this, OnTimeSelectListener { date, v ->
                    if (FormatUtils.compareDate(FormatUtils.getTime(date, "yyyy-MM-dd HH:mm"), FormatUtils.getCurrentTime(), "yyyy-MM-dd HH:mm") == 1) {
                        et_recruit_start.setText(FormatUtils.getTime(date, "yyyy-MM-dd HH:mm"))
                        map.put("recruit_starttime", et_recruit_start.text.toString())
                    } else {
                        ToastUtils.showShort(this, getString(R.string.small_current_time))
                    }
                })
                        .setTitleText(getString(R.string.start_time))
                        .setCancelText(getString(R.string.cancel))
                        .setSubmitText(getString(R.string.sure))
                        .setType(booleanArrayOf(true, true, true, true, true, false))
                        .setRangDate(if (StringUtils.isEmpty(et_recruit_start.text.toString())) FormatUtils.getCurTime() else FormatUtils.getCalendar(et_recruit_start.text.toString()), FormatUtils.getEndTime())
                        .isDialog(true)
                        .build()
                val mDialog = pvTime.dialog
                if (mDialog != null) {
                    val params = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            Gravity.BOTTOM)

                    params.leftMargin = 0
                    params.rightMargin = 0
                    pvTime.dialogContainerLayout.layoutParams = params

                    val dialogWindow = mDialog!!.window
                    if (dialogWindow != null) {
                        dialogWindow!!.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim)//修改动画样式
                        dialogWindow!!.setGravity(Gravity.BOTTOM)//改成Bottom,底部显示
                        dialogWindow!!.setDimAmount(0.1f)
                    }
                }
                pvTime.show()

            }
            R.id.et_recruit_end -> {
                //时间选择器
                var pvTime = TimePickerBuilder(this, OnTimeSelectListener { date, v ->
                    if (FormatUtils.compareDate(FormatUtils.getTime(date, "yyyy-MM-dd HH:mm"), FormatUtils.getCurrentTime(), "yyyy-MM-dd HH:mm") == 1) {
                        et_recruit_end.setText(FormatUtils.getTime(date, "yyyy-MM-dd HH:mm"))
                        map.put("recruit_endtime", et_recruit_end.text.toString())
                    } else {
                        ToastUtils.showShort(this, getString(R.string.small_current_time))
                    }

                })
                        .setTitleText(getString(R.string.end_time))
                        .setCancelText(getString(R.string.cancel))
                        .setSubmitText(getString(R.string.sure))
                        .setType(booleanArrayOf(true, true, true, true, true, false))
                        .setRangDate(if (StringUtils.isEmpty(et_recruit_end.text.toString())) FormatUtils.getCurTime() else FormatUtils.getCalendar(et_recruit_end.text.toString()), FormatUtils.getEndTime())
                        .isDialog(true)
                        .build()
                val mDialog = pvTime.dialog
                if (mDialog != null) {
                    val params = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            Gravity.BOTTOM)

                    params.leftMargin = 0
                    params.rightMargin = 0
                    pvTime.dialogContainerLayout.layoutParams = params

                    val dialogWindow = mDialog!!.window
                    if (dialogWindow != null) {
                        dialogWindow!!.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim)//修改动画样式
                        dialogWindow!!.setGravity(Gravity.BOTTOM)//改成Bottom,底部显示
                        dialogWindow!!.setDimAmount(0.1f)
                    }
                }
                pvTime.show()


            }
            R.id.et_acts_start -> {
                //时间选择器
                var pvTime = TimePickerBuilder(this, OnTimeSelectListener { date, v ->
                    if (FormatUtils.compareDate(FormatUtils.getTime(date, "yyyy-MM-dd HH:mm"), FormatUtils.getCurrentTime(), "yyyy-MM-dd HH:mm") == 1) {
                        et_acts_start.setText(FormatUtils.getTime(date, "yyyy-MM-dd HH:mm"))
                        dataTimeLong()
                        map.put("start_time", et_acts_start.text.toString())
                    } else {
                        ToastUtils.showShort(this, getString(R.string.small_current_time))
                    }
                })
                        .setTitleText(getString(R.string.start_time))
                        .setCancelText(getString(R.string.cancel))
                        .setSubmitText(getString(R.string.sure))
                        .setType(booleanArrayOf(true, true, true, true, true, false))
                        .setRangDate(if (StringUtils.isEmpty(et_acts_start.text.toString())) FormatUtils.getCurTime() else FormatUtils.getCalendar(et_acts_start.text.toString()), FormatUtils.getEndTime())
                        .isDialog(true)
                        .build()
                val mDialog = pvTime.dialog
                if (mDialog != null) {
                    val params = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            Gravity.BOTTOM)

                    params.leftMargin = 0
                    params.rightMargin = 0
                    pvTime.dialogContainerLayout.layoutParams = params

                    val dialogWindow = mDialog!!.window
                    if (dialogWindow != null) {
                        dialogWindow!!.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim)//修改动画样式
                        dialogWindow!!.setGravity(Gravity.BOTTOM)//改成Bottom,底部显示
                        dialogWindow!!.setDimAmount(0.1f)
                    }
                }
                pvTime.show()
            }
            R.id.et_acts_end -> {
                var pvTime = TimePickerBuilder(this, OnTimeSelectListener { date, v ->
                    if (FormatUtils.compareDate(FormatUtils.getTime(date, "yyyy-MM-dd HH:mm"), FormatUtils.getCurrentTime(), "yyyy-MM-dd HH:mm") == 1) {
                        et_acts_end.setText(FormatUtils.getTime(date, "yyyy-MM-dd HH:mm"))
                        dataTimeLong()
                        map.put("end_time", et_acts_end.text.toString())
                    } else {
                        ToastUtils.showShort(this, getString(R.string.small_current_time))
                    }
                })
                        .setTitleText(getString(R.string.end_time))
                        .setCancelText(getString(R.string.cancel))
                        .setSubmitText(getString(R.string.sure))
                        .setType(booleanArrayOf(true, true, true, true, true, false))
                        .setRangDate(if (StringUtils.isEmpty(et_acts_end.text.toString())) FormatUtils.getCurTime() else FormatUtils.getCalendar(et_acts_end.text.toString()), FormatUtils.getEndTime())
                        .isDialog(true)
                        .build()
                val mDialog = pvTime.dialog
                if (mDialog != null) {
                    val params = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            Gravity.BOTTOM)

                    params.leftMargin = 0
                    params.rightMargin = 0
                    pvTime.dialogContainerLayout.layoutParams = params

                    val dialogWindow = mDialog!!.window
                    if (dialogWindow != null) {
                        dialogWindow!!.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim)//修改动画样式
                        dialogWindow!!.setGravity(Gravity.BOTTOM)//改成Bottom,底部显示
                        dialogWindow!!.setDimAmount(0.1f)
                    }
                }
                pvTime.show()
            }
            R.id.rl_acts_place -> {
                IntentUtils.to(this, LocationMapActivity::class.java)
            }
            R.id.rl_acts_service_area -> {
                getBaseInfoList("FWLY")?.let {
                    BottomListDialog().setBaseInfoList(it).setOnItemClickListener(object : BottomListDialog.OnItemClickListener {
                        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long, selectItem: String, itemList: ArrayList<String>) {
                            et_service_area.text = selectItem
                            map.put("activity_type", it[position].id.toString())
                        }

                    }).show(supportFragmentManager, "dialog")
                }
            }
            R.id.rl_acts_object -> {
                getBaseInfoList("FWDX")?.let {
                    BottomListDialog().setBaseInfoList(it).setOnItemClickListener(object : BottomListDialog.OnItemClickListener {
                        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long, selectItem: String, itemList: ArrayList<String>) {
                            et_acts_object.text = selectItem
                            map.put("activity_object", it[position].id.toString())
                        }

                    }).show(supportFragmentManager, "dialog")
                }
            }
            R.id.rl_open_scope -> {
                getBaseInfoList("GKFW")?.let {
                    BottomListDialog().setBaseInfoList(it).setOnItemClickListener(object : BottomListDialog.OnItemClickListener {
                        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long, selectItem: String, itemList: ArrayList<String>) {
                            et_open_scope.text = selectItem
                            map.put("scope", it[position].id.toString())
                        }

                    }).show(supportFragmentManager, "dialog")
                }
            }
            R.id.ll_save -> {
                if (setInformation()) {
                    map.put("statu", "0")
                    mPresenter.publishActs(map)
                }

            }
            R.id.ll_see -> {
                if (setInformation()) run {
                    var bundle = Bundle()
                    bundle.putSerializable("data", ReviewActsBean(map, SPUtil.get(this, "zgorgname", "").toString(), et_service_area.text.toString(), et_acts_object.text.toString()))
                    IntentUtils.to(this, ReviewActsActivity::class.java, bundle)
                }
            }
            R.id.ll_publish -> {
                if (setInformation()) {
                    map.put("statu", "2")
                    mPresenter.publishActs(map)
                }

            }
            R.id.ll_logo_delete -> {
                ll_logo_delete.visibility = View.GONE
                Glide.with(this).load(R.mipmap.acts_background).into(iv_logo)
                map.remove("activity_logo")
            }

        }
    }

    private fun setInformation(): Boolean {
        if (StringUtils.isEmptyToast(this, et_acts_name.text.toString(), getString(R.string.activity_name) + getString(R.string.is_null))) {
        } else if (StringUtils.isEmptyToast(this, et_acts_num.text.toString(), getString(R.string.activity_number) + getString(R.string.is_null))) {
        } else if (StringUtils.isEmptyToast(this, et_service_time.text.toString(), getString(R.string.service_time) + getString(R.string.is_null))) {
        } else if (StringUtils.isEmptyToast(this, et_contacts_people.text.toString(), getString(R.string.contacts_people) + getString(R.string.is_null))) {
        } else if (StringUtils.isEmptyToast(this, et_contacts_phone.text.toString(), getString(R.string.contacts_phone) + getString(R.string.is_null))) {
        } else if (StringUtils.isEmptyToast(this, et_time_recorder.text.toString(), getString(R.string.time_recorder) + getString(R.string.is_null))) {
        } else if (StringUtils.isEmptyToast(this, et_org_detail.text.toString(), getString(R.string.activity_introduce) + getString(R.string.is_null))) {
        } else if (StringUtils.isEmptyToast(this, map["recruit_starttime"], getString(R.string.recruit_time) + getString(R.string.is_null))) {
        } else if (StringUtils.isEmptyToast(this, map["recruit_endtime"], getString(R.string.recruit_time) + getString(R.string.is_null))) {
        } else if (StringUtils.isEmptyToast(this, map["start_time"], getString(R.string.activity_time) + getString(R.string.is_null))) {
        } else if (StringUtils.isEmptyToast(this, map["end_time"], getString(R.string.activity_time) + getString(R.string.is_null))) {
        } else if (StringUtils.isEmptyToast(this, map["activity_pace"], getString(R.string.activity_place) + getString(R.string.is_null))) {
        } else if (StringUtils.isEmptyToast(this, map["activity_type"], getString(R.string.org_service_area) + getString(R.string.is_null))) {
        } else if (StringUtils.isEmptyToast(this, map["activity_object"], getString(R.string.service_object) + getString(R.string.is_null))) {
        } else if (StringUtils.isEmptyToast(this, map["scope"], getString(R.string.open_scope) + getString(R.string.is_null))) {
        } else if (FormatUtils.compareDate(et_recruit_start.text.toString(), et_recruit_end.text.toString(), "yyyy-MM-dd hh:mm") == 1) {
            ToastUtils.showShort(this, getString(R.string.recruit_time_no))
        } else if (FormatUtils.compareDate(et_recruit_end.text.toString(), et_acts_start.text.toString(), "yyyy-MM-dd hh:mm") == 1) {
            ToastUtils.showShort(this, getString(R.string.time_no))
        } else if (FormatUtils.compareDate(et_acts_start.text.toString(), et_acts_end.text.toString(), "yyyy-MM-dd hh:mm") == 1) {
            ToastUtils.showShort(this, getString(R.string.time_result))
        } else if (FormatUtils.dateDiff(et_acts_start.text.toString(), et_acts_end.text.toString(), "yyyy-MM-dd HH:mm") > 8.5 || FormatUtils.dateDiff(et_acts_start.text.toString(), et_acts_end.text.toString(), "yyyy-MM-dd HH:mm") < 0.5) {
            ToastUtils.showShort(this, getString(R.string.time_long))
        } else {
            map.put("activity_name", et_acts_name.text.toString())
            map.put("recruitment", et_acts_num.text.toString())
            map.put("service_time", et_service_time.text.toString())
            map.put("activity_contacts", et_contacts_people.text.toString())
            map.put("tel", et_contacts_phone.text.toString())
            map.put("time_noter", et_time_recorder.text.toString())
            map.put("activity_introduce", et_org_detail.text.toString())
            return true
        }
        return false

    }

    private fun dataTimeLong() {
        var timeLong = FormatUtils.dateDiff(et_acts_start.text.toString(), et_acts_end.text.toString(), "yyyy-MM-dd HH:mm")
        if (timeLong > 8.5 || timeLong < 0.5) {
            ToastUtils.showShort(this, getString(R.string.time_long))
            et_service_time.setText("0")
        } else {
            et_service_time.setText(timeLong.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode === Activity.RESULT_OK) {
            when (requestCode) {
                IMAGE_CHOOSE -> {
                    var imagePaths = data?.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES)
                    imagePaths?.let {
                        mPresenter.imageUpload(this, it)
                        Glide.with(this).load(it[0]).into(iv_logo)
                    }

                }

            }
        }
    }

    override fun showLoading() {
        loadingDialog.show()
    }

    override fun dismissLoading() {
        loadingDialog.dismiss()
    }

    override fun publishResult(data: String) {
        ToastUtils.showShort(this, data)
        finish()
    }

    override fun showError(errorMsg: String, errorCode: Int) {
        ToastUtils.showShort(this, errorMsg)
    }

    override fun setImageUploadResult(data: ArrayList<String>) {
//        Glide.with(this).load(Const.BASE_URL + data[0]).into(iv_logo)
        map.put("activity_logo", data[0])
        ll_logo_delete.visibility = View.VISIBLE
    }


    override fun onReceiveEvent(event: EventBusMessage<Any>) {
        when (event.code) {
            Const.LOCATION -> {
                var data = event.data as PoiLocationItem
                et_acts_place.text = data.provinceName + data.cityName + data.address
                map.put("activity_pace", data.provinceName + data.cityName + data.address)
                map.put("jwd", data.latLonPoint.toString())
            }
            Const.SEARCH_LOCATION -> {
                var data = event.data as Tip
                et_acts_place.text = data.district + data.address
                map.put("activity_pace", data.district + data.address)
                map.put("jwd", data.point.toString())
            }
        }
    }

    private fun getBaseInfoList(type: String): ArrayList<BaseInfoBean>? {
        val json = Gson().fromJson<List<BaseInfoBean>>(SPUtil.get(this, type, "").toString(), object : TypeToken<List<BaseInfoBean>>() {
        }.type)
        return json as ArrayList<BaseInfoBean>
    }

    override fun setOrgInfoDetails(data: ActivityShowBean) {
        Glide.with(this).load(Const.BASE_URL + data.activity_logo).into(iv_logo)
        map.put("activity_logo", data.activity_logo)
        map.put("id", data.id)
        ll_logo_delete.visibility = View.VISIBLE
        et_acts_name.setText(data.activity_name)
        et_acts_num.setText(data.recruitment.toString())
        et_service_time.setText(data.service_time)
        et_contacts_people.setText(data.activity_contacts)
        et_contacts_phone.setText(data.tel)
        et_time_recorder.setText(data.time_noter)
        et_org_detail.setText(data.activity_introduce)
        et_acts_place.text = data.activity_pace
        map.put("activity_pace", data.activity_pace)
        map.put("jwd", data.jwd)
        et_recruit_start.setText(data.recruit_starttime)
        map.put("recruit_starttime", data.recruit_starttime)
        et_recruit_end.setText(data.recruit_endtime)
        map.put("recruit_endtime", data.recruit_endtime)
        et_acts_start.setText(data.start_time)
        map.put("start_time", data.start_time)
        et_acts_end.setText(data.end_time)
        map.put("end_time", data.end_time)
        et_service_area.text = data.activity_type
        map.put("activity_type", data.activity_type_id)
        et_acts_object.text = data.activity_object
        map.put("activity_object", data.activity_object_id)
        et_open_scope.text = data.scope_cn
        map.put("scope", data.scope)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }


}