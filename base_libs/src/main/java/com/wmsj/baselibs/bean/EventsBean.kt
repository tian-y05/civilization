package com.wmsj.baselibs.bean

/**
 * 活动管理bean
on 2019/8/5.
 */
data class EventsBean(
    val activitytime: String,
    val id: String,
    val name: String,
    val needcheck: Int,
    val orgname: String,
    val participate: Int,
    val picnum: String,
    val place: String,
    val qrcode: String,
    val reason: String,
    val recruited: Int,
    val recruitment: Int,
    val report_num: Int,
    val statu: Int
)