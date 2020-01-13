package com.wmsj.baselibs.bean

/**
 * Created by tian
on 2019/8/17.
 */
data class RegistBean(
    val country: String,
    val ethnic: String,
    val phone: String,
    val userid: String
) {
    override fun toString(): String {
        return "RegistBean(country='$country', ethnic='$ethnic', phone='$phone', userid='$userid')"
    }
}