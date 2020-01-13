package com.wmsj.baselibs.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.wmsj.baselibs.R
import com.wmsj.baselibs.bean.Record
import com.wmsj.baselibs.recyclerview.RecyclerViewHolder
import com.wmsj.baselibs.recyclerview.adapter.CommonRecyclerAdapter

/**
 * 首页最新活动adapter
on 2019/8/6.
 */
class ServiceRecordListAdapter(mContext: Context, record: ArrayList<Record>, layoutId: Int) : CommonRecyclerAdapter<Record>(mContext, record, layoutId) {


    companion object {
        private const val ITEM_TYPE_NORMAL = 1
        private const val ITEM_TYPE_EMPTY = 2
    }

    override fun bindData(holder: RecyclerViewHolder, data: Record, position: Int) {
        when (getItemViewType(position)) {
            ITEM_TYPE_NORMAL -> {
                holder.setText(R.id.tv_title,data.activity_name)
                holder.setText(R.id.tv_area,data.activity_pace)
                holder.setText(R.id.tv_time,data.start_time)
                holder.setText(R.id.tv_long,data.service_time)
                if(position % 2 == 0){
                    holder.getView<TextView>(R.id.tv_title).setBackgroundColor(mContext.resources.getColor(R.color.white))
                    holder.getView<TextView>(R.id.tv_area).setBackgroundColor(mContext.resources.getColor(R.color.white))
                    holder.getView<TextView>(R.id.tv_time).setBackgroundColor(mContext.resources.getColor(R.color.white))
                    holder.getView<TextView>(R.id.tv_long).setBackgroundColor(mContext.resources.getColor(R.color.white))
                }else{
                    holder.getView<TextView>(R.id.tv_title).setBackgroundColor(mContext.resources.getColor(R.color.dialog_gray))
                    holder.getView<TextView>(R.id.tv_area).setBackgroundColor(mContext.resources.getColor(R.color.dialog_gray))
                    holder.getView<TextView>(R.id.tv_time).setBackgroundColor(mContext.resources.getColor(R.color.dialog_gray))
                    holder.getView<TextView>(R.id.tv_long).setBackgroundColor(mContext.resources.getColor(R.color.dialog_gray))
                }
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
                RecyclerViewHolder(inflaterView(R.layout.service_record_item, parent))
        }

    }

}