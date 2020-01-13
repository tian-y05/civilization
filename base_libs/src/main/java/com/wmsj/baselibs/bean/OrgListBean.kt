package com.wmsj.baselibs.bean

/**
 * Created by tian
on 2019/8/20.
 */
class OrgListBean {
    var org_cname: String? = null
    var id: String? = null

    constructor(org_cname: String?, id: String?) {
        this.org_cname = org_cname
        this.id = id
    }

    override fun toString(): String {
        return "BaseInfoBean(c_name='$org_cname', id='$id')"

    }
}