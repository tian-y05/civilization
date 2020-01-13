package com.wmsj.baselibs.bean

/**
 * Created by tian
on 2019/8/16.
 */
data class ActivityShowBean(
    val activity_contacts: String,
    val activity_images: List<String>,
    val activity_introduce: String,
    val activity_logo: String,
    val activity_name: String,
    val activity_object: String,
    val activity_object_id: String,
    val activity_pace: String,
    val activity_range: Int,
    val activity_type: String,
    val activity_type_id: String,
    val area1: String,
    val area2: String,
    val area3: String,
    val area4: Any,
    val area5: Any,
    val bottominfo: String,
    val click: Int,
    val codeid: Any,
    val createtime: String,
    val day_endtime: String,
    val day_starttime: String,
    val end_time: String,
    val id: String,
    val img_count: Int,
    val in_num: String,
    val oid: String,
    val org_name: String,
    val pareme1: String,
    val pareme2: Any,
    val pareme3: Any,
    val publicity_time: String,
    val qrcode: String,
    val reason: Any,
    val recruit_condition: String,
    val recruit_endtime: String,
    val recruit_num: Int,
    val recruit_starttime: String,
    val recruitment: Int,
    val region_id: String,
    val scope: String,
    val scope_cn: String,
    val service_time: String,
    val sign_num: Int,
    val sign_up: Int,
    val start_time: String,
    val statu: Int,
    val status: Int,
    val tel: String,
    val time_noter: String,
    val topinfo: String,
    val report_num: String,
    val report_id: String,
    val total_time: Any,
    val jwd: String
) {
    override fun toString(): String {
        return "ActivityShowBean(activity_contacts='$activity_contacts', activity_images=$activity_images, activity_introduce='$activity_introduce', activity_logo='$activity_logo', activity_name='$activity_name', activity_object='$activity_object', activity_pace='$activity_pace', activity_range=$activity_range, activity_type='$activity_type', area1='$area1', area2='$area2', area3='$area3', area4=$area4, area5=$area5, bottominfo='$bottominfo', click=$click, codeid=$codeid, createtime='$createtime', day_endtime='$day_endtime', day_starttime='$day_starttime', end_time='$end_time', id='$id', img_count=$img_count, in_num='$in_num', oid='$oid', org_name='$org_name', pareme1='$pareme1', pareme2=$pareme2, pareme3=$pareme3, publicity_time='$publicity_time', qrcode='$qrcode', reason=$reason, recruit_condition='$recruit_condition', recruit_endtime='$recruit_endtime', recruit_num=$recruit_num, recruit_starttime='$recruit_starttime', recruitment=$recruitment, region_id='$region_id', scope='$scope', service_time=$service_time, sign_num=$sign_num, sign_up=$sign_up, start_time='$start_time', statu=$statu, status=$status, tel='$tel', time_noter='$time_noter', topinfo='$topinfo', report_num='$report_num', report_id='$report_id', total_time=$total_time)"
    }
}