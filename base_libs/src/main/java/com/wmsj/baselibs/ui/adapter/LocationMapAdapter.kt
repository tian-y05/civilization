package com.wmsj.baselibs.ui.adapter

import android.content.Context
import android.view.View
import com.wmsj.baselibs.recyclerview.RecyclerViewHolder
import com.wmsj.baselibs.recyclerview.adapter.CommonRecyclerAdapter
import com.wmsj.baselibs.R
import com.xbrc.myapplication.bean.PoiLocationItem

/**
 * Created by tian
on 2019/8/12.
 */
class LocationMapAdapter(mContext: Context, photoList: ArrayList<PoiLocationItem>, layoutId: Int) : CommonRecyclerAdapter<PoiLocationItem>(mContext, photoList, layoutId) {


    override fun bindData(holder: RecyclerViewHolder, data: PoiLocationItem, position: Int) {
        holder.setText(R.id.tv_title,data.poiName)
        holder.setText(R.id.tv_location,data.provinceName + data.cityName + data.address)
        if(data.isSelect){
            holder.setViewVisibility(R.id.iv_select, View.VISIBLE)
        }else{
            holder.setViewVisibility(R.id.iv_select, View.GONE)
        }
    }


}