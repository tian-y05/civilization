package com.wmsj.baselibs.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.wmsj.baselibs.R
import com.wmsj.baselibs.bean.MyActivityBean
import com.wmsj.baselibs.recyclerview.RecyclerViewHolder
import com.wmsj.baselibs.recyclerview.adapter.CommonRecyclerAdapter

/**
 * 首页最新活动adapter
on 2019/8/6.
 */
class MyActivityListAdapter(mContext: Context, newsHomeActivityListBean: ArrayList<MyActivityBean>, layoutId: Int) : CommonRecyclerAdapter<MyActivityBean>(mContext, newsHomeActivityListBean, layoutId) {

    var type = -1


    companion object {
        private const val ITEM_TYPE_NORMAL = 1
        private const val ITEM_TYPE_EMPTY = 2
    }

    override fun bindData(holder: RecyclerViewHolder, data: MyActivityBean, position: Int) {
        when (getItemViewType(position)) {
            ITEM_TYPE_NORMAL -> {
                holder.setText(R.id.tv_title, data.activity_name)
                holder.setText(R.id.tv_org, data.org_name)
                holder.setText(R.id.tv_area, data.activity_pace)
                holder.setText(R.id.tv_time, "活动时间：" + data.start_time + "~" + data.end_time)
                when (type) {
                    1 -> {
                        holder.setText(R.id.tv_state, "招募中")
                        holder.getView<TextView>(R.id.tv_state).background = mContext.resources.getDrawable(R.drawable.shape_corner_ing)
                        when (data.state) {
                            "0" -> {//已报名，待审核
                                holder.setText(R.id.tv_operation, mContext.getString(R.string.wait_and_sign))
                            }
                            "1" -> {//审核通过
                                holder.setText(R.id.tv_operation, mContext.getString(R.string.pass_and_sign))
                            }
                        }
                    }
                    2 -> {
                        holder.setText(R.id.tv_state, "进行中")
                        holder.getView<TextView>(R.id.tv_state).background = mContext.resources.getDrawable(R.drawable.shape_corner_going)
                        holder.setText(R.id.tv_operation, mContext.getString(R.string.service_detail))
                        when(data.card_stye){
                            "1"->{
                                holder.setText(R.id.tv_sign, mContext.getString(R.string.check_in))
                            }
                            else->{
                                holder.setText(R.id.tv_sign, mContext.getString(R.string.check_out))
                            }
                        }
                        holder.setViewVisibility(R.id.view, View.VISIBLE)
                        holder.setViewVisibility(R.id.tv_sign, View.VISIBLE)
                    }
                    3 -> {
                        holder.setText(R.id.tv_state, "已完成")
                        holder.getView<TextView>(R.id.tv_state).background = mContext.resources.getDrawable(R.drawable.shape_corner_over)
                        holder.setText(R.id.tv_operation, mContext.getString(R.string.service_detail))
                    }
                }
                holder.getView<TextView>(R.id.tv_operation).setOnClickListener { clickListener?.leftClick(position) }
                holder.getView<TextView>(R.id.tv_sign).setOnClickListener { clickListener?.rightClick(position) }
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
                RecyclerViewHolder(inflaterView(R.layout.fragment_my_acts, parent))
        }

    }

    fun setActivityType(type: Int) {
        this.type = type
    }

    var clickListener: onItemOperateListener? = null
    fun setOnItemOperateListener(clickListener: onItemOperateListener) {
        this.clickListener = clickListener
    }

    interface onItemOperateListener {
        fun leftClick(position: Int)
        fun rightClick(position: Int)
    }
}