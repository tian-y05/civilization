package com.wmsj.baselibs.bean

/**
 * Created by tian
on 2019/8/6.
 */
class HomeColumnsBean {
    var app_createtime: String? = null
    var app_desc: String? = null
    var app_id: Int = 0
    var app_logo: String? = null
    var app_name: String? = null //应用名称
    var app_package: String? = null
    var app_status: Int = 0
    var app_type: Int = 0
    var app_updatetime: String? = null
    var app_version: String? = null
    var c_id: Int = 0
    var col_createtime: String? = null
    var col_id: Int = 0
    var col_img: String? = null
    var col_name: String? = null
    var col_updatetime: String? = null
    var id: Int = 0
    var logo: String? = null
    var name: String? = null //栏目名称
    var p_id: Int = 0

    constructor(logo: String, name: String, app_package: String) {
        this.app_logo = logo
        this.app_name = name
        this.app_package = app_package
    }
}

