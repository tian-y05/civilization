package com.wmsj.baselibs.bean

/**
 * Created by tian
on 2019/8/20.
 */
data class UserInfo(
    val Certificates_type: String,
    val age: Int,
    val area1: String,
    val area2: String,
    val area3: String,
    val area4: String,
    val area5: String,
    val area6: String,
    val area_cn: String,
    val area_list: AreaList,
    val birthday: String,
    val certificates_number: String,
    val certificates_number1: String,
    val certificates_type_cn: String,
    val completion: Int,
    val country: String,
    val country_cn: String,
    val createtime: String,
    val edu: String,
    val edu_cn: String,
    val email: Any,
    val ethnic: String,
    val ethnic_cn: String,
    val fwzqrcode: String,
    val graduate: Any,
    val id: String,
    val job_content: Any,
    val job_title: Any,
    val lastareaid: String,
    val logo: Any,
    val name: String,
    val native_place: Any,
    val parame1: String,
    val parame2: Any,
    val parame3: Any,
    val phone: String,
    val political: String,
    val political_cn: String,
    val profession: Any,
    val qrcode: String,
    val service_count: String,
    val servicearea: String,
    val servicearea_cn: String,
    val serviceobj: String,
    val serviceobj_cn: String,
    val servicetime: String,
    val servicetime_cn: String,
    val sex: String,
    val sex_cn: String,
    val skills: String,
    val skills_cn: String,
    val state: Int,
    val unit: Any,
    val unit_address: Any,
    val updatetime: String,
    val user_id: String,
    val volunteer_number: String,
    val zipcode: Any,
    val zyztype: String,
    val zyztype_cn: String
)

data class AreaList(
    val city: City,
    val community: Community,
    val province: Province,
    val region: Region,
    val street: Street
)

data class Community(
    val id: String,
    val level: String,
    val name: String
)

data class Street(
    val id: String,
    val level: String,
    val name: String
)

data class Province(
    val id: String,
    val level: String,
    val name: String
)

data class Region(
    val id: String,
    val level: String,
    val name: String
)

data class City(
    val id: String,
    val level: String,
    val name: String
)