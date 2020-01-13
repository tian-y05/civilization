package com.wmsj.baselibs.bean

/**
 * Created by tian
on 2019/8/7.
 */
data class ArticleBean(
        val com_name: String,
        val list: List<ArticleListBean>
)

data class ArticleListBean(
        val c_id: Int,
        val col_id: Int,
        val con_author: String,
        val con_content: String,
        val con_createtime: Int,
        val con_editor: String,
        val con_files: String,
        val con_id: Int,
        val con_images: String,
        val con_links: String,
        val con_source: String,
        val con_state: Int,
        val con_title: String,
        val con_type: Int,
        val con_updatetime: Int,
        val con_userid: String,
        val con_video: String,
        val con_videourl: String,
        val id: Int,
        val p_id: Int,
        val time: String
)