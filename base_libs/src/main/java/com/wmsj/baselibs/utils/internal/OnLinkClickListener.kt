package com.xbrc.iou.views.linktextview.internal

import android.view.View


interface OnLinkClickListener {
    /**
     * @param view    the view been clicked ,aka the TextView
     * @param content the content which is clicked
     */
    fun onClick(view: View, content: String)
}