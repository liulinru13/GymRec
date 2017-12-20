package com.mmrx.gymrec.ui.framework

/**
 * 页面管理接口
 * Created by mmrx on 17/12/16.
 */
interface IPageManager {

    //返回上一级页面
    fun goBack(): Boolean
    //返回指定页面
    fun goBackTo(pageId: String)
    //进入指定页面
    fun gotoPage(pageLayoutId: Int,map: HashMap<String,*>?)
    //标题栏响应
    fun onTitleBarAction(action: EnumPageTitleType?): Boolean
}