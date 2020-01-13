package com.wmsj.baselibs.weight

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.wmsj.baselibs.R
import com.wmsj.baselibs.ui.adapter.SelectDialogAdapter
import com.wmsj.baselibs.bean.BaseInfoBean
import java.util.*

/**
 * 复选框
on 2019/8/19.
 */
class SelectDialog(context: Context?, themeResId: Int) : Dialog(context, themeResId) {
    class Builder(private val context: Context) {
        private var title: String? = null
        private var itemSelect: String? = null
        private var itemList: ArrayList<BaseInfoBean>? = null
        private var itemSelctList = ArrayList<String>()
        private var itemNameList = ArrayList<String>()
        private var positiveButtonContent: String? = null
        private var negativeButtonContent: String? = null
        private var positiveButtonListener: DialogInterface.OnClickListener? = null
        private var negativeButtonListener: onCheckBackListener? = null
        private var withOffSize: Float = 0.toFloat()
        private var heightOffSize: Float = 0.toFloat()


        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }


        fun setTitle(title: Int): Builder {
            this.title = context.getText(title) as String
            return this
        }


        fun setPositiveButton(text: String, listener: DialogInterface.OnClickListener): Builder {
            this.positiveButtonContent = text
            this.positiveButtonListener = listener
            return this
        }

        fun setPositiveButton(textId: Int, listener: DialogInterface.OnClickListener): Builder {
            this.positiveButtonContent = context.getText(textId) as String
            this.positiveButtonListener = listener
            return this
        }

        fun setNegativeButton(text: String, listener: onCheckBackListener): Builder {
            this.negativeButtonContent = text
            this.negativeButtonListener = listener
            return this
        }

        fun setNegativeButton(textId: Int, listener: onCheckBackListener): Builder {
            this.negativeButtonContent = context.getText(textId) as String
            this.negativeButtonListener = listener
            return this
        }

        fun setWith(v: Float): Builder {
            this.withOffSize = v
            return this
        }

        fun setItemList(list: ArrayList<BaseInfoBean>): Builder {
            this.itemList = list
            return this
        }

        fun setItemSelectList(list: String?): Builder {
            this.itemSelect = list
            return this
        }

        fun setContentView(v: Float): Builder {
            this.heightOffSize = v
            return this
        }

        fun create(): SelectDialog {
            /**
             * 利用我们刚才自定义的样式初始化Dialog
             */
            val dialog = SelectDialog(context,
                    R.style.CommonDialogStyle)
            /**
             * 下面就初始化Dialog的布局页面
             */
            val inflater = context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val dialogLayoutView = inflater.inflate(R.layout.dialog_select,
                    null)
            dialog.addContentView(dialogLayoutView, ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))

            if (!TextUtils.isEmpty(title)) {
                (dialogLayoutView.findViewById<View>(R.id.tv_dialog_title) as TextView).text = title
            }
            dialogLayoutView.findViewById<RecyclerView>(R.id.recyclerView).layoutManager = LinearLayoutManager(context)
            var selectDialogAdapter = itemList?.let { SelectDialogAdapter(context, it, R.layout.dialog_select_item) }
            itemSelect?.let { selectDialogAdapter?.setItemSelect(it) }
            dialogLayoutView.findViewById<RecyclerView>(R.id.recyclerView).adapter = selectDialogAdapter
            selectDialogAdapter?.setOnItemCheck(object : SelectDialogAdapter.onItemCheckListener {
                override fun onCheck(position: Int, isChecked: Boolean) {
                    if (isChecked) {
                        itemList?.get(position)?.let { it.id?.let { it1 -> itemSelctList.add(it1) } }
                        itemList?.get(position)?.let { it.c_name?.let { it1 -> itemNameList.add(it1) } }
                    } else {
                        itemList?.get(position)?.let { itemSelctList.remove(it.id) }
                        itemList?.get(position)?.let { itemNameList.remove(it.c_name) }
                    }
                }

            })
            if (!TextUtils.isEmpty(positiveButtonContent)) {
                (dialogLayoutView.findViewById<View>(R.id.tv_dialog_pos) as TextView).text = positiveButtonContent
                if (positiveButtonListener != null) {
                    (dialog.findViewById<View>(R.id.tv_dialog_pos) as TextView)
                            .setOnClickListener { positiveButtonListener!!.onClick(dialog, -1) }

                }
            } else {
                (dialogLayoutView.findViewById<View>(R.id.tv_dialog_pos) as TextView).visibility = View.GONE
            }

            if (!TextUtils.isEmpty(negativeButtonContent)) {
                (dialogLayoutView.findViewById<View>(R.id.tv_dialog_neg) as TextView).text = negativeButtonContent
                if (negativeButtonListener != null) {
                    (dialogLayoutView
                            .findViewById<View>(R.id.tv_dialog_neg) as TextView)
                            .setOnClickListener { negativeButtonListener!!.onChecked(dialog, itemSelctList, itemNameList) }
                }
            } else {
                (dialogLayoutView.findViewById<View>(R.id.tv_dialog_neg) as TextView).visibility = View.GONE
            }
            /**
             * 将初始化完整的布局添加到dialog中
             */
            dialog.setContentView(dialogLayoutView)
            /**
             * 禁止点击Dialog以外的区域时Dialog消失
             */
            dialog.setCanceledOnTouchOutside(false)


            val window = dialog.window
            val context = this.context as Activity
            val windowManager = context.windowManager

            val defaultDisplay = windowManager.defaultDisplay

            val attributes = window!!.attributes

            if (withOffSize.toDouble() != 0.0) {

                attributes.width = (defaultDisplay.width * withOffSize).toInt()
            } else {
                attributes.width = (defaultDisplay.width * 0.77).toInt()

            }
            if (heightOffSize.toDouble() != 0.0) {

                attributes.height = (defaultDisplay.height * heightOffSize).toInt()
            }
            window.attributes = attributes
            return dialog
        }
    }

    interface onCheckBackListener {
        fun onChecked(dialog: DialogInterface, idList: ArrayList<String>?, nameList: ArrayList<String>?)
    }
}