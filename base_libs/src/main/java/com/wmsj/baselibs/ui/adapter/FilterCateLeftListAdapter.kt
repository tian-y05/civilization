package com.wmsj.baselibs.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.LinearLayout
import com.wmsj.baselibs.recyclerview.RecyclerViewHolder
import com.wmsj.baselibs.recyclerview.adapter.CommonRecyclerAdapter
import com.wmsj.baselibs.R
import com.wmsj.baselibs.bean.OrgSon

/**
 * 筛选adapter
on 2019/8/6.
 */
class FilterCateLeftListAdapter(mContext: Context, orgSonBeanList: ArrayList<OrgSon>) : CommonRecyclerAdapter<OrgSon>(mContext, orgSonBeanList, -1) {

    companion object {
        private const val ITEM_TYPE_NORMAL = 1
        private const val ITEM_TYPE_EMPTY = 2
    }
    private var selectId = -1

    override fun bindData(holder: RecyclerViewHolder, data: OrgSon, position: Int) {
        when (getItemViewType(position)) {
            ITEM_TYPE_NORMAL -> {
                holder.setText(R.id.tv_name, data.cate_name)
                if(position == selectId){
                    holder.getView<LinearLayout>(R.id.ll_item).setBackgroundColor(mContext.resources.getColor(R.color.background_gray))
                }else{
                    holder.getView<LinearLayout>(R.id.ll_item).setBackgroundColor(mContext.resources.getColor(R.color.white))
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
                RecyclerViewHolder(inflaterView(R.layout.filter_last_item, parent))
        }

    }

    fun setCurrentSelect(selectId: Int) {
        this.selectId = selectId
        notifyDataSetChanged()
    }
}