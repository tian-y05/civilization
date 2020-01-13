package com.wmsj.baselibs.bean

/**
 * Created by tian
on 2019/8/12.
 */
class ConventientPhotoBean {
    var c_id: Int = 0
    var click: Int = 0
    var cul_address: String? = null
    var cul_city: String? = null
    var cul_content: String? = null
    var cul_createtime: String? = null
    var cul_gps: String? = null
    var cul_images: String? = null
    var cul_pushtime: String? = null
    var cul_role: String? = null
    var cul_state: Int = 0
    var cul_type: String? = null
    var cul_userid: String? = null
    var cul_video: String? = null
    var id: Int = 0
    var p_id: Int = 0
    var user_logo: String? = null
    var username: String? = null
    var cover: String? = null

    constructor(c_id: Int) {
        this.c_id = c_id
    }
}