package com.wmsj.baselibs.eventBus

/**
 * Created by tian
 * on 2019/7/1.
 */

class EventBusMessage<T> {

    var code: Int = 0
    var data: T? = null

    constructor(code: Int) {
        this.code = code
    }

    constructor(code: Int, data: T) {
        this.code = code
        this.data = data
    }

    override fun toString(): String {
        return "EventBusMessage{" +
                "code=" + code +
                ", data=" + data +
                '}'
    }
}

