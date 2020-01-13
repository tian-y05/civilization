package com.wmsj.baselibs

import android.content.Context
import com.wmsj.baselibs.utils.DisplayManager

/**
 * @Created by TOME .
 * @时间 2018/5/14 17:40
 * @描述 ${应用的application}
 */

object BaseApplication {
    var appInstance: Context? = null
        private set

    fun init(context: Context) {
        appInstance = context
        DisplayManager.init(context)
    }


}
