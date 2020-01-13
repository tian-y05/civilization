package com.wmsj.baselibs.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle

/**
 * Created by tian
on 2019/7/18.
 */
class IntentUtils {
    companion object {
        fun to(context: Context, cls: Class<*>) {
            to(context, cls, null)
        }

        fun to(context: Context, cls: Class<*>, data: Bundle?) {
            val intent = Intent(context, cls)
            if (data != null) {
                intent.putExtras(data)
            }
            context.startActivity(intent)

        }

        fun toforresult(context: Context, cls: Class<*>, requestCode: Int) {
            toforresult(context, cls, null, requestCode)
        }

        fun toforresult(context: Context, cls: Class<*>, data: Bundle?, requestCode: Int) {
            val intent = Intent(context, cls)
            if (data != null) {
                intent.putExtras(data)
            }
            (context as Activity).startActivityForResult(intent, requestCode)
        }
    }
}