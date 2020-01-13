package com.wmsj.baselibs.utils

import java.math.BigDecimal
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by tian
on 2019/8/29.
 */
object FormatUtils {

    /**
     * 时间戳转日期
     * "yyyy-MM-dd"
     * "yyyy-MM-dd HH:mm:ss"
     */
    fun getTime(date: Date, type: String): String {//可根据需要自行截取数据显示
        val format = SimpleDateFormat(type)
        return format.format(date)
    }

//    fun getTime(date: Date): String {//可根据需要自行截取数据显示
//        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//        return format.format(date)
//    }

    /**
     * 比较日期相隔多久
     */
    fun dateDiff(startTime: String, endTime: String, format: String): Int {
        if (compareDate(startTime, endTime, format) == 0) {
            // 按照传入的格式生成一个simpledateformate对象
            val sd = SimpleDateFormat(format)
            val nd = (1000 * 24 * 60 * 60).toLong()// 一天的毫秒数
            val nh = (1000 * 60 * 60).toLong()// 一小时的毫秒数
            val nm = (1000 * 60).toLong()// 一分钟的毫秒数
            val ns: Long = 1000// 一秒钟的毫秒数
            val diff: Long
            var day: Long = 0
            try {
                // 获得两个时间的毫秒时间差异
                diff = sd.parse(endTime).time - sd.parse(startTime).time
                val min = diff / nm// 计算差多少分钟

                val b1 = BigDecimal(min)
                val b2 = BigDecimal(60)
                //默认保留两位会有错误，这里设置保留小数点后4位
                val double = b1.divide(b2, 1, BigDecimal.ROUND_DOWN).toDouble()
                // 输出结果
                return double.toInt()

            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return 0

        } else {
            return 0
        }

    }

    /**
     * 日期转时间戳
     */
    fun date2TimeStamp(date: String, format: String): String {
        try {
            val sdf = SimpleDateFormat(format)
            return (sdf.parse(date).time / 1000).toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 比较两个时间大小
     */
    fun compareDate(startTime: String, endTime: String, format: String): Int {
        L.d("et_recruit_start:" + startTime + "-----" + endTime)
        if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)) {
            return -1
        }
        val df = SimpleDateFormat(format)
        try {
            val dt1 = df.parse(startTime)
            val dt2 = df.parse(endTime)
            if (dt1.time > dt2.time) {
                return 1
            } else if (dt1.time <= dt2.time) {
                return 0
            } else {
                return -1
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

        return -1
    }


    /**
     * 时间字符串转Calendar
     */
    fun getCalendar(time: String): Calendar {
        var date: Date? = null
        var calendar = Calendar.getInstance()
        var format: SimpleDateFormat? = null//声明格式化日期的对象
        if (time != null) {
            format = SimpleDateFormat("yyyy-MM-dd HH:mm")//创建日期的格式化类型
            try {
                date = format.parse(time)
                calendar.time = date
            } catch (e: ParseException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
        }
        return calendar
    }

    /**
     * 时间字符串转Calendar
     */
    fun getSelectCalendar(time: String): Calendar {
        var date: Date? = null
        var calendar = Calendar.getInstance()
        var format: SimpleDateFormat? = null//声明格式化日期的对象
        if (time != null) {
            format = SimpleDateFormat("yyyy-MM-dd")//创建日期的格式化类型
            try {
                date = format.parse(time)
                calendar.time = date
            } catch (e: ParseException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
        }
        return calendar
    }

    /**
     * 获取当前时间Calendar
     */
    fun getCurTime(): Calendar {
        var calendar = Calendar.getInstance()
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
        return calendar
    }

    /**
     * 获取时间Calendar
     */
    fun getEndTime(): Calendar {
        var endCalendar = Calendar.getInstance()
        endCalendar.set(2099, 12, 31)
        return endCalendar
    }

    fun getCurrentTime(): String {
        val currentTime = System.currentTimeMillis()

        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")

        val date = Date(currentTime)

        return formatter.format(date)
    }
}