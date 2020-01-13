package com.wmsj.baselibs.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ListView

/**
 * 最大高度listview
 *
 * on 2018/11/12.
 */

class MaxHeightListView : ListView {


    var maxHeight = 400

    constructor(context: Context) : super(context) {}


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}


    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        setViewHeightBasedOnChildren()
    }


    fun setViewHeightBasedOnChildren() {
        val listAdapter = this.adapter ?: return
// int h = 10;
        // int itemHeight = BdUtilHelper.getDimens(getContext(), R.dimen.ds30);
        var sumHeight = 0
        val size = listAdapter.count


        for (i in 0 until size) {
            val v = listAdapter.getView(i, null, this)
            v.measure(0, 0)
            sumHeight += v.measuredHeight


        }


        if (sumHeight > maxHeight) {
            sumHeight = maxHeight
        }
        val params = this.layoutParams
        // this.getLayoutParams();
        params.height = sumHeight


        this.layoutParams = params
    }


}
