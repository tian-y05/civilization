package com.wmsj.baselibs.ui.activity

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.FrameLayout
import com.amap.api.navi.services.search.model.Tip
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lcw.library.imagepicker.ImagePicker
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.PerfectOrgInfoContract
import com.wmsj.baselibs.mvp.presenter.PerfectOrgInfoPresenter
import com.wmsj.baselibs.weight.SelectDialog
import com.wmsj.baselibs.base.BaseActivity
import com.wmsj.baselibs.bean.AreaBean
import com.wmsj.baselibs.bean.BaseInfoBean
import com.wmsj.baselibs.bean.OrgBaseInfoBean
import com.wmsj.baselibs.bean.OrgListBean
import com.wmsj.baselibs.eventBus.EventBusMessage
import com.wmsj.baselibs.eventBus.EventBusUtils
import com.wmsj.baselibs.utils.*
import com.wmsj.baselibs.view.BottomListDialog
import com.wmsj.baselibs.view.SetSuccessDialog
import com.xbrc.myapplication.bean.PoiLocationItem
import kotlinx.android.synthetic.main.activity_perfect_org_info.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * 完善组织信息
on 2019/8/17.
 */
class PerfectOrgInfoActivity : BaseActivity(), PerfectOrgInfoContract.View, View.OnClickListener {


    private val DUTY_JUST = 1
    private val DUTY_BACK = 2
    private val LEGAL_PERSON_JUST = 3
    private val LEGAL_PERSON_BACK = 4
    private val CERTIFICATE = 5

    var map = HashMap<String, String>()
    var areaMap = HashMap<String, String>()
    var cityList = ArrayList<AreaBean>()
    var districtList = ArrayList<AreaBean>()
    var streetList = ArrayList<AreaBean>()
    var communityList = ArrayList<AreaBean>()
    var orgList = ArrayList<OrgListBean>()
    var isFirst = true
    private var orgInfo: OrgBaseInfoBean? = null
    private val mPresenter by lazy { PerfectOrgInfoPresenter() }
    private var provinceList = arrayListOf(BaseInfoBean("请选择", "0"), BaseInfoBean("江苏省", "8ad881e94d74108d014d7414c3220004"))

    override fun layoutId(): Int {
        return R.layout.activity_perfect_org_info
    }

    override fun initData(savedInstanceState: Bundle?) {
        mPresenter.attachView(this)
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, getString(R.string.base_message))

        orgInfo = intent.getSerializableExtra("orginfo") as OrgBaseInfoBean?
        if (orgInfo != null) {
            initBaseInfo(orgInfo)
        } else {
            tv_org_create_time.text = FormatUtils.getTime(Date(System.currentTimeMillis()), "yyyy-MM-dd")
            map.put("clrq", tv_org_create_time.text.toString())
            tv_province.text = provinceList[1].c_name
            areaMap.put("province", provinceList[1]!!.id!!)
            mPresenter.getArea(provinceList[1]!!.id!!, "province")
            mPresenter.getOrg(provinceList[1]!!.id!!)
        }

    }

    override fun initView() {
        tcv_first.setColor(resources.getColor(R.color.tab_select))
        tv_first.setTextColor(resources.getColor(R.color.tab_select))
        tcv_second.setColor(resources.getColor(R.color.leader_message))
        tv_second.setTextColor(resources.getColor(R.color.leader_message))

        ll_first.visibility = View.VISIBLE
        ll_two.visibility = View.GONE
        ll_registration_and_filing.visibility = View.GONE
        rl_org_service_area.setOnClickListener(this)
        rl_org_act_object.setOnClickListener(this)
        rl_org_type.setOnClickListener(this)
        rl_org_unit_nature.setOnClickListener(this)
        rl_org_create_time.setOnClickListener(this)
        rl_province.setOnClickListener(this)
        rl_city.setOnClickListener(this)
        rl_district.setOnClickListener(this)
        rl_street.setOnClickListener(this)
        rl_community.setOnClickListener(this)
        rl_competent_organization.setOnClickListener(this)
        rl_org_location.setOnClickListener(this)
        tv_sure.setOnClickListener(this)
        ll_card_positive.setOnClickListener(this)
        ll_card_back.setOnClickListener(this)
        ll_legal_card_positive.setOnClickListener(this)
        ll_legal_card_back.setOnClickListener(this)
        ll_document_scanning.setOnClickListener(this)
        iv_card_positive.setOnClickListener(this)
        iv_card_back.setOnClickListener(this)
        legal_card_positive.setOnClickListener(this)
        legal_card_back.setOnClickListener(this)
        iv_document_scanning.setOnClickListener(this)

        map.put("djyba", "false")
        cb_yes.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                cb_no.isChecked = false
                ll_registration_and_filing.visibility = View.VISIBLE
                map.put("djyba", "true")
            } else {
                ll_registration_and_filing.visibility = View.GONE
                map.put("djyba", "false")
            }
        }
        cb_no.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                cb_yes.isChecked = false
                ll_registration_and_filing.visibility = View.GONE
                map.put("djyba", "false")
            } else {
                ll_registration_and_filing.visibility = View.GONE
                map.put("djyba", "false")
            }
        }
    }

    override fun start() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rl_org_service_area -> {//服务领域
                getBaseInfoList("FWLY")?.let {
                    SelectDialog.Builder(this)
                            .setTitle(getString(R.string.org_service_area))
                            .setItemList(it)
                            .setItemSelectList(map["fwly"])
                            .setPositiveButton(getString(R.string.cancel), DialogInterface.OnClickListener { p0, p1 ->
                                p0?.dismiss()

                            })
                            .setNegativeButton(getString(R.string.sure), object : SelectDialog.onCheckBackListener {
                                override fun onChecked(dialog: DialogInterface, idList: ArrayList<String>?, nameList: ArrayList<String>?) {
                                    dialog.dismiss()
                                    tv_org_service_area.text = StringUtils.stringSpell(nameList)
                                    map.put("fwly", StringUtils.stringSpell(idList).toString())
                                }

                            })
                            .setWith(0.77f)
                            .create()
                            .show()
                }

            }
            R.id.rl_org_act_object -> {//志愿组织类型
                getBaseInfoList("ZYZZLX")?.let {
                    SelectDialog.Builder(this)
                            .setTitle(getString(R.string.vol_org_type))
                            .setItemList(it)
                            .setItemSelectList(map["zyzzlx"])
                            .setPositiveButton(getString(R.string.cancel), DialogInterface.OnClickListener { p0, p1 ->
                                p0?.dismiss()

                            })
                            .setNegativeButton(getString(R.string.sure), object : SelectDialog.onCheckBackListener {
                                override fun onChecked(dialog: DialogInterface, idList: ArrayList<String>?, nameList: ArrayList<String>?) {
                                    dialog.dismiss()
                                    tv_org_act_object.text = StringUtils.stringSpell(nameList)
                                    map.put("zyzzlx", StringUtils.stringSpell(idList).toString())
                                }

                            })
                            .setWith(0.77f)
                            .create()
                            .show()
                }
            }
            R.id.rl_org_type -> {//组织类型
                getBaseInfoList("ZZLX")?.let {
                    BottomListDialog().setBaseInfoList(it).setOnItemClickListener(object : BottomListDialog.OnItemClickListener {
                        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long, selectItem: String, itemList: ArrayList<String>) {
                            tv_org_type.text = selectItem
                            map.put("zzlx", it[position].id.toString())
                        }

                    }).show(supportFragmentManager, "dialog")
                }
            }
            R.id.rl_org_unit_nature -> {//单位性质
                getBaseInfoList("DWXZ")?.let {
                    BottomListDialog().setBaseInfoList(it).setOnItemClickListener(object : BottomListDialog.OnItemClickListener {
                        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long, selectItem: String, itemList: ArrayList<String>) {
                            tv_org_unit_nature.text = selectItem
                            map.put("dwxz", it[position].id.toString())
                        }

                    }).show(supportFragmentManager, "dialog")
                }
            }
            R.id.rl_org_create_time -> {//成立时间
                //时间选择器
                var pvTime = TimePickerBuilder(this, OnTimeSelectListener { date, v ->
                    tv_org_create_time.text = FormatUtils.getTime(date, "yyyy-MM-dd")
                    map.put("clrq", FormatUtils.getTime(date, "yyyy-MM-dd"))
                })
                        .setTitleText(getString(R.string.org_create_time))
                        .setCancelText(getString(R.string.cancel))
                        .setSubmitText(getString(R.string.sure))
                        .setType(booleanArrayOf(true, true, true, false, false, false))
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
            R.id.rl_competent_organization -> {//主管组织
                if (orgList.size > 0) {
                    BottomListDialog().setOrgList(orgList).setOnItemClickListener(object : BottomListDialog.OnItemClickListener {
                        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long, selectItem: String, itemList: ArrayList<String>) {
                            tv_competent_organization.text = selectItem
                            map.put("zgzz", orgList[position].id.toString())
                        }

                    }).show(supportFragmentManager, "dialog")
                }
            }
            R.id.rl_org_location -> {//详细地址
                IntentUtils.to(this, LocationMapActivity::class.java)
            }
            R.id.rl_province -> {
                BottomListDialog().setBaseInfoList(provinceList).setOnItemClickListener(object : BottomListDialog.OnItemClickListener {
                    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long, selectItem: String, itemList: ArrayList<String>) {
                        tv_province.text = selectItem
                        tv_city.text = getString(R.string.please_choose)
                        tv_district.text = getString(R.string.please_choose)
                        tv_street.text = getString(R.string.please_choose)
                        tv_community.text = getString(R.string.please_choose)
                        tv_competent_organization.text = getString(R.string.please_choose)
                        areaMap.remove("city")
                        areaMap.remove("street")
                        areaMap.remove("district")
                        areaMap.remove("community")
                        if (provinceList[position].id != "0") {
                            areaMap.put("province", provinceList[position]!!.id!!)
                            mPresenter.getArea(provinceList[position]!!.id!!, "province")
                            mPresenter.getOrg(provinceList[position]!!.id!!)
                        } else {
                            areaMap.remove("province")
                            cityList.clear()
                            districtList.clear()
                            streetList.clear()
                            communityList.clear()
                            orgList.clear()
                        }
                    }

                }).show(supportFragmentManager, "dialog")
            }
            R.id.rl_city -> {
                if (cityList.size > 0) {
                    BottomListDialog().setAreaInfoList(cityList).setOnItemClickListener(object : BottomListDialog.OnItemClickListener {
                        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long, selectItem: String, itemList: ArrayList<String>) {
                            tv_city.text = selectItem
                            tv_district.text = getString(R.string.please_choose)
                            tv_street.text = getString(R.string.please_choose)
                            tv_community.text = getString(R.string.please_choose)
                            tv_competent_organization.text = getString(R.string.please_choose)
                            if (cityList[position].id != "0") {
                                mPresenter.getArea(cityList[position].id, "city")
                                areaMap.put("city", cityList[position].id)
                                mPresenter.getOrg(cityList[position].id)
                            } else {
                                areaMap.remove("city")
                                districtList.clear()
                                streetList.clear()
                                communityList.clear()
                                orgList.clear()
                            }
                            areaMap.remove("district")
                            areaMap.remove("street")
                            areaMap.remove("community")
                        }

                    }).show(supportFragmentManager, "dialog")
                }
            }
            R.id.rl_district -> {
                if (districtList.size > 0) {
                    BottomListDialog().setAreaInfoList(districtList).setOnItemClickListener(object : BottomListDialog.OnItemClickListener {
                        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long, selectItem: String, itemList: ArrayList<String>) {
                            tv_district.text = selectItem
                            tv_street.text = getString(R.string.please_choose)
                            tv_community.text = getString(R.string.please_choose)
                            tv_competent_organization.text = getString(R.string.please_choose)
                            if (districtList[position].id != "0") {
                                mPresenter.getArea(districtList[position].id, "district")
                                mPresenter.getOrg(districtList[position].id)
                                areaMap.put("district", districtList[position].id)
                            } else {
                                areaMap.remove("district")
                                streetList.clear()
                                communityList.clear()
                                orgList.clear()
                            }
                            areaMap.remove("street")
                            areaMap.remove("community")
                        }

                    }).show(supportFragmentManager, "dialog")
                }
            }
            R.id.rl_street -> {
                if (streetList.size > 0) {
                    BottomListDialog().setAreaInfoList(streetList).setOnItemClickListener(object : BottomListDialog.OnItemClickListener {
                        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long, selectItem: String, itemList: ArrayList<String>) {
                            tv_street.text = selectItem
                            tv_community.text = getString(R.string.please_choose)
                            tv_competent_organization.text = getString(R.string.please_choose)
                            if (streetList[position].id != "0") {
                                mPresenter.getArea(streetList[position].id, "street")
                                mPresenter.getOrg(streetList[position].id)
                                areaMap.put("street", streetList[position].id)
                            } else {
                                areaMap.remove("street")
                                communityList.clear()
                                orgList.clear()
                            }
                            areaMap.remove("community")
                        }

                    }).show(supportFragmentManager, "dialog")
                }
            }
            R.id.rl_community -> {
                if (communityList.size > 0) {
                    BottomListDialog().setAreaInfoList(communityList).setOnItemClickListener(object : BottomListDialog.OnItemClickListener {
                        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long, selectItem: String, itemList: ArrayList<String>) {
                            tv_community.text = selectItem
                            tv_competent_organization.text = getString(R.string.please_choose)
                            if (communityList[position].id != "0") {
                                mPresenter.getOrg(communityList[position].id)
                                areaMap.put("community", communityList[position].id)
                            } else {
                                areaMap.remove("community")
                                orgList.clear()
                            }
                        }
                    }).show(supportFragmentManager, "dialog")
                }
            }
            R.id.tv_sure -> {
                if (isFirst) {
                    if (StringUtils.isEmptyToast(this, et_org_name.text.toString(), getString(R.string.org_name) + getString(R.string.is_null))) {
                    } else if (StringUtils.isEmptyToast(this, map["fwly"], getString(R.string.org_service_area) + getString(R.string.is_null))) {
                    } else if (StringUtils.isEmptyToast(this, map["zyzzlx"], getString(R.string.vol_org_type) + getString(R.string.is_null))) {
                    } else if (StringUtils.isEmptyToast(this, map["zzlx"], getString(R.string.org_type) + getString(R.string.is_null))) {
                    } else if (StringUtils.isEmptyToast(this, map["dwxz"], getString(R.string.org_unit_nature) + getString(R.string.is_null))) {
                    } else if (StringUtils.isEmptyToast(this, et_org_contacts.text.toString(), getString(R.string.org_contacts) + getString(R.string.is_null))) {
                    } else if (StringUtils.isEmptyToast(this, et_rl_org_contacts_tel.text.toString(), getString(R.string.org_contacts_tel) + getString(R.string.is_null))) {
                    } else if (StringUtils.isEmptyToast(this, map["clrq"], getString(R.string.org_create_time) + getString(R.string.is_null))) {
                    } else if (StringUtils.isEmptyToast(this, et_org_detail.text.toString(), getString(R.string.org_detail) + getString(R.string.is_null))) {
                    } else if (StringUtils.isEmptyToast(this, map["xxdzmz"], getString(R.string.org_location) + getString(R.string.is_null))) {
                    } else if (StringUtils.isEmptyToast(this, map["xxdz"], getString(R.string.org_location) + getString(R.string.is_null))) {
                    } else {
                        isFirst = false
                        scrollView.scrollTo(0, 0)
                        tcv_first.setColor(resources.getColor(R.color.leader_message))
                        tv_first.setTextColor(resources.getColor(R.color.leader_message))
                        tcv_second.setColor(resources.getColor(R.color.tab_select))
                        tv_second.setTextColor(resources.getColor(R.color.tab_select))
                        ll_first.visibility = View.GONE
                        ll_two.visibility = View.VISIBLE
                        if (cb_yes.isChecked) {
                            ll_registration_and_filing.visibility = View.VISIBLE
                        }
                        tv_sure.text = getString(R.string.sure)
                    }

                } else {
                    if (StringUtils.isEmptyToast(this, et_lead_name.text.toString(), getString(R.string.lead_name) + getString(R.string.is_null))) {
                    } else if (StringUtils.isEmptyToast(this, et_lead_tel.text.toString(), getString(R.string.lead_tel) + getString(R.string.is_null))) {
                    } else if (StringUtils.isEmptyToast(this, et_lead_card.text.toString(), getString(R.string.lead_card) + getString(R.string.is_null))) {
                    } else if (!et_lead_card.text.toString().contains("*") && IDCardValidate(et_lead_card.text.toString()).isCorrect != 0) {
                        ToastUtils.showShort(this, getString(R.string.card_true))
                    } else if (StringUtils.isEmptyToast(this, map["zgzz"], getString(R.string.competent_organization) + getString(R.string.is_null))) {
                    } else if (StringUtils.isEmptyToast(this, map["sfzzmz"], getString(R.string.lead_card_positive) + getString(R.string.is_null))) {
                    } else if (StringUtils.isEmptyToast(this, map["sfzfmz"], getString(R.string.lead_card_back) + getString(R.string.is_null))) {

                    } else if (cb_yes.isChecked && StringUtils.isEmptyToast(this, et_certificate_number.text.toString(), getString(R.string.certificate_number) + getString(R.string.is_null))) {
                    } else if (cb_yes.isChecked && StringUtils.isEmptyToast(this, map["frsfzzmz"], getString(R.string.legal_person_positive) + getString(R.string.is_null))) {
                    } else if (cb_yes.isChecked && StringUtils.isEmptyToast(this, map["frsfzfmz"], getString(R.string.legal_person_back) + getString(R.string.is_null))) {
                    } else if (cb_yes.isChecked && StringUtils.isEmptyToast(this, map["tyshxydmzssmj"], getString(R.string.document_scanning) + getString(R.string.is_null))) {
                    } else {
                        map.put("zzzwm", et_org_name.text.toString())
                        map.put("lxr", et_org_contacts.text.toString())
                        map.put("lxrdh", et_rl_org_contacts_tel.text.toString())
                        map.put("zzjj", et_org_detail.text.toString())
                        map.put("fzrxm", et_lead_name.text.toString())
                        map.put("fzrdh", et_lead_tel.text.toString())
                        map.put("sfzhm", et_lead_card.text.toString())
                        if (cb_yes.isChecked) {
                            map.put("tyshxydmzh", et_certificate_number.text.toString())
                        } else {
                            map.put("tyshxydmzh", et_certificate_number.text.toString())
                            map.put("frsfzzmz", et_certificate_number.text.toString())
                            map.put("frsfzfmz", et_certificate_number.text.toString())
                            map.put("tyshxydmzssmj", et_certificate_number.text.toString())
                        }
                        map.put("qy", getAreaKey())
                        map.put("djyba", if (cb_yes.isChecked) "true" else "false")

                        if (orgInfo != null) {
                            mPresenter.doPerfectInfo(map)
                        } else {
                            SetSuccessDialog.Builder(this)
                                    .setTitle(getString(R.string.sure_edit))
                                    .setMessage(getString(R.string.org_info_edit))
                                    .setPositiveButton(getString(R.string.cancel), DialogInterface.OnClickListener { p0, p1 ->
                                        p0.dismiss()
                                    })
                                    .setNegativeButton(getString(R.string.sure), DialogInterface.OnClickListener { p0, p1 ->
                                        p0.dismiss()
                                        mPresenter.doPerfectInfo(map)
                                    })
                                    .setWith(0.77f)
                                    .create()
                                    .show()
                        }
                    }
                }
            }
            R.id.ll_card_positive -> {
                Glide.with(this).load(R.mipmap.iv_card_positive).into(iv_card_positive)
                map.remove("sfzzmz")
                ll_card_positive.visibility = View.GONE
            }
            R.id.ll_card_back -> {
                Glide.with(this).load(R.mipmap.iv_card_back).into(iv_card_back)
                map.remove("sfzfmz")
                ll_card_back.visibility = View.GONE
            }
            R.id.ll_legal_card_positive -> {
                Glide.with(this).load(R.mipmap.iv_card_positive).into(legal_card_positive)
                map.remove("frsfzzmz")
                ll_legal_card_positive.visibility = View.GONE
            }
            R.id.ll_legal_card_back -> {
                Glide.with(this).load(R.mipmap.iv_card_back).into(legal_card_back)
                map.remove("frsfzfmz")
                ll_legal_card_back.visibility = View.GONE
            }
            R.id.ll_document_scanning -> {
                Glide.with(this).load(R.mipmap.document_scanning).into(iv_document_scanning)
                map.remove("tyshxydmzssmj")
                ll_document_scanning.visibility = View.GONE
            }
            R.id.iv_card_positive -> {
                ImagePicker.getInstance()
                        .setTitle("选择图片")//设置标题
                        .showCamera(false)//设置是否显示拍照按钮
                        .showImage(true)//设置是否展示图片
                        .showVideo(false)//设置是否展示视频
                        .setSingleType(true)//设置图片视频不能同时选择
                        .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                        .setImageLoader(GlideLoader())//设置自定义图片加载器
                        .start(this, DUTY_JUST)//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
            }
            R.id.iv_card_back -> {
                ImagePicker.getInstance()
                        .setTitle("选择图片")//设置标题
                        .showCamera(false)//设置是否显示拍照按钮
                        .showImage(true)//设置是否展示图片
                        .showVideo(false)//设置是否展示视频
                        .setSingleType(true)//设置图片视频不能同时选择
                        .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                        .setImageLoader(GlideLoader())//设置自定义图片加载器
                        .start(this, DUTY_BACK)//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
            }
            R.id.legal_card_positive -> {
                ImagePicker.getInstance()
                        .setTitle("选择图片")//设置标题
                        .showCamera(false)//设置是否显示拍照按钮
                        .showImage(true)//设置是否展示图片
                        .showVideo(false)//设置是否展示视频
                        .setSingleType(true)//设置图片视频不能同时选择
                        .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                        .setImageLoader(GlideLoader())//设置自定义图片加载器
                        .start(this, LEGAL_PERSON_JUST)//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
            }
            R.id.legal_card_back -> {
                ImagePicker.getInstance()
                        .setTitle("选择图片")//设置标题
                        .showCamera(false)//设置是否显示拍照按钮
                        .showImage(true)//设置是否展示图片
                        .showVideo(false)//设置是否展示视频
                        .setSingleType(true)//设置图片视频不能同时选择
                        .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                        .setImageLoader(GlideLoader())//设置自定义图片加载器
                        .start(this, LEGAL_PERSON_BACK)//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
            }
            R.id.iv_document_scanning -> {
                ImagePicker.getInstance()
                        .setTitle("选择图片")//设置标题
                        .showCamera(false)//设置是否显示拍照按钮
                        .showImage(true)//设置是否展示图片
                        .showVideo(false)//设置是否展示视频
                        .setSingleType(true)//设置图片视频不能同时选择
                        .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                        .setImageLoader(GlideLoader())//设置自定义图片加载器
                        .start(this, CERTIFICATE)//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
            }
        }
    }

    private fun getBaseInfoList(type: String): ArrayList<BaseInfoBean>? {
        return SPUtil.get(this, type, "")?.let {
            Gson().fromJson<List<BaseInfoBean>>(it.toString(), object : TypeToken<List<BaseInfoBean>>() {
            }.type)?.let { it as ArrayList<BaseInfoBean> }
        }
    }

    override fun showLoading() {
        loadingDialog.show()
    }

    override fun dismissLoading() {
        loadingDialog.dismiss()
    }


    override fun perfectInfoResult(data: String) {
        ToastUtils.showShort(this, data)
        EventBusUtils.post(EventBusMessage(Const.PERFECT_ORG_SUCCESS, ""))
        finish()
    }

    override fun areaResult(data: List<AreaBean>, type: String) {
        when (type) {
            "province" -> {
                cityList.clear()
                cityList.addAll(data)
                cityList.add(0, AreaBean("0", 0, "请选择"))
                if (!StringUtils.isEmpty(orgInfo?.area2)) {
                    tv_city.text = getArea(cityList, orgInfo!!.area2)
                }
            }
            "city" -> {
                districtList.clear()
                districtList.addAll(data)
                districtList.add(0, AreaBean("0", 0, "请选择"))
                if (!StringUtils.isEmpty(orgInfo?.area3)) {
                    tv_district.text = getArea(districtList, orgInfo!!.area3)
                }
            }
            "district" -> {
                streetList.clear()
                streetList.addAll(data)
                streetList.add(0, AreaBean("0", 0, "请选择"))
                if (!StringUtils.isEmpty(orgInfo?.area4)) {
                    tv_street.text = getArea(streetList, orgInfo!!.area4)
                }
            }
            "street" -> {
                communityList.clear()
                communityList.addAll(data)
                communityList.add(0, AreaBean("0", 0, "请选择"))
                if (!StringUtils.isEmpty(orgInfo?.area5)) {
                    tv_community.text = getArea(communityList, orgInfo!!.area5)
                }
            }

        }
    }

    override fun showError(errorMsg: String, errorCode: Int) {
        ToastUtils.showShort(this, errorMsg)
    }


    override fun getOrgResult(data: List<OrgListBean>) {
        orgList.clear()
        orgList.addAll(data)
        orgList.forEach {
            if (it.org_cname == orgInfo?.zgorgname) {
                map.put("zgzz", it.id!!)
                tv_competent_organization.text = it.org_cname
            }
        }
    }

    private fun initBaseInfo(data: OrgBaseInfoBean?) {
        data?.let {
            if (!StringUtils.isEmpty(it.org_cname)) {
                et_org_name.setText(it.org_cname)
                map.put("zzzwm", it.org_cname)
            }
            if (!StringUtils.isEmpty(it.service_field)) {
                tv_org_service_area.text = it.service_field
                map.put("fwly", it.service_field_id)
            }
            if (!StringUtils.isEmpty(it.org_extend_type)) {
                tv_org_act_object.text = it.org_extend_type
                map.put("zyzzlx", it.org_extend_type_id)
            }
            if (!StringUtils.isEmpty(it.org_type)) {
                tv_org_type.text = it.org_type
                map.put("zzlx", it.org_type_id)
            }
            if (!StringUtils.isEmpty(it.company_nature)) {
                tv_org_unit_nature.text = it.company_nature
                map.put("dwxz", it.company_nature_id)
            }
            if (!StringUtils.isEmpty(it.contact)) {
                et_org_contacts.setText(it.contact)
                map.put("lxr", it.contact)
            }
            if (!StringUtils.isEmpty(it.contact_mobile)) {
                et_rl_org_contacts_tel.setText(it.contact_mobile)
                map.put("lxrdh", it.contact_mobile)
            }
            if (!StringUtils.isEmpty(it.inserttime)) {
                tv_org_create_time.text = it.inserttime
                map.put("clrq", it.inserttime)
            }

            if (!StringUtils.isEmpty(it.desc)) {
                map.put("zzjj", it.desc)
                et_org_detail.setText(it.desc)
            }
            if (!StringUtils.isEmpty(it.gpsname)) {
                map.put("xxdzmz", it.gpsname)
                et_org_location.setText(it.gpsname)
            }

            if (!StringUtils.isEmpty(it.duty_name)) {
                map.put("fzrxm", it.duty_name)
                et_lead_name.setText(it.duty_name)
            }
            if (!StringUtils.isEmpty(it.duty_tel)) {
                map.put("fzrdh", it.duty_tel)
                et_lead_tel.setText(it.duty_tel)
            }

            if (!StringUtils.isEmpty(it.duty_code)) {
                map.put("sfzhm", it.duty_code)
                et_lead_card.setText(it.duty_code)
                et_lead_card.isFocusable = false
                et_lead_card.isClickable = false
            }

            if (!StringUtils.isEmpty(it.credit_code)) {
                map.put("tyshxydmzh", it.credit_code)
                et_certificate_number.setText(it.credit_code)
            }

            if (!StringUtils.isEmpty(it.gps)) {
                map.put("xxdz", it.gps)
            }

            if (!StringUtils.isEmpty(it.area1)) {
                tv_province.text = "江苏省"
                mPresenter.getArea(it.area1, "province")
                areaMap.put("province", it.area1)
            } else {
                mPresenter.getArea("8ad881e94d74108d014d7414c3220004", "province")
            }
            if (!StringUtils.isEmpty(it.area2)) {
                mPresenter.getArea(it.area2, "city")
                areaMap.put("city", it.area2)
            }
            if (!StringUtils.isEmpty(it.area3)) {
                mPresenter.getArea(it.area3, "district")
                areaMap.put("district", it.area3)
            }
            if (!StringUtils.isEmpty(it.area4)) {
                mPresenter.getArea(it.area4, "street")
                areaMap.put("street", it.area4)
            }
            if (!StringUtils.isEmpty(it.area5)) {
                areaMap.put("community", it.area5)
            }
            if (!StringUtils.isEmpty(it.area5)) {
                mPresenter.getOrg(it.area5)
            } else {
                if (!StringUtils.isEmpty(it.area4)) {
                    mPresenter.getOrg(it.area4)
                } else {
                    if (!StringUtils.isEmpty(it.area3)) {
                        mPresenter.getOrg(it.area3)
                    } else {
                        if (!StringUtils.isEmpty(it.area2)) {
                            mPresenter.getOrg(it.area2)
                        } else {
                            if (!StringUtils.isEmpty(it.area1)) {
                                mPresenter.getOrg(it.area1)
                            } else {
                                mPresenter.getOrg("8ad881e94d74108d014d7414c3220004")
                            }
                        }
                    }
                }
            }

            if (!StringUtils.isEmpty(it.duty_just)) {
                map.put("sfzzmz", it.duty_just)
                Glide.with(this).load(Const.BASE_URL + it.duty_just).into(iv_card_positive)
                ll_card_positive.visibility = View.VISIBLE
            }

            if (!StringUtils.isEmpty(it.duty_back)) {
                map.put("sfzfmz", it.duty_back)
                Glide.with(this).load(Const.BASE_URL + it.duty_back).into(iv_card_back)
                ll_card_back.visibility = View.VISIBLE
            }

            if (!StringUtils.isEmpty(it.legal_person_just)) {
                map.put("frsfzzmz", it.legal_person_just)
                Glide.with(this).load(Const.BASE_URL + it.legal_person_just).into(legal_card_positive)
                ll_legal_card_positive.visibility = View.VISIBLE
            }

            if (!StringUtils.isEmpty(it.legal_person_back)) {
                map.put("frsfzfmz", it.legal_person_back)
                Glide.with(this).load(Const.BASE_URL + it.legal_person_back).into(legal_card_back)
                ll_legal_card_back.visibility = View.VISIBLE
            }


            if (!StringUtils.isEmpty(it.certificate)) {
                map.put("tyshxydmzssmj", it.certificate)
                Glide.with(this).load(Const.BASE_URL + it.certificate).into(iv_document_scanning)
                ll_document_scanning.visibility = View.VISIBLE
            }

            when (data.is_register) {
                "0" -> {
                    cb_no.isChecked = true
                    ll_registration_and_filing.visibility = View.GONE
                    map.put("djyba", "false")
                    L.d("ischecked no")
                }
                "1" -> {
                    cb_yes.isChecked = true
                    ll_registration_and_filing.visibility = View.VISIBLE
                    map.put("djyba", "true")
                    L.d("ischecked yes")
                }
                else -> {
                }
            }
        }


    }


    private fun getArea(list: ArrayList<AreaBean>, areaId: String): String {
        return list
                .firstOrNull { it.id == areaId }
                ?.name
                ?: "请选择"
    }

    private fun getAreaKey(): String {
        var list = arrayListOf(areaMap["province"], areaMap["city"], areaMap["district"], areaMap["street"], areaMap["community"])
        return StringUtils.stringSpell(list).toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode === Activity.RESULT_OK) {
            var imagePaths = data?.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES)
            imagePaths?.let {
                when (requestCode) {
                    DUTY_JUST, DUTY_BACK, LEGAL_PERSON_BACK, LEGAL_PERSON_JUST, CERTIFICATE -> {
                        mPresenter.imageUpload(this, it, requestCode)
                    }
                }
            }


        }
    }


    override fun setImageUploadResult(data: ArrayList<String>, type: Int) {
        when (type) {
            DUTY_JUST -> {
                Glide.with(this).load(Const.BASE_URL + data[0]).into(iv_card_positive)
                ll_card_positive.visibility = View.VISIBLE
                map.put("sfzzmz", data[0])
            }
            DUTY_BACK -> {
                map.put("sfzfmz", data[0])
                Glide.with(this).load(Const.BASE_URL + data[0]).into(iv_card_back)
                ll_card_back.visibility = View.VISIBLE
            }
            LEGAL_PERSON_JUST -> {
                map.put("frsfzzmz", data[0])
                Glide.with(this).load(Const.BASE_URL + data[0]).into(legal_card_positive)
                ll_legal_card_positive.visibility = View.VISIBLE
            }
            LEGAL_PERSON_BACK -> {
                map.put("frsfzfmz", data[0])
                Glide.with(this).load(Const.BASE_URL + data[0]).into(legal_card_back)
                ll_legal_card_back.visibility = View.VISIBLE
            }
            CERTIFICATE -> {
                map.put("tyshxydmzssmj", data[0])
                Glide.with(this).load(Const.BASE_URL + data[0]).into(iv_document_scanning)
                ll_document_scanning.visibility = View.VISIBLE
            }
        }
    }


    override fun onReceiveEvent(event: EventBusMessage<Any>) {
        when (event.code) {
            Const.LOCATION -> {
                var data = event.data as PoiLocationItem
                et_org_location.setText(data.provinceName + data.cityName + data.address)
                map.put("xxdzmz", data.provinceName + data.cityName + data.address)
                map.put("xxdz", data.latLonPoint.toString())
            }
            Const.SEARCH_LOCATION -> {
                var data = event.data as Tip
                et_org_location.setText(data.district + data.address)
                map.put("xxdzmz", data.district + data.address)
                map.put("xxdz", data.point.toString())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        if (!isFirst) {
            isFirst = true
            tcv_first.setColor(resources.getColor(R.color.tab_select))
            tv_first.setTextColor(resources.getColor(R.color.tab_select))
            tcv_second.setColor(resources.getColor(R.color.leader_message))
            tv_second.setTextColor(resources.getColor(R.color.leader_message))
            ll_first.visibility = View.VISIBLE
            ll_two.visibility = View.GONE
            ll_registration_and_filing.visibility = View.GONE
        } else {
            finish()
        }
    }
}