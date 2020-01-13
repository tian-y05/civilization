package com.wmsj.baselibs.recyclerview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions


class ViewHolder private constructor(private val context: Context, parent: ViewGroup, layoutId: Int,
                                     val position: Int) {

    private var views: SparseArray<View> = SparseArray()
    private var requestManager: RequestManager = Glide.with(context.applicationContext)
    var convertView: View = LayoutInflater.from(context).inflate(layoutId, parent,
            false)

    init {
        convertView.tag = this
    }// 配置图片加载及显示选项（还有一些其他的配置，查阅doc文档吧）


    fun <T : View> getView(viewId: Int): T {

        var view: View? = views.get(viewId)
        if (view == null) {
            view = convertView.findViewById(viewId)
            views.put(viewId, view)
        }
        return view as T
    }

    fun setText(viewId: Int, text: String): ViewHolder {
        val view = getView<TextView>(viewId)
        view.text = text
        return this
    }

    fun setEditText(viewId: Int, text: String): ViewHolder {
        val view = getView<EditText>(viewId)
        view.setText(text)
        return this
    }

    fun setCheckBoxText(viewId: Int, text: String): ViewHolder {
        val view = getView<CheckBox>(viewId)
        view.text = text
        return this
    }

    fun setCheckBoxCheck(viewId: Int, ischeck: Boolean?): ViewHolder {
        val view = getView<CheckBox>(viewId)
        view.isChecked = ischeck!!
        return this
    }

    fun setRadiobtnText(viewId: Int, text: String): ViewHolder {
        val view = getView<RadioButton>(viewId)
        view!!.text = text
        return this
    }

    fun setRadiobtnCheck(viewId: Int, ischeck: Boolean?): ViewHolder {
        val view = getView<RadioButton>(viewId)
        view.isChecked = ischeck!!
        return this
    }

    fun setTextBackgroud(viewId: Int, bgId: Int): ViewHolder {
        val view = getView<TextView>(viewId)
        view.setBackgroundResource(bgId)
        return this
    }

    fun setText(viewId: Int, text: CharSequence): ViewHolder {
        val view = getView<TextView>(viewId)
        view.text = text
        return this
    }

    fun setTextColor(viewId: Int, textColor: Int): ViewHolder {
        val view = getView<TextView>(viewId)
        view.setTextColor(textColor)
        return this
    }

    fun setTextColorRes(viewId: Int, textColorRes: Int): ViewHolder {
        val view = getView<TextView>(viewId)
        view.setTextColor(context.resources.getColor(textColorRes))
        return this
    }

    fun setImageurl(viewId: Int, url: String): ViewHolder {
        val view = getView<ImageView>(viewId)
        requestManager.load(url).into(view)
        return this
    }
    fun setImageurl(viewId: Int, url: String, defultid: Int, errorid: Int): ViewHolder {
        val view = getView<ImageView>(viewId)
        val requestOptions = RequestOptions()
        requestOptions.placeholder(defultid)
                .error(errorid)
        requestManager.load(url).apply(requestOptions).into(view)

        return this
    }

    fun setImageVisibility(viewId: Int, visibity: Boolean): ViewHolder {
        val view = getView<ImageView>(viewId)
        if (visibity)
            view.visibility = View.VISIBLE
        else
            view.visibility = View.GONE
        return this
    }

    fun setImageDrawable(viewId: Int, drawable: Drawable): ViewHolder {
        val view = getView<ImageView>(viewId)
        view.setImageDrawable(drawable)
        return this
    }

    fun setImageBitmap(viewId: Int, bitmap: Bitmap): ViewHolder {
        val view = getView<ImageView>(viewId)
        view.setImageBitmap(bitmap)
        return this
    }

    fun setImageBackground(viewId: Int, imageID: Int): ViewHolder {
        val view = getView<ImageView>(viewId)
        view.setImageResource(imageID)
        return this
    }


    fun setLayoutground(viewId: Int, imageID: Int): ViewHolder {
        val view = getView<View>(viewId)
        view.setBackgroundResource(imageID)
        return this
    }

    fun setRateProgress(viewId: Int, rateing: Float?): ViewHolder {
        val view = getView<RatingBar>(viewId)
        view.max = 50
        view.progress = (rateing!! * 10).toInt()
        return this
    }

    companion object {


        operator fun get(context: Context, convertView: View?,
                         parent: ViewGroup, layoutId: Int, position: Int): ViewHolder {

            return if (convertView == null) {
                ViewHolder(context, parent, layoutId, position)
            } else convertView.tag as ViewHolder
        }
    }
}
