package com.wmsj.baselibs.bean

/**
 * Created by tian
on 2019/8/19.
 */
data class IdentityInfoBean(
        val area: String,
        val certificates_number: String,
        val id: String,
        val name: String,
        val phone: String,
        val sex: String,
        val user_id: String,
        val volunteer_number: String
) {
    override fun toString(): String {
        return "IdentityInfoBean(area='$area', certificates_number='$certificates_number', id='$id', name='$name', phone='$phone', sex='$sex', user_id='$user_id', volunteer_number='$volunteer_number')"
    }
}