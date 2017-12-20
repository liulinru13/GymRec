package com.mmrx.gymrec.ui.framework

import android.view.View

/**
 * Created by mmrx on 17/12/16.
 */
interface IPage {
    //页面进入时的操作
    fun onForground()
    //向页面传入参数
    fun <T: Any>receiveParam(map: HashMap<String,T>)
    //页面返回后台的操作
    fun onBackGround()
    //页面销毁的操作
    fun onRemove()
    //获取当前展示的layout
    fun getContentView(): View
    //获取当前页的展示等级
    fun getPageLevel(): Int
    //悬浮按钮要不要展示
    fun floatingButtonVisiable(): Boolean
    //标题的设置
    fun buildTitleBar():PageTitleStruct?
    //标题栏响应
    fun onTitleBarAction(action: EnumPageTitleType?): EnumPageTitleAction?
}