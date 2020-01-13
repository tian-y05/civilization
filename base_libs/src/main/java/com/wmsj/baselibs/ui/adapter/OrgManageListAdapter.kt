package com.wmsj.baselibs.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.wmsj.baselibs.R
import com.wmsj.baselibs.bean.HangupOrgBean
import com.wmsj.baselibs.recyclerview.RecyclerViewHolder
import com.wmsj.baselibs.recyclerview.adapter.CommonRecyclerAdapter
import com.wmsj.baselibs.utils.StringUtils

/**
 * 首页最新活动adapter
on 2019/8/6.
 */
class OrgManageListAdapter(mContext: Context, activityListBean: ArrayList<HangupOrgBean>, layoutId: Int) : CommonRecyclerAdapter<HangupOrgBean>(mContext, activityListBean, layoutId) {

    companion object {
        private const val ITEM_TYPE_NORMAL = 1
        private const val ITEM_TYPE_EMPTY = 2
    }

    private var type = -1

    override fun bindData(holder: RecyclerViewHolder, data: HangupOrgBean, position: Int) {
        when (getItemViewType(position)) {
            ITEM_TYPE_NORMAL -> {
                holder.setText(R.id.tv_title, data.name)
                holder.setText(R.id.tv_location, "地址:" + data.address)
                holder.setText(R.id.tv_service_duration, data.service_count)
                holder.setText(R.id.tv_member_num, data.membernum.toString())
                holder.setText(R.id.tv_create_time, if (StringUtils.isEmpty(data.time)) "" else StringUtils.split(data.time, " ")[0])
                holder.setViewVisibility(R.id.ll_bottom, View.VISIBLE)

                when (type) {
                    1 -> {
                        holder.setViewVisibility(R.id.ll_bottom, View.VISIBLE)
                        holder.setViewVisibility(R.id.tv_right, View.VISIBLE)
                        holder.setViewVisibility(R.id.tv_left, View.GONE)

                        when (data.state) {
                            "1" -> {// 申请挂靠

                            }
                            "2" -> {//已申请挂靠,请等待审核
                                holder.setText(R.id.tv_right, mContext.resources.getString(R.string.move_hungup_wait))
                                holder.getView<TextView>(R.id.tv_right).background = mContext.resources.getDrawable(R.color.move_hungup)
                            }
                            "3" -> {//撤销挂靠
                                holder.setText(R.id.tv_right, mContext.resources.getString(R.string.move_hungup))
                            }
                            "4" -> {//移除挂靠
                                holder.setText(R.id.tv_right, mContext.resources.getString(R.string.delete_hungup))
                            }
                            "5", "6" -> {//通过
                                holder.setViewVisibility(R.id.ll_bottom, View.VISIBLE)
                                holder.setViewVisibility(R.id.tv_right, View.VISIBLE)
                                holder.setViewVisibility(R.id.tv_left, View.VISIBLE)
                                holder.setText(R.id.tv_right, mContext.resources.getString(R.string.go_on))
                                holder.setText(R.id.tv_left, mContext.resources.getString(R.string.refuse))
                            }

                        }
                    }
                    2 -> {
                        holder.setViewVisibility(R.id.ll_bottom, View.VISIBLE)
                        holder.setViewVisibility(R.id.tv_right, View.VISIBLE)
                        holder.setViewVisibility(R.id.tv_left, View.GONE)
                        holder.setText(R.id.tv_right, mContext.resources.getString(R.string.delete_hungup))
                    }
                    3 -> {
                        holder.setViewVisibility(R.id.ll_bottom, View.VISIBLE)
                        holder.setViewVisibility(R.id.tv_right, View.VISIBLE)
                        holder.setViewVisibility(R.id.tv_left, View.VISIBLE)
                        holder.setText(R.id.tv_right, mContext.resources.getString(R.string.go_on))
                        holder.setText(R.id.tv_left, mContext.resources.getString(R.string.refuse))
                    }
                }
                holder.getView<LinearLayout>(R.id.ll_content).background = mContext.resources.getDrawable(R.mipmap.background_gray)
                holder.getView<TextView>(R.id.tv_right).setOnClickListener { childClickListener?.onItemChidClick(it, position) }
                holder.getView<TextView>(R.id.tv_left).setOnClickListener { childClickListener?.onItemChidClick(it, position) }
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
                RecyclerViewHolder(inflaterView(R.layout.activity_list_item, parent))
        }
    }

    fun setType(type: Int) {
        this.type = type
    }

}