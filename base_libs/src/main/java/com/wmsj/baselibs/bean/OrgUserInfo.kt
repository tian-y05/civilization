package com.wmsj.baselibs.bean

/**
 * Created by tian
on 2019/9/4.
 */
data class OrgUserInfo(
    val Certificates: String,
    val all: All,
    val area_list: String,
    val birthday: String,
    val certificates_number: String,
    val edu: String,
    val ethnic: String,
    val jinqi_time: String,
    val name: String,
    val onemonth: Onemonth,
    val oneyear: Oneyear,
    val org_count: String,
    val org_time: String,
    val phone: String,
    val pic: String,
    val political: String,
    val service_time: Int,
    val servicearea: String,
    val serviceobj: String,
    val servicetime: String,
    val sex: Int,
    val skills: String,
    val threeyear: Threeyear,
    val zyzcode: String,
    val zyztype: String
)

data class Oneyear(
    val join: Int,
    val servicetime: Int
)

data class All(
    val join: Int,
    val servicetime: Int
)

data class Onemonth(
    val join: Int,
    val servicetime: Int
)

data class Threeyear(
    val join: Int,
    val servicetime: Int
)