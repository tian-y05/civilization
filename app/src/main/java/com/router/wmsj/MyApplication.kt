package com.router.wmsj

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import android.text.TextUtils
import com.wmsj.baselibs.BaseApplication
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException


/**
 * Created by tian
 * on 2019/7/12.
 */

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
      
        BaseApplication.init(this)

    }


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)

    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private fun getProcessName(pid: Int): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName = reader!!.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim({ it <= ' ' })
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                if (reader != null) {
                    reader!!.close()
                }
            } catch (exception: IOException) {
                exception.printStackTrace()
            }

        }
        return null
    }
}
