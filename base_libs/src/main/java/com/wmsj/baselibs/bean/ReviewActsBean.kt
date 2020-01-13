package com.wmsj.baselibs.bean

import java.io.Serializable

/**
 * Created by tian
on 2019/8/30.
 */
data class ReviewActsBean(
        val data: Map<String, String>,
        val zgorgname: String,
        val activity_type: String,
        val activity_object: String
) : Serializable