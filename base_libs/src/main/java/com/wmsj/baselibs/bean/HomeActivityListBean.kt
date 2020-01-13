package com.wmsj.baselibs.bean

/**
 * Created by tian
on 2019/8/6.
 */
data class HomeActivityListBean(
    val activity_images: List<Any>,
    val activity_logo: String,
    val activity_name: String,
    val activity_object: String,
    val activity_pace: String,
    val aid: String,
    val end_time: String,
    val oid: String,
    val id: String,
    val org_cname: String,
    val progress: String,
    val progress_type: Int,
    val recruit_endtime: String,
    val recruit_starttime: String,
    val recruitment: Int,
    val sign_num: Int,
    val start_time: String
)