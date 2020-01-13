package com.wmsj.baselibs.bean

/**
 * Created by tian
on 2019/9/3.
 */
data class OrgMemberBean(
        val id: String,
        val join: String,
        val logo: String,
        val name: String,
        val place: String,
        val sex: Int,
        var isSelect: Boolean
)