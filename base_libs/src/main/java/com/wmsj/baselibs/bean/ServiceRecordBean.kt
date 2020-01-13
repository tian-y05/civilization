package com.wmsj.baselibs.bean

/**
 * Created by tian
on 2019/8/26.
 */
data class ServiceRecordBean(
        val list: List<Record>,
        val total: Int,
        val totalservertime: Int,
        val username: String
)

data class Record(
        val activity_name: String,
        val activity_pace: String,
        val id: String,
        val name: String,
        val orgname: String,
        val service_time: String,
        val start_time: String
)