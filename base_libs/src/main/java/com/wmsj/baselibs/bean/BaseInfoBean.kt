package com.wmsj.baselibs.bean

/**
 * Created by tian
on 2019/8/20.
 */
class BaseInfoBean {
    var c_name: String? = null
    var id: String? = null

    constructor(c_name: String?, id: String?) {
        this.c_name = c_name
        this.id = id
    }

    override fun toString(): String {
        return "BaseInfoBean(c_name='$c_name', id='$id')"

    }
}