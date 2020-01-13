package com.wmsj.baselibs.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.wmsj.baselibs.R
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.bean.OrgMemberBean
import com.wmsj.baselibs.recyclerview.RecyclerViewHolder
import com.wmsj.baselibs.recyclerview.adapter.CommonRecyclerAdapter

/**
 * 首页最新活动adapter
on 2019/8/6.
 */
class EmployMemberAdapter(mContext: Context, memberList: ArrayList<OrgMemberBean>, layoutId: Int) : CommonRecyclerAdapter<OrgMemberBean>(mContext, memberList, layoutId) {

    companion object {
        private const val ITEM_TYPE_NORMAL = 1
        private const val ITEM_TYPE_EMPTY = 2
    }


    override fun bindData(holder: RecyclerViewHolder, data: OrgMemberBean, position: Int) {
        when (getItemViewType(position)) {
            ITEM_TYPE_NORMAL -> {
                holder.setImagePath(R.id.iv_logo, object : RecyclerViewHolder.HolderImageLoader(Const.BASE_URL + data.logo) {
                    override fun loadImage(iv: ImageView, path: String) {
                        var requestOptions = RequestOptions()
                        requestOptions.error(R.mipmap.author_default).placeholder(R.mipmap.author_default)
                        Glide.with(mContext)
                                .load(path)
                                .apply(requestOptions)
                                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                                .into(iv)
                    }
                })
                holder.setText(R.id.tv_name, data.name + "\u3000" + "(" + (if (data.sex == 2) "女" else "男") + ")")
                holder.setText(R.id.tv_area, "所在地：" + data.place)
                if(data.join == "1"){
                    holder.getView<CheckBox>(R.id.checkbox).setBackgroundResource(R.mipmap.org_checked)
                }else{
                    holder.getView<CheckBox>(R.id.checkbox).isChecked = data.isSelect
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
                RecyclerViewHolder(inflaterView(R.layout.employ_member_item, parent))
        }

    }

}