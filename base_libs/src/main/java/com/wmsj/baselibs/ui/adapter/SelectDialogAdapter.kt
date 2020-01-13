package com.wmsj.baselibs.ui.adapter

import android.content.Context
import android.widget.CheckBox
import com.wmsj.baselibs.R
import com.wmsj.baselibs.bean.BaseInfoBean
import com.wmsj.baselibs.recyclerview.RecyclerViewHolder
import com.wmsj.baselibs.recyclerview.adapter.CommonRecyclerAdapter
import com.wmsj.baselibs.utils.L
import java.util.*

/**
 * Created by tian
on 2019/8/7.
 */
class SelectDialogAdapter(mContext: Context, lists: ArrayList<BaseInfoBean>, layoutId: Int) : CommonRecyclerAdapter<BaseInfoBean>(mContext, lists, layoutId) {

    private var itemNameList: String? = ""

    override fun bindData(holder: RecyclerViewHolder, data: BaseInfoBean, position: Int) {
        holder.setIsRecyclable(false)
        data.c_name?.let { holder.setText(R.id.tv_item, it) }
        holder.getView<CheckBox>(R.id.cb).setOnCheckedChangeListener { buttonView, isChecked ->
            L.d("SelectDialogAdapter:" + position + isChecked)
            onItemClick.onCheck(position, isChecked)
        }
        holder.getView<CheckBox>(R.id.cb).isChecked = (itemNameList != null && itemNameList!!.contains(data.id!!))
    }

    private lateinit var onItemClick: onItemCheckListener

    fun setOnItemCheck(onItemClick: onItemCheckListener) {
        this.onItemClick = onItemClick
    }

    interface onItemCheckListener {
        fun onCheck(position: Int, isChecked: Boolean)

    }

    fun setItemSelect(itemSelct: String) {
        itemNameList = itemSelct
    }
}