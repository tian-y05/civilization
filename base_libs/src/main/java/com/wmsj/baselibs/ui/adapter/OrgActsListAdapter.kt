package com.wmsj.baselibs.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.wmsj.baselibs.R
import com.wmsj.baselibs.bean.EventsBean
import com.wmsj.baselibs.recyclerview.RecyclerViewHolder
import com.wmsj.baselibs.recyclerview.adapter.CommonRecyclerAdapter
import com.wmsj.baselibs.utils.StringUtils
import com.wmsj.baselibs.view.DrawableCenterTextView


/**
 * 组织活动管理adapter
on 2019/8/6.
 */
class OrgActsListAdapter(mContext: Context, newsHomeActivityListBean: ArrayList<EventsBean>, layoutId: Int) : CommonRecyclerAdapter<EventsBean>(mContext, newsHomeActivityListBean, layoutId) {

    private var type = -1

    companion object {
        private const val ITEM_TYPE_NORMAL = 1
        private const val ITEM_TYPE_EMPTY = 2
    }

    override fun bindData(holder: RecyclerViewHolder, data: EventsBean, position: Int) {
        when (getItemViewType(position)) {
            ITEM_TYPE_NORMAL -> {
                holder.setText(R.id.tv_title, data.name)
                holder.setText(R.id.tv_org, data.orgname)
                holder.setText(R.id.tv_area, data.place)
                holder.setText(R.id.tv_time, "活动时间：" + data.activitytime)
                when (type) {
                    6 -> {
                        holder.setViewVisibility(R.id.ll_operation, View.GONE)
                        holder.setViewVisibility(R.id.ll_recruit, View.GONE)
                        holder.setViewVisibility(R.id.ll_going, View.GONE)
                        holder.setViewVisibility(R.id.tv_back, View.VISIBLE)
                        holder.setText(R.id.tv_state, "招募前")
                        holder.getView<TextView>(R.id.tv_state).background = mContext.resources.getDrawable(R.drawable.shape_corner_before)
                        holder.setText(R.id.tv_state, "招募前")
                    }
                    1 -> {
                        holder.setViewVisibility(R.id.ll_operation, View.GONE)
                        holder.setViewVisibility(R.id.ll_recruit, View.VISIBLE)
                        holder.setViewVisibility(R.id.tv_back, View.VISIBLE)
                        holder.setViewVisibility(R.id.ll_going, View.GONE)
                        holder.setText(R.id.tv_state, "招募中")
                        holder.getView<TextView>(R.id.tv_state).background = mContext.resources.getDrawable(R.drawable.shape_corner_ing)
                        holder.setText(R.id.tv_state, "招募中")
                        holder.setText(R.id.tv_recruit_num, data.recruited.toString() + "/" + data.recruitment)
                        holder.setText(R.id.tv_pending_trial, data.needcheck.toString())
                    }
                    2 -> {
                        holder.setViewVisibility(R.id.ll_operation, View.GONE)
                        holder.setViewVisibility(R.id.ll_recruit, View.GONE)
                        holder.setViewVisibility(R.id.tv_back, View.GONE)
                        holder.setViewVisibility(R.id.ll_going, View.VISIBLE)
                        holder.setText(R.id.tv_state, "进行中")
                        holder.getView<TextView>(R.id.tv_state).background = mContext.resources.getDrawable(R.drawable.shape_corner_going)
                        holder.setText(R.id.tv_state, "进行中")
                        holder.setText(R.id.tv_join_num, data.participate.toString() + "/" + data.recruitment)
                        holder.setText(R.id.tv_acts_photo, if (StringUtils.isEmpty(data.picnum)) "0" else StringUtils.split(data.picnum,",").size.toString())
                        holder.setText(R.id.tv_acts_report, data.report_num.toString())
                    }
                    3 -> {
                        holder.setText(R.id.tv_state, "待审核")
                        holder.getView<TextView>(R.id.tv_state).background = mContext.resources.getDrawable(R.drawable.shape_corner_check)
                        holder.setText(R.id.tv_state, "待审核")
                        holder.setViewVisibility(R.id.ll_recruit, View.GONE)
                        holder.setViewVisibility(R.id.ll_going, View.GONE)
                        when (data.statu) {
                            3 -> {
                                holder.setViewVisibility(R.id.ll_operation, View.VISIBLE)
                                holder.setViewVisibility(R.id.tv_back, View.VISIBLE)
                                holder.setViewVisibility(R.id.iv_statu, View.VISIBLE)
                            }
                            else -> {
                                holder.setViewVisibility(R.id.ll_operation, View.GONE)
                                holder.setViewVisibility(R.id.tv_back, View.VISIBLE)
                                holder.setViewVisibility(R.id.iv_statu, View.GONE)
                            }
                        }
                    }
                    4 -> {
                        holder.setViewVisibility(R.id.ll_operation, View.GONE)
                        holder.setViewVisibility(R.id.ll_recruit, View.GONE)
                        holder.setViewVisibility(R.id.tv_back, View.GONE)
                        holder.setViewVisibility(R.id.ll_supplement_time, View.GONE)
                        holder.setViewVisibility(R.id.view_supple, View.GONE)
                        holder.setViewVisibility(R.id.ll_going, View.VISIBLE)
                        holder.setText(R.id.tv_state, "已完成")
                        holder.getView<TextView>(R.id.tv_state).background = mContext.resources.getDrawable(R.drawable.shape_corner_before)
                        holder.setText(R.id.tv_state, "已完成")
                        holder.setText(R.id.tv_join_num, data.participate.toString() + "/" + data.recruitment)
                        holder.setText(R.id.tv_acts_photo, if (StringUtils.isEmpty(data.picnum)) "0" else StringUtils.split(data.picnum,",").size.toString())
                        holder.setText(R.id.tv_acts_report, data.report_num.toString())

                    }
                    5 -> {
                        holder.setViewVisibility(R.id.ll_operation, View.VISIBLE)
                        holder.setViewVisibility(R.id.ll_recruit, View.GONE)
                        holder.setViewVisibility(R.id.tv_back, View.GONE)
                        holder.setViewVisibility(R.id.ll_going, View.GONE)
                        holder.setViewVisibility(R.id.tv_publish, View.VISIBLE)
                        holder.setViewVisibility(R.id.tv_back_reason, View.GONE)
                        holder.setText(R.id.tv_state, "草稿")
                        holder.getView<TextView>(R.id.tv_state).background = mContext.resources.getDrawable(R.drawable.shape_corner_draft)
                        holder.setText(R.id.tv_state, "草稿")

                    }
                }
                setOnChildClickListener(holder.getView<DrawableCenterTextView>(R.id.tv_publish), position)
                setOnChildClickListener(holder.getView<DrawableCenterTextView>(R.id.tv_edit), position)
                setOnChildClickListener(holder.getView<DrawableCenterTextView>(R.id.tv_delete), position)
                setOnChildClickListener(holder.getView<LinearLayout>(R.id.ll_join_num), position)
                setOnChildClickListener(holder.getView<LinearLayout>(R.id.ll_acts_photo), position)
                setOnChildClickListener(holder.getView<LinearLayout>(R.id.ll_acts_report), position)
                setOnChildClickListener(holder.getView<LinearLayout>(R.id.tv_back), position)
                setOnChildClickListener(holder.getView<LinearLayout>(R.id.ll_recruit_num), position)
                setOnChildClickListener(holder.getView<LinearLayout>(R.id.ll_pending_trial), position)
                setOnChildClickListener(holder.getView<LinearLayout>(R.id.ll_employ_member), position)
                setOnChildClickListener(holder.getView<LinearLayout>(R.id.ll_supplement_time), position)
                setOnChildClickListener(holder.getView<DrawableCenterTextView>(R.id.tv_back_reason), position)
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
                RecyclerViewHolder(inflaterView(R.layout.fragment_org_acts, parent))
        }

    }

    fun setActsType(state: Int) {
        this.type = state
    }

}