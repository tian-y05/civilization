package com.wmsj.baselibs.ui.adapter

import android.content.Context
import android.view.ViewGroup
import com.wmsj.baselibs.R
import com.wmsj.baselibs.bean.SystemNewsBean
import com.wmsj.baselibs.recyclerview.RecyclerViewHolder
import com.wmsj.baselibs.recyclerview.adapter.CommonRecyclerAdapter

/**
 * 首页最新活动adapter
on 2019/8/6.
 */
class SystemMessageListAdapter(mContext: Context, record: ArrayList<SystemNewsBean>, layoutId: Int) : CommonRecyclerAdapter<SystemNewsBean>(mContext, record, layoutId) {


    companion object {
        private const val ITEM_TYPE_NORMAL = 1
        private const val ITEM_TYPE_EMPTY = 2
    }

    override fun bindData(holder: RecyclerViewHolder, data: SystemNewsBean, position: Int) {
        when (getItemViewType(position)) {
            ITEM_TYPE_NORMAL -> {
                holder.setText(R.id.tv_time, data.send_time)
                holder.setText(R.id.tv_content, data.content)
                holder.setText(R.id.tv_type, data.news_type)

            }
        }


    }

    override fun getItemViewType(position: Int): Int {
        return if (mData.size == 0)
            ITEM_TYPE_EMPTY
        else
            ITEM_TYPE_NORMAL
    }

    override fun getItemCount(): Int {
        return if (mData.size == 0)
            1
        else
            mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return when (viewType) {
            ITEM_TYPE_EMPTY ->
                RecyclerViewHolder(inflaterView(R.layout.empty_view, parent))
            else ->
                RecyclerViewHolder(inflaterView(R.layout.system_message_item, parent))
        }

    }

}