package com.wmsj.baselibs.retrofit.api

/**
 * Created by tian
on 2019/7/2.
 */
class ApiException : RuntimeException {

    private var code: Int? = null


    constructor(throwable: Throwable, code: Int) : super(throwable) {
        this.code = code
    }

    constructor(message: String) : super(Throwable(message))
}