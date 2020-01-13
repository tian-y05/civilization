package com.wmsj.baselibs.ui.activity

import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.FrameLayout
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.PerfectInfomationContract
import com.wmsj.baselibs.mvp.presenter.PerfectInfomationPresenter
import com.wmsj.baselibs.weight.SelectDialog
import com.wmsj.baselibs.base.BaseActivity
import com.wmsj.baselibs.bean.AreaBean
import com.wmsj.baselibs.bean.BaseInfoBean
import com.wmsj.baselibs.bean.UserInfo
import com.wmsj.baselibs.eventBus.EventBusMessage
import com.wmsj.baselibs.eventBus.EventBusUtils
import com.wmsj.baselibs.utils.*
import com.wmsj.baselibs.view.BottomListDialog
import kotlinx.android.synthetic.main.activity_perfect_information.*
import java.util.*
import kotlin.collections.ArrayList


/**
 * 完善信息
on 2019/8/17.
 */
class PerfectInformationActivity : BaseActivity(), PerfectInfomationContract.View, View.OnClickListener {
    var map = HashMap<String, String>()
    var areaList = HashMap<String, String>()
    var cityList = ArrayList<AreaBean>()
    var districtList = ArrayList<AreaBean>()
    var streetList = ArrayList<AreaBean>()
    var communityList = ArrayList<AreaBean>()
    var areaId = ArrayList<String>()
    private var provinceList = arrayListOf(BaseInfoBean("请选择", "0"), BaseInfoBean("江苏省", "8ad881e94d74108d014d7414c3220004"))
    private val mPresenter by lazy { PerfectInfomationPresenter() }


    override fun layoutId(): Int {
        return R.layout.activity_perfect_information
    }

    override fun initData(savedInstanceState: Bundle?) {
        mPresenter.attachView(this)
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, getString(R.string.base_message))

    }

    override fun initView() {
        rl_sex.setOnClickListener(this)
        rl_card.setOnClickListener(this)
        rl_birth.setOnClickListener(this)
        rl_political.setOnClickListener(this)
        rl_country.setOnClickListener(this)
        rl_nation.setOnClickListener(this)
        rl_education.setOnClickListener(this)
        rl_organization.setOnClickListener(this)
        rl_personal_skills.setOnClickListener(this)
        rl_volunteer_type.setOnClickListener(this)
        rl_intentional_service.setOnClickListener(this)
        rl_intentional_service_object.setOnClickListener(this)
        rl_intentional_service_areas.setOnClickListener(this)
        tv_sure.setOnClickListener(this)
        rl_province.setOnClickListener(this)
        rl_city.setOnClickListener(this)
        rl_district.setOnClickListener(this)
        rl_street.setOnClickListener(this)
        rl_community.setOnClickListener(this)
        et_birth_data.text = FormatUtils.getTime(Date(System.currentTimeMillis()), "yyyy-MM-dd")
        map.put("birthday", et_birth_data.text.toString())
    }

    override fun start() {
        if (!StringUtils.isEmpty(SPUtil.get(this, "userId", "").toString())) {
            mPresenter.getUserInfo(SPUtil.get(this, "userId", "").toString())
            map.put("lastareaid", provinceList[1]!!.id!!)
            mPresenter.getArea(provinceList[1]!!.id!!, "province")
            tv_province.text = provinceList[1]!!.c_name!!
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rl_sex -> {
                var sexList = arrayListOf(BaseInfoBean("男", "1"), BaseInfoBean("女", "2"))
                BottomListDialog().setBaseInfoList(sexList).setOnItemClickListener(object : BottomListDialog.OnItemClickListener {
                    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long, selectItem: String, itemList: ArrayList<String>) {
                        et_sex.text = selectItem
                        map.put("sex", sexList[position].id.toString())
                    }

                }).show(supportFragmentManager, "dialog")
            }
            R.id.rl_card -> {
                getBaseInfoList("ZJLX")?.let {
                    BottomListDialog().setBaseInfoList(it).setOnItemClickListener(object : BottomListDialog.OnItemClickListener {
                        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long, selectItem: String, itemList: ArrayList<String>) {
                            et_document_type.text = selectItem
                            map.put("certificates_type", it[position].id.toString())
                        }

                    }).show(supportFragmentManager, "dialog")
                }

            }
            R.id.rl_birth -> {
                if (!StringUtils.isEmpty(et_document_number.text.toString())) {
                    et_birth_data.text = IDCardValidate(et_document_number.text.toString()).year.toString() + "-" + IDCardValidate(et_document_number.text.toString()).month.toString() + "-" + IDCardValidate(et_document_number.text.toString()).day.toString()
                }
                //时间选择器
                var pvTime = TimePickerBuilder(this, OnTimeSelectListener { date, v ->
                    et_birth_data.text = FormatUtils.getTime(date, "yyyy-MM-dd")
                    map.put("birthday", FormatUtils.getTime(date, "yyyy-MM-dd"))
                })
                        .setTitleText(getString(R.string.birth_data))
                        .setCancelText(getString(R.string.cancel))
                        .setSubmitText(getString(R.string.sure))
                        .setType(booleanArrayOf(true, true, true, false, false, false))
                        .setDate(if (StringUtils.isEmpty(et_birth_data.text.toString())) FormatUtils.getCurTime() else FormatUtils.getSelectCalendar(et_birth_data.text.toString()))
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
            R.id.rl_political -> {
                getBaseInfoList("ZZMM")?.let {
                    BottomListDialog().setBaseInfoList(it).setOnItemClickListener(object : BottomListDialog.OnItemClickListener {
                        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long, selectItem: String, itemList: ArrayList<String>) {
                            et_political_appearance.text = selectItem
                            map.put("political", it[position].id.toString())
                        }

                    }).show(supportFragmentManager, "dialog")
                }
            }
            R.id.rl_country -> {
                getBaseInfoList("GJ")?.let {
                    BottomListDialog().setBaseInfoList(it).setOnItemClickListener(object : BottomListDialog.OnItemClickListener {
                        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long, selectItem: String, itemList: ArrayList<String>) {
                            et_country.text = selectItem
                            map.put("country", it[position].id.toString())
                        }

                    }).show(supportFragmentManager, "dialog")
                }
            }
            R.id.rl_nation -> {
                getBaseInfoList("MZ")?.let {
                    BottomListDialog().setBaseInfoList(it).setOnItemClickListener(object : BottomListDialog.OnItemClickListener {
                        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long, selectItem: String, itemList: ArrayList<String>) {
                            et_nation.text = selectItem
                            map.put("ethnic", it[position].id.toString())
                        }

                    }).show(supportFragmentManager, "dialog")
                }
            }
            R.id.rl_education -> {
                getBaseInfoList("WHCD")?.let {
                    BottomListDialog().setBaseInfoList(it).setOnItemClickListener(object : BottomListDialog.OnItemClickListener {
                        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long, selectItem: String, itemList: ArrayList<String>) {
                            et_degree_of_education.text = selectItem
                            map.put("edu", it[position].id.toString())
                        }

                    }).show(supportFragmentManager, "dialog")
                }
            }
            R.id.rl_personal_skills -> {
                getBaseInfoList("GRJN")?.let {
                    SelectDialog.Builder(this)
                            .setTitle(getString(R.string.personal_skills))
                            .setItemList(it)
                            .setItemSelectList(map["skills"])
                            .setPositiveButton(getString(R.string.cancel), DialogInterface.OnClickListener { p0, p1 ->
                                p0?.dismiss()

                            })
                            .setNegativeButton(getString(R.string.sure), object : SelectDialog.onCheckBackListener {
                                override fun onChecked(dialog: DialogInterface, idList: ArrayList<String>?, nameList: ArrayList<String>?) {
                                    dialog.dismiss()
                                    L.d("BaseInfoBean:" + idList.toString())
                                    L.d("BaseInfoBean:" + nameList.toString())
                                    et_personal_skills.text = StringUtils.stringSpell(nameList)
                                    map.put("skills", StringUtils.stringSpell(idList).toString())
                                }

                            })
                            .setWith(0.77f)
                            .create()
                            .show()
                }
            }
            R.id.rl_volunteer_type -> {
                getBaseInfoList("ZYZLB")?.let {
                    SelectDialog.Builder(this)
                            .setTitle(getString(R.string.volunteer_type))
                            .setItemList(it)
                            .setItemSelectList(map["zyztype"])
                            .setPositiveButton(getString(R.string.cancel), DialogInterface.OnClickListener { p0, p1 ->
                                p0?.dismiss()

                            })
                            .setNegativeButton(getString(R.string.sure), object : SelectDialog.onCheckBackListener {
                                override fun onChecked(dialog: DialogInterface, idList: ArrayList<String>?, nameList: ArrayList<String>?) {
                                    dialog.dismiss()
                                    L.d("BaseInfoBean:" + idList.toString())
                                    et_volunteer_type.text = StringUtils.stringSpell(nameList)
                                    map.put("zyztype", StringUtils.stringSpell(idList).toString())
                                }

                            })
                            .setWith(0.77f)
                            .create()
                            .show()
                }
            }
            R.id.rl_intentional_service -> {
                getBaseInfoList("YXFWSJ")?.let {
                    SelectDialog.Builder(this)
                            .setTitle(getString(R.string.intentional_service_time))
                            .setItemList(it)
                            .setItemSelectList(map["servicetime"])
                            .setPositiveButton(getString(R.string.cancel), DialogInterface.OnClickListener { p0, p1 ->
                                p0?.dismiss()

                            })
                            .setNegativeButton(getString(R.string.sure), object : SelectDialog.onCheckBackListener {
                                override fun onChecked(dialog: DialogInterface, idList: ArrayList<String>?, nameList: ArrayList<String>?) {
                                    dialog.dismiss()
                                    L.d("BaseInfoBean:" + idList.toString())
                                    et_intentional_service_time.text = StringUtils.stringSpell(nameList)
                                    map.put("servicetime", StringUtils.stringSpell(idList).toString())
                                }

                            })
                            .setWith(0.77f)
                            .create()
                            .show()
                }
            }
            R.id.rl_intentional_service_object -> {
                getBaseInfoList("FWDX")?.let {
                    SelectDialog.Builder(this)
                            .setTitle(getString(R.string.intentional_service_object))
                            .setItemList(it)
                            .setItemSelectList(map["serviceobj"])
                            .setPositiveButton(getString(R.string.cancel), DialogInterface.OnClickListener { p0, p1 ->
                                p0?.dismiss()

                            })
                            .setNegativeButton(getString(R.string.sure), object : SelectDialog.onCheckBackListener {
                                override fun onChecked(dialog: DialogInterface, idList: ArrayList<String>?, nameList: ArrayList<String>?) {
                                    dialog.dismiss()
                                    L.d("BaseInfoBean:" + idList.toString())
                                    et_intentional_service_object.text = StringUtils.stringSpell(nameList)
                                    map.put("serviceobj", StringUtils.stringSpell(idList).toString())
                                }

                            })
                            .setWith(0.77f)
                            .create()
                            .show()
                }
            }
            R.id.rl_intentional_service_areas -> {
                getBaseInfoList("FWLY")?.let {
                    SelectDialog.Builder(this)
                            .setTitle(getString(R.string.intentional_service_areas))
                            .setItemList(it)
                            .setItemSelectList(map["servicearea"])
                            .setPositiveButton(getString(R.string.cancel), DialogInterface.OnClickListener { p0, p1 ->
                                p0?.dismiss()

                            })
                            .setNegativeButton(getString(R.string.sure), object : SelectDialog.onCheckBackListener {
                                override fun onChecked(dialog: DialogInterface, idList: ArrayList<String>?, nameList: ArrayList<String>?) {
                                    dialog.dismiss()
                                    L.d("BaseInfoBean:" + idList.toString())
                                    et_intentional_service_areas.text = StringUtils.stringSpell(nameList)
                                    map.put("servicearea", StringUtils.stringSpell(idList).toString())
                                }

                            })
                            .setWith(0.77f)
                            .create()
                            .show()
                }
            }
            R.id.rl_province -> {

                BottomListDialog().setBaseInfoList(provinceList).setOnItemClickListener(object : BottomListDialog.OnItemClickListener {
                    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long, selectItem: String, itemList: ArrayList<String>) {
                        tv_province.text = selectItem
                        tv_city.text = getString(R.string.please_choose)
                        tv_district.text = getString(R.string.please_choose)
                        tv_street.text = getString(R.string.please_choose)
                        tv_community.text = getString(R.string.please_choose)
                        if (provinceList[position].id != "0") {
                            map.put("lastareaid", provinceList[position]!!.id!!)
                            mPresenter.getArea(provinceList[position]!!.id!!, "province")
                        } else {
                            map.remove("lastareaid")
                            cityList.clear()
                            districtList.clear()
                            streetList.clear()
                            communityList.clear()
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
                            if (cityList[position].id != "0") {
                                map.put("lastareaid", cityList[position].id)
                                mPresenter.getArea(cityList[position].id, "city")
                            } else {
                                map.remove("lastareaid")
                                districtList.clear()
                                streetList.clear()
                                communityList.clear()
                            }
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
                            if (districtList[position].id != "0") {
                                map.put("lastareaid", districtList[position].id)
                                mPresenter.getArea(districtList[position].id, "district")
                            } else {
                                map.remove("lastareaid")
                                streetList.clear()
                                communityList.clear()
                            }
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
                            if (streetList[position].id != "0") {
                                map.put("lastareaid", streetList[position].id)
                                mPresenter.getArea(streetList[position].id, "street")
                            } else {
                                map.remove("lastareaid")
                                communityList.clear()
                            }

                        }

                    }).show(supportFragmentManager, "dialog")
                }
            }
            R.id.rl_community -> {
                if (communityList.size > 0) {
                    BottomListDialog().setAreaInfoList(communityList).setOnItemClickListener(object : BottomListDialog.OnItemClickListener {
                        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long, selectItem: String, itemList: ArrayList<String>) {
                            tv_community.text = selectItem
                            if (communityList[position].id != "0") {
                                map.put("lastareaid", communityList[position].id)
                            } else {
                                map.remove("lastareaid")
                            }

                        }

                    }).show(supportFragmentManager, "dialog")
                }
            }
            R.id.tv_sure -> {
                if (StringUtils.isEmptyToast(this, et_input_name.text.toString(), getString(R.string.name) + getString(R.string.is_null))) {
                } else if (StringUtils.isEmptyToast(this, map["sex"], getString(R.string.sex) + getString(R.string.is_null))) {
                } else if (StringUtils.isEmptyToast(this, map["certificates_type"], getString(R.string.document_type) + getString(R.string.is_null))) {
                } else if (StringUtils.isEmptyToast(this, et_document_number.text.toString(), getString(R.string.document_number) + getString(R.string.is_null))) {
                } else if (StringUtils.isEmptyToast(this, et_tel_number.text.toString(), getString(R.string.tel_number) + getString(R.string.is_null))) {
                } else if (IDCardValidate(et_document_number.text.toString()).isCorrect != 0) {
                    ToastUtils.showShort(this, getString(R.string.card_true))
                } else if (StringUtils.isEmptyToast(this, map["birthday"], getString(R.string.birth_data) + getString(R.string.is_null))) {
                } else if (StringUtils.isEmptyToast(this, map["political"], getString(R.string.political_appearance) + getString(R.string.is_null))) {
                } else if (StringUtils.isEmptyToast(this, map["country"], getString(R.string.country) + getString(R.string.is_null))) {
                } else if (StringUtils.isEmptyToast(this, map["ethnic"], getString(R.string.nation) + getString(R.string.is_null))) {
                } else if (StringUtils.isEmptyToast(this, map["edu"], getString(R.string.degree_of_education) + getString(R.string.is_null))) {
                } else if (StringUtils.isEmptyToast(this, map["skills"], getString(R.string.personal_skills) + getString(R.string.is_null))) {
                } else if (StringUtils.isEmptyToast(this, map["zyztype"], getString(R.string.volunteer_type) + getString(R.string.is_null))) {
                } else if (StringUtils.isEmptyToast(this, map["servicetime"], getString(R.string.intentional_service_time) + getString(R.string.is_null))) {
                } else if (StringUtils.isEmptyToast(this, map["serviceobj"], getString(R.string.intentional_service_object) + getString(R.string.is_null))) {
                } else if (StringUtils.isEmptyToast(this, map["servicearea"], getString(R.string.intentional_service_areas) + getString(R.string.is_null))) {
                } else if (StringUtils.isEmptyToast(this, map["lastareaid"], getString(R.string.org_location) + getString(R.string.is_null))) {
                } else {
                    map.put("name", et_input_name.text.toString())
                    map.put("userid", SPUtil.get(this, "userId", "").toString())
                    map.put("certificates_number", et_document_number.text.toString())
                    map.put("phone", et_tel_number.text.toString())
                    mPresenter.doPerfectInfo(map)
                }
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
        EventBusUtils.post(EventBusMessage(Const.PERFECT_INFO_SUCCESS, ""))
        finish()
    }

    override fun areaResult(data: List<AreaBean>, type: String) {
        when (type) {
            "province" -> {
                cityList.clear()
                cityList.addAll(data)
                cityList.add(0, AreaBean("0", 0, "请选择"))
            }
            "city" -> {
                districtList.clear()
                districtList.addAll(data)
                districtList.add(0, AreaBean("0", 0, "请选择"))
            }
            "district" -> {
                streetList.clear()
                streetList.addAll(data)
                streetList.add(0, AreaBean("0", 0, "请选择"))
            }
            "street" -> {
                communityList.clear()
                communityList.addAll(data)
                communityList.add(0, AreaBean("0", 0, "请选择"))
            }

        }
    }

    override fun showError(errorMsg: String, errorCode: Int) {
        ToastUtils.showShort(this, errorMsg)
    }

    override fun userInfoResult(data: UserInfo) {
        if (!StringUtils.isEmpty(data.name)) {
            et_input_name.setText(data.name)
        }
        if (!StringUtils.isEmpty(data.sex_cn)) {
            et_sex.text = data.sex_cn
        }
        if (!StringUtils.isEmpty(data.certificates_type_cn)) {
            et_document_type.text = data.certificates_type_cn
        }
        if (!StringUtils.isEmpty(data.certificates_number)) {
            et_document_number.setText(data.certificates_number)
            et_document_number.isFocusable = false
            et_document_number.isClickable = false
        }
        if (!StringUtils.isEmpty(data.birthday)) {
            et_birth_data.text = data.birthday
        }
        if (!StringUtils.isEmpty(data.political_cn)) {
            et_political_appearance.text = data.political_cn
        }
        if (!StringUtils.isEmpty(data.country_cn)) {
            et_country.text = data.country_cn
        }
        if (!StringUtils.isEmpty(data.ethnic_cn)) {
            et_nation.text = data.ethnic_cn
        }
        if (!StringUtils.isEmpty(data.edu_cn)) {
            et_degree_of_education.text = data.edu_cn
        }
        if (!StringUtils.isEmpty(data.phone)) {
            et_tel_number.setText(data.phone)
        }
        if (!StringUtils.isEmpty(data.skills_cn)) {
            et_personal_skills.text = data.skills_cn
        }
        if (!StringUtils.isEmpty(data.zyztype_cn)) {
            et_volunteer_type.text = data.zyztype_cn
        }
        if (!StringUtils.isEmpty(data.servicetime_cn)) {
            et_intentional_service_time.text = data.servicetime_cn
        }
        if (!StringUtils.isEmpty(data.serviceobj_cn)) {
            et_intentional_service_object.text = data.serviceobj_cn
        }
        if (!StringUtils.isEmpty(data.servicearea_cn)) {
            et_intentional_service_areas.text = data.servicearea_cn
        }
        data.area_list?.province?.name?.let {
            tv_province.text = it
            mPresenter.getArea(data.area_list.province.id, "province")
        }
        data.area_list?.city?.name?.let {
            tv_city.text = it
            mPresenter.getArea(data.area_list.city.id, "city")
        }
        data.area_list?.region?.name?.let {
            tv_district.text = it
            mPresenter.getArea(data.area_list.region.id, "district")
        }
        data.area_list?.street?.name?.let {
            tv_street.text = it
            mPresenter.getArea(data.area_list.street.id, "street")
        }
        data.area_list?.community?.name?.let {
            tv_community.text = it
        }
        map.put("sex", data.sex)
        map.put("certificates_type", data.Certificates_type)
        map.put("birthday", data.birthday)
        map.put("political", data.political)
        map.put("country", data.country)
        map.put("ethnic", data.ethnic)
        map.put("edu", data.edu)
        map.put("skills", data.skills)
        map.put("zyztype", data.zyztype)
        map.put("servicetime", data.servicetime)
        map.put("serviceobj", data.serviceobj)
        map.put("servicearea", data.servicearea)
        map.put("lastareaid", data.lastareaid)
        map.put("name", data.name)
        map.put("userid", SPUtil.get(this, "userId", "").toString())
        map.put("certificates_number", data.certificates_number)
        map.put("phone", data.phone)

    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}