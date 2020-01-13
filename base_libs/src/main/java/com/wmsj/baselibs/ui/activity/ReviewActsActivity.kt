package com.wmsj.baselibs.ui.activity

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.wmsj.baselibs.R
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.base.BaseActivity
import com.wmsj.baselibs.bean.ReviewActsBean
import kotlinx.android.synthetic.main.activity_events_details.*

/**
 * 预览活动
 */
class ReviewActsActivity : BaseActivity() {


    private val TAG = ReviewActsActivity::class.java.simpleName

    override fun layoutId(): Int {
        return R.layout.activity_events_details
    }

    override fun initData(savedInstanceState: Bundle?) {
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, getString(R.string.activity_detail))
        ll_bottom.visibility = View.GONE

    }

    override fun initView() {

        var data = intent.getSerializableExtra("data") as ReviewActsBean
        data?.let {
            var requestOptions = RequestOptions()
            requestOptions.error(R.mipmap.news_normal)
                    .placeholder(R.mipmap.news_normal)
                    .transforms(RoundedCorners(30))
            Glide.with(this).load(Const.BASE_URL + data.data["activity_logo"]).apply(requestOptions).into(iv_content)
            tv_title.text = data.data["activity_name"]
            tv_recruit_time.text = data.data["recruit_starttime"] + "至" + data.data["recruit_endtime"]
            tv_activity_time.text = data.data["start_time"] + "至" + data.data["end_time"]
            tv_activity_place.text = data.data["activity_pace"]
            tv_service_time.text = data.data["service_time"] + "小时"
            tv_service_type.text = data.activity_type
            tv_service_object.text = data.activity_object
            tv_initiating_organization.text = data.zgorgname
            tv_contacts_people.text = data.data["activity_contacts"]
            tv_contacts_phone.text = data.data["tel"]
            tv_content.text = "\u3000\u3000" + data.data["activity_introduce"]
        }
    }

    override fun start() {

    }
}