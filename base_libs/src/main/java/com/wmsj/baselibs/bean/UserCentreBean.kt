package com.wmsj.baselibs.bean

/**
 * Created by tian
on 2019/8/20.
 */
data class UserCentreBean(
    val codeurl: String,
    val logo: String,
    val name: String,
    val service_count: String,
    val service_star: Int,
    val volunteer_number: String

) {
    override fun toString(): String {
        return "UserCentreBean(codeurl='$codeurl', logo='$logo', name='$name', service_count='$service_count', service_star=$service_star, volunteer_number='$volunteer_number')"
    }
}