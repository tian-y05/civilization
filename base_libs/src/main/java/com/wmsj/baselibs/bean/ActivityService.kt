package com.wmsj.baselibs.bean

/**
 * Created by tian
on 2019/8/23.
 */
data class ActivityService(
        val alltime: Int,
        val list: List<ServiceList>,
        val user_logo: String,
        val user_name: String
)

data class ServiceList(
        val clock: String,
        val clock_type: String,
        val date: String,
        val publicity: String,
        val publicity_type: Int,
        val service_end: String,
        val service_start: String,
        val service_state: String,
        val service_time: Int
)