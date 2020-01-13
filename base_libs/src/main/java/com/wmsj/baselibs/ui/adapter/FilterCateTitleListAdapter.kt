package com.wmsj.baselibs.ui.adapter

import android.content.Context
import android.text.TextUtils
import com.wmsj.baselibs.recyclerview.RecyclerViewHolder
import com.wmsj.baselibs.recyclerview.adapter.CommonRecyclerAdapter
import com.wmsj.baselibs.R
import com.wmsj.baselibs.bean.OrgCataBean

/**
 * 筛选adapter
on 2019/8/6.
 */
class FilterCateTitleListAdapter(mContext: Context, orgSonBeanList: ArrayList<OrgCataBean>, layoutId: Int) : CommonRecyclerAdapter<OrgCataBean>(mContext, orgSonBeanList, layoutId) {


    override fun bindData(holder: RecyclerViewHolder, data: OrgCataBean, position: Int) {
        holder.setText(R.id.tv_name, if (data.select_name != null && !TextUtils.isEmpty(data.select_name)) {data.select_name} else data.cate_name)
    }


}