package com.wmsj.baselibs.bean

/**
 * Created by tian
on 2019/9/3.
 */
data class ActsPerson(
        val list: List<Member>,
        val recruitment: Int
)

data class Member(
        val aid: String,
        val clock_type: String,
        val duration: String,
        val end_time: String,
        val id: String,
        val logo: String,
        val name: String,
        val place: String,
        val registertime: String,
        val service_time: String,
        val sex: Int,
        val start_time: String,
        val userid: String,
        var isSelect: Boolean
)