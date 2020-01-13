package com.wmsj.baselibs.interfaces

import android.view.View

/**
 * Created by tian
on 2019/7/29.
 */
interface AdapterCallBack {

    fun onItemCheckedChangeListerner(view: View, position: Int,isChecked : Boolean)

    fun onItemClickListerner(view: View, position: Int)
}