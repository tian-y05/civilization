package com.wmsj.baselibs.ui.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.widget.TextView
import com.amap.api.services.help.Tip
import com.wmsj.baselibs.recyclerview.RecyclerViewHolder
import com.wmsj.baselibs.recyclerview.adapter.CommonRecyclerAdapter
import com.wmsj.baselibs.R
import com.wmsj.baselibs.utils.Linker

/**
 * Created by tian
on 2019/8/14.
 */
class SearchLocationAdapter(mContext: Context, mSearchList: ArrayList<Tip>, layoutId: Int) : CommonRecyclerAdapter<Tip>(mContext, mSearchList, layoutId) {

    private var search_string = ""
    override fun bindData(holder: RecyclerViewHolder, data: Tip, position: Int) {
        holder.setText(R.id.tv_title, data.name)
        holder.setText(R.id.tv_location, data.district + data.address)
        val androidRules = arrayOf(search_string)
        Linker.Builder()
                .content(data.name)
                .textView(holder.getView<TextView>(R.id.tv_title))
                .links(androidRules)
                .linkColor(ContextCompat.getColor(mContext, R.color.end_blue))
                .apply()
    }

    fun setSearchText(keyWord: String) {
        this.search_string = keyWord
    }

}