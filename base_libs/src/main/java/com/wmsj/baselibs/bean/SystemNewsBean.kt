package com.wmsj.baselibs.bean

/**
 * Created by tian
on 2019/8/26.
 */
data class SystemNewsBean(
    val content: String,
    val news_type: String,
    val send_time: String
) {
    override fun toString(): String {
        return "SystemNewsBean(content='$content', news_type='$news_type', send_time='$send_time')"
    }
}