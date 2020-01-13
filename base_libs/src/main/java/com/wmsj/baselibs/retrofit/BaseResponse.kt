package com.wmsj.baselibs.retrofit

/**
 * Created by tian
    on 2019/7/2.
    返回的数据封装
 */
class BaseResponse<T>(val state :String,
                      val httpcode :String,
                      val message:String,
                      val data:T) {
    override fun toString(): String {
        return "BaseResponse(state='$state', message='$message', data=$data)"
    }
}