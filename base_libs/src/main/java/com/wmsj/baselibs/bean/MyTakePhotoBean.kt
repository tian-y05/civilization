package com.wmsj.baselibs.bean

/**
 * Created by tian
on 2019/8/26.
 */
data class MyTakePhotoBean(
    val c_id: Int,
    val click: Int,
    val cul_address: String,
    val cul_city: String,
    val cul_content: String,
    val cul_createtime: String,
    val cul_gps: String,
    val cul_images: List<String>,
    val cul_pushtime: Any,
    val cul_role: String,
    val cul_state: Int,
    val cul_type: String,
    val cul_userid: String,
    val cul_video: List<String>,
    val id: Int,
    val p_id: Int,
    val user_logo: String,
    val username: String,
    val cover: String
)