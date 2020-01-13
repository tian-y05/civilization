package com.wmsj.baselibs.bean

/**
 * Created by tian
on 2019/8/8.
 */
data class OrgCataBean(
        var cate_name: String,
        var id: Int,
        var son: List<OrgSon>,
        var select_id: Int,
        var select_lastid: Int,
        var select_name: String
) {
    override fun toString(): String {
        return "OrgCataBean(cate_name='$cate_name', id=$id, son=$son, select_id=$select_id, select_name='$select_name',select_lastid='$select_lastid')"
    }
}

data class OrgSon(
        var cate_createtime: Int,
        var cate_name: String,
        var cate_parent: Int,
        var cate_sort: Int,
        var cate_state: Int,
        var id: Int,
        var p_id: Int,
        var last: List<OrgSonList>
) {
    override fun toString(): String {
        return "OrgSon(cate_createtime=$cate_createtime, cate_name='$cate_name', cate_parent=$cate_parent, cate_sort=$cate_sort, cate_state=$cate_state, id=$id, p_id=$p_id, last=$last)"
    }
}

data class OrgSonList(
        var cate_createtime: Int,
        var cate_name: String,
        var cate_parent: Int,
        var cate_sort: Int,
        var cate_state: Int,
        var id: Int,
        var p_id: Int
) {
    override fun toString(): String {
        return "OrgSonList(cate_createtime=$cate_createtime, cate_name='$cate_name', cate_parent=$cate_parent, cate_sort=$cate_sort, cate_state=$cate_state, id=$id, p_id=$p_id)"
    }
}