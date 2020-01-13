package com.wmsj.baselibs.retrofit.api

import com.wmsj.baselibs.retrofit.BaseResponse
import com.wmsj.baselibs.bean.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

/**
 * Created by tian
on 2019/7/2.
 */
interface ApiService {

    /**
     * 志愿者绑定
     */
    @POST("/wechat/user/binding")
    fun doLogin(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<LoginInfoBean>>

    /**
     *组织绑定
     */
    @POST("/wechat/org/bind")
    fun doOrgLogin(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<LoginInfoBean>>

    /**
     * 组织活动管理列表
     */
    @GET("/wechat/org/activitylist?")
    fun getEventsData(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<List<EventsBean>>>

    /**
     * 首页栏目列表
     */
    @GET("/web/menu-column?")
    fun getHomeColumns(@QueryMap(encoded = true) params: Map<String, Int>): Observable<BaseResponse<List<HomeColumnsBean>>>

    /**
     * 首页应用列表
     */
    @GET("/web/menu-app?")
    fun getHomeApps(): Observable<BaseResponse<List<HomeColumnsBean>>>

    /**
     * 首页轮播图
     */
    @GET("/web/ad-list?")
    fun getHomeBanner(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<List<HomeBanner>>>

    /**
     * 首页活动
     */
    @GET("/wechat/activity/lists?")
    fun getHomeNews(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<List<HomeActivityListBean>>>


    /**
     * 文章列表
     */
    @GET("/web/contents-list?")
    fun getArticleLists(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<ArticleBean>>

    /**
     * 活动一级分类
     */
    @GET("/web/polymerize/activitycate?")
    fun getActivitycate(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<List<OrgCataBean>>>

    /**
     * 活动列表
     */
    @GET("/web/polymerize/activitylist?")
    fun getActivityList(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<List<HomeActivityListBean>>>

    /**
     * 活动下级分类
     */
    @GET("/web/polymerize/sonlist?")
    fun getOrgSonList(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<List<OrgSonList>>>


    /**
     * 组织列表
     */
    @GET("/web/polymerize/orglist?")
    fun getArticleOrg(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<List<ArticileOrgList>>>

    /**
     * 组织一级分类
     */
    @GET("/web/polymerize/orgcate?")
    fun getOrgcate(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<List<OrgCataBean>>>

    /**
     * 组织下级分类
     */
    @GET("/web/polymerize/getlastcate?")
    fun getLastcate(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<List<OrgSonList>>>

    /**
     * 组织下级分类
     */
    @GET("/web/station/lists?")
    fun getStationLists(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<List<StationListBean>>>


    /**
     * 实践基地
     */
    @GET("/web/practice/lists?")
    fun getPracticeLists(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<List<PracticeBaseBean>>>

    /**
     * 所站简介相关活动
     */
    @GET("/web/station/activity-list?")
    fun getStationActivityLists(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<List<HomeActivityListBean>>>

    /**
     * 所站简介实践站
     */
    @GET("/web/station/spot-list?")
    fun getStationSpotLists(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<List<StationListBean>>>

    /**
     * 随手拍
     */
    @GET("/web/culture/lists?")
    fun getCulturePhotoLists(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<List<ConventientPhotoBean>>>

    /**
     * 图片上传
     */
    @Multipart
    @POST("/web/uploadfj?")
    fun postImage(@Part part: MultipartBody.Part): Observable<BaseResponse<ImageBean>>

    /**
     * 视频
     */

    @Multipart
    @POST("/platform/videoupload?")
    fun postVideo(@Part part: MultipartBody.Part): Observable<BaseResponse<ImageBean>>

    /**
     * 随手拍提交
     */

    @Multipart
    @POST("/web/culture/create?")
    fun cultureVideoCreate(@QueryMap(encoded = true) params: Map<String, String>, @Part part: MultipartBody.Part): Observable<BaseResponse<Any>>

    /**
     * 随手拍提交
     */

    @POST("/web/culture/create?")
    fun culturePhotoCreate(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<Any>>

    /**
     * 客户端应用权限
     */

    @GET("/web/clients/auth?")
    fun getClientsAuth(): Observable<BaseResponse<Any>>

    /**
     * 获取版本信息
     */

    @GET("/platform/app/list?")
    fun getVersion(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<VersionBean>>


    /**
     * 所站简介相关活动
     */
    @POST("/wechat/activity/show?")
    fun getActivityShow(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<ActivityShowBean>>

    /**
     * 组织详情
     */
    @GET("/wechat/user/neworginfo?")
    fun getOrgInfo(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<OrgBaseInfoBean>>

    /**
     * 组织列表
     */
    @GET("/web/polymerize/org-activity?")
    fun getOrgActivityList(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<List<HomeActivityListBean>>>

    /**
     * 获取验证码
     */
    @GET("/wechat/user/send?")
    fun getUserSend(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<Any>>

    /**
     * 志愿者注册
     */
    @POST("/wechat/user/regist?")
    fun doRegist(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<RegistBean>>

    /**
     * 志愿者忘记密码
     */
    @POST("/wechat/user/forgetpwd?")
    fun doForgetpwd(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<Any>>

    /**
     * 志愿组织忘记密码
     */
    @POST("/wechat/org/forgetpwd?")
    fun doOrgForgetpwd(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<Any>>


    /**
     * 志愿者手机号验证
     */
    @POST("/wechat/user/gphone?")
    fun verificationPhone(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<Any>>

    /**
     * 匹配身份信息
     */
    @GET("/wechat/user/mach-code?")
    fun machIdentityInfo(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<List<IdentityInfoBean>>>

    /**
     * 基础信息 政治面貌，国家，民族，文化程度、服务类型，服务对象分类
     */
    @POST("/wechat/user/getbase?")
    fun getBaseInfo(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<List<BaseInfoBean>>>

    /**
     * 获取地区
     */
    @GET("/wechat/user/getarea?")
    fun getArea(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<List<AreaBean>>>

    /**
     * 完善基本信息
     */
    @POST("/wechat/user/complete?")
    fun completeBaseInfo(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<Any>>

    /**
     * 删除信息，重新注册
     */
    @POST("/wechat/user/del-match?")
    fun deleteAccount(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<Any>>

    /**
     * 绑定身份
     */
    @POST("/wechat/user/del-other?")
    fun bindIdentity(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<Any>>

    /**
     * 重置密码
     */
    @POST("/wechat/user/reset-pwd?")
    fun resetPwd(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<Any>>

    /**
     * 获取志愿者信息
     */
    @POST("/wechat/user/userinfo?")
    fun getUserInfo(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<UserInfo>>

    /**
     * 志愿者中心
     */
    @POST("/wechat/user/centre?")
    fun getUserCentre(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<UserCentreBean>>

    /**
     * 志愿者中心
     */
    @POST("/wechat/user/myactivity?")
    fun getMyActivity(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<List<MyActivityBean>>>

    /**
     * 服务详情
     */
    @POST("/wechat/user/service?")
    fun getActivityService(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<ActivityService>>


    /**
     * 签到
     */
    @GET("/wechat/user/card?")
    fun checkCard(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<ClockCardBean>>

    /**
     * 志愿者取消报名
     */
    @POST("/wechat/user/removesign?")
    fun removeSign(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<Any>>

    /**
     * 志愿者-我的组织
     */
    @GET("/wechat/user/myorg?")
    fun myOrgList(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<List<MyOrgList>>>

    /**
     * 志愿者-我的组织详情
     */
    @GET("/wechat/user/orginfo?")
    fun myOrgInfo(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<OrgBaseInfoBean>>

    /**
     * 我的随手拍列表
     */
    @GET("/wechat/user/culture?")
    fun myTakePhotoList(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<List<MyTakePhotoBean>>>

    /**
     * 我的随手拍列表
     */
    @POST("/web/culture/del?")
    fun myTakePhotoDelete(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<Any>>

    /**
     * 志愿者服务时长记录
     */
    @GET("/wechat/user/services-list?")
    fun serviceRecordList(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<ServiceRecordBean>>

    /**
     * 系统通知
     */
    @POST("/wechat/user/news?")
    fun systemNews(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<List<SystemNewsBean>>>

    /**
     * 修改手机号
     */
    @POST("/wechat/user/change-mobile?")
    fun editTel(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<Any>>

    /**
     * 活动报名
     */
    @POST("/wechat/user/joinin?")
    fun joinActivity(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<Any>>

    /**
     * 组织注册
     */
    @POST("/wechat/org/register?")
    fun orgRegister(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<Any>>

    /**
     * 主管组织
     */
    @GET("/wechat/user/getorg?")
    fun getOrg(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<List<OrgListBean>>>

    /**
     * 完善组织基本信息
     */
    @POST("/wechat/org/org-complete?")
    fun completeOrgInfo(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<Any>>

    /**
     * 加入组织
     */
    @POST("/wechat/user/joinorg?")
    fun joinOrg(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<Any>>

    /**
     * 挂靠组织
     */
    @GET("/wechat/org/hangupop?")
    fun applyOrg(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<Any>>

    /**
     * 活动发布
     */
    @POST("/wechat/org/pubactivity?")
    fun publishActs(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<Any>>

    /**
     * 组织修改手机号
     */
    @GET("/wechat/org/change-mobile?")
    fun orgChangeMobile(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<Any>>

    /**
     * 退出组织
     */
    @POST("/wechat/user/outorg?")
    fun exitOrg(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<Any>>

    /**
     * 撤回草稿
     */
    @GET("/wechat/org/activitystate?")
    fun backDraft(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<Any>>

    /**
     * 活动发布 删除
     */
    @GET("/wechat/org/activitydel?")
    fun actsManage(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<Any>>

    /**
     * 组织活动取消报名 录用  1:取消报名，2：录用，3：拒绝
     */
    @GET("/wechat/org/activitystateupdate?")
    fun orgActsState(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<Any>>

    /**
     * 组织招募待审核人数
     */
    @GET("/wechat/org/activityperson?")
    fun orgActsPerson(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<ActsPerson>>

    /**
     * 组织成员列表
     */
    @GET("/wechat/org/member?")
    fun orgMember(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<List<OrgMemberBean>>>

    /**
     * 组织录用
     */
    @POST("/wechat/org/employment?")
    fun orgEmployment(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<Any>>

    /**
     * 活动图片上传
     */
    @POST("/wechat/org/activitypicupload?")
    fun actsPicUpload(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<Any>>


    /**
     * 组织成员管理列表
     */
    @GET("/wechat/org/managelist?")
    fun orgManageMember(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<List<OrgMemberManageBean>>>

    /**
     * 加入审核移除组织
     */
    @GET("/wechat/org/check?")
    fun orgMemberCheck(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<Any>>

    /**
     * 活动报道发布
     */
    @POST("/web/polymerize/push-report?")
    fun pushOrgReport(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<Any>>



    /**
     * 活动时长补录
     */
    @POST("/wechat/org/replace?")
    fun timeEnter(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<Any>>

    /**
     * 活动时长补录
     */
    @GET("/wechat/org/userinfo?")
    fun orgUserInfo(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<OrgUserInfo>>

    /**
     * 1:上级组织 -1:下级
     */
    @GET("/wechat/org/hanguplist?")
    fun hangupOrgList(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<List<HangupOrgBean>>>


    /**
     * 挂靠组织详情
     */
    @GET("/wechat/org/hangupdetail?")
    fun hangupOrgDetail(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<HangupDetailBean>>

    /**
     * 挂靠组织详情
     */
    @GET("/wechat/org/hangupop?")
    fun hangupOrgOperation(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<Any>>

    /**
     * 挂靠组织搜索
     */
    @GET("/wechat/org/hangupsearch?")
    fun hangupOrgSearch(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<List<HangupOrgBean>>>

    /**
     * 组织列表模糊查询
     */
    @GET("/wechat/activity/orglist?")
    fun searchOrgList(@QueryMap(encoded = true) params: Map<String, String>): Observable<BaseResponse<List<ArticileOrgList>>>
}