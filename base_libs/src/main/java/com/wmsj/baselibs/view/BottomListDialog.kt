package com.wmsj.baselibs.view

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.view.*
import android.widget.*
import com.wmsj.baselibs.R
import com.wmsj.baselibs.bean.AreaBean
import com.wmsj.baselibs.bean.BaseInfoBean
import com.wmsj.baselibs.bean.OrgListBean
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable


/**
 * 通过底部弹出框
on 2019/8/17.
 */
class BottomListDialog : AppCompatDialogFragment() {

    private lateinit var lstShow: ListView
    private lateinit var btnCancel: TextView

    private var itemList = ArrayList<String>()
    private var itemIconList = ArrayList<Int>()

    private var mComposite: CompositeDisposable = CompositeDisposable()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(activity!!, R.style.CommonDialogStyle)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // 设置Content前设定
        dialog.setContentView(R.layout.dialog_bottom_list)
        dialog.setCanceledOnTouchOutside(true) // 外部点击取消

        // 设置宽度为屏宽, 靠近屏幕底部。
        val window = dialog.window
        val lp = window!!.attributes
        lp.gravity = Gravity.BOTTOM // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT // 宽度持平
        window.attributes = lp
        initView(dialog)

//        if (savedInstanceState != null) {
//            itemList = savedInstanceState.getStringArray("itemList")
//            itemIconList = Gson().fromJson(savedInstanceState.getString("itemIconList")!!, Array<Int>::class.java)
//        }

        lstShow.adapter = ArrayAdapter(context!!, R.layout.item_simple_list, R.id.tv_show, itemList!!)
        setListViewHeight(lstShow)
        lstShow.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            onItemClickListener.onItemClick(parent, view, position, id, itemList!![position], itemList)
            dismiss()
        }
        btnCancel.setOnClickListener {
            dismiss()
        }

        return dialog
    }

    private fun initView(dialog: Dialog) {
        lstShow = dialog.findViewById(R.id.lstShow)
        btnCancel = dialog.findViewById(R.id.tv_cancel)
    }

    fun setItemList(itemList: ArrayList<String>): BottomListDialog {
        this.itemList.addAll(itemList)
        return this
    }

    fun setBaseInfoList(list: ArrayList<BaseInfoBean>): BottomListDialog {
//        for (infoBean in list) {
//            infoBean.c_name?.let { this.itemList.add(it) }
//        }
        Observable.fromIterable(list)
                .map { t -> t.c_name }
                .subscribe { it -> it?.let { it1 -> itemList.add(it1) } }
        return this
    }

    fun setAreaInfoList(list: ArrayList<AreaBean>): BottomListDialog {
        Observable.fromIterable(list)
                .map { t -> t.name }
                .subscribe { it -> it?.let { it1 -> itemList.add(it1) } }
        return this
    }

    fun setOrgList(list: ArrayList<OrgListBean>): BottomListDialog {
        Observable.fromIterable(list)
                .map { t -> t.org_cname }
                .subscribe { it -> it?.let { it1 -> itemList.add(it1) } }
        return this
    }
    fun setItemIconList(itemIconList: ArrayList<Int>): BottomListDialog {
        this.itemIconList.addAll(itemIconList)
        return this
    }


    override fun onDestroyView() {
        mComposite.clear()
        super.onDestroyView()
    }

    interface OnItemClickListener {
        fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long, selectItem: String, itemList: ArrayList<String>)
    }

    private lateinit var onItemClickListener: OnItemClickListener

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener): BottomListDialog {
        this.onItemClickListener = onItemClickListener
        return this
    }

    private fun setListViewHeight(listView: ListView) {
        val listAdapter = listView.adapter ?: return //得到ListView 添加的适配器
        if (listAdapter.count < 1) return
        val itemView = listAdapter.getView(0, null, listView) //获取其中的一项
        itemView.measure(0, 0)
        val itemHeight = itemView.measuredHeight //一项的高度
        val itemCount = listAdapter.count//得到总的项数
        var layoutParams: LinearLayout.LayoutParams? = null //进行布局参数的设置
        if (itemCount <= 4) {
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight * itemCount)
        } else if (itemCount > 4) {
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight * 3)
        }
        listView.layoutParams = layoutParams
    }
}
