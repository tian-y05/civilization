package com.wmsj.baselibs



/**
 * Created by tian
 * on 2019/7/1.
 */

object Const {

    val BASE_URL = BuildConfig.BASE_URL

    val TOKEN = ""

    val API_TOKEN = BuildConfig.API_TOKEN

    val BANNER_KEY = BuildConfig.BANNER_KEY

    val STATION_KEY = BuildConfig.STATION_KEY

    //	uploadUrl: debug ? 'http://api.jsvolunteer.org/web/uploadfj' : 'http://api.jsvolunteer.org/web/uploadfj',
//    uploadVideoUrl: debug ? 'http://testapi.jsvolunteer.org/platform/videoupload' : 'http://api.jsvolunteer.org/platform/videoupload',

    val UPLOADURL = BuildConfig.UPLOADURL

    val WEBVIEW_URL = BuildConfig.WEBVIEW_URL


    val PATTERN_URL = "home/good_man_list"
    val ARTICLE_URL = "home/theory_study/article_details?id="
    val VIDEO_URL = "home/micro_listen/video_details?id="
    val ACT_REPORT = "home/practical_activity/act_report_details?report_id="

    val ARTICLE = "article"
    val VIDEO = "video"
    val PLATFORM = "platform"
    val PRACTICE = "practice"
    val STATION = "station"
    val POLYMERIZE = "polymerize"
    val PATTERN = "pattern"
    val CULTURE = "culture"
    val BANNER = "banner"

    val LOCATION = 0  //选择当前位置
    val SEARCH_LOCATION = 1 //选择搜索位置
    val LOGIN_VOL_SUCCESS = 2 //志愿者登录成功
    val LOGIN_ORG_SUCCESS = 3 //志愿组织登录成功
    val LOGIN_EXIT = 4 //退出登录
    val PERFECT_INFO_SUCCESS = 5 //完善信息成功
    val VOL_EDIT_TEL = 6 //修改手机号
    val VOL_EDIT_PWD = 7 //修改密码
    val GO_LOGIN_VOL = 8 //志愿者登录
    val GO_LOGIN_ORG = 9 //志愿组织登录
    val PERFECT_ORG_SUCCESS = 10 //完善组织信息成功
    val EXIT_ORG = 11 //退出组织
    val ACTS_PIC_UPLOAD = 12 //活动图片操作
    val ORG_MANAGE = 13 //挂靠组织操作
    val ZYZ_INFO = 14 //志愿者信息详情

}
