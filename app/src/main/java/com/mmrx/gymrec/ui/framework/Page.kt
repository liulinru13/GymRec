package com.mmrx.gymrec.ui.framework

import android.content.Context
import android.view.View
import com.mmrx.gymrec.ui.createIPageContentByLayouytId

/**
 * 页面管理类，一个对象表示一个页面
 * Created by mmrx on 17/12/16.
 */
class Page constructor(val context: Context,val layoutResId: Int) : IPage {


    companion object {
        //页面名称和栈等级映射关系
        private val pageMap: HashMap<String,Int> = HashMap()
        val PAGE_LEVEL_INVALID = -1 // 非法页面等级
        val PAGE_KEY_FIRST_PAGE = "first_page"//首页
        val PAGE_KEY_TRAIN_SUBJECT = "train_subject_page"//训练计划页

        fun getPageLevelByName(key: String):Int{
            return pageMap.get(key) ?: PAGE_LEVEL_INVALID
        }
    }
    init {
        pageMap.put(PAGE_KEY_FIRST_PAGE,5)
        pageMap.put(PAGE_KEY_TRAIN_SUBJECT,10)
    }
    var view : IPageContent? = null

    init {
        view = createIPageContentByLayouytId(layoutResId, context)
    }

    override fun onForground() {
        view?.onForground()
    }

    override fun <T : Any> receiveParam(map: HashMap<String, T>) {
        view?.receiveParam(map)
    }

    override fun onBackGround() {
        view?.onBackGround()
    }

    override fun onRemove() {
        view?.onRemove()
    }

    override fun getContentView(): View {
        return view as View
    }

    override fun getPageLevel(): Int {
        return view?.getPageLevel() ?: PAGE_LEVEL_INVALID
    }

    override fun floatingButtonVisiable(): Boolean {
        return view?.floatingButtonVisiable() ?: false
    }

    override fun buildTitleBar(): PageTitleStruct? {
        return view?.buildTitleBar() ?: null
    }

    override fun onTitleBarAction(action: EnumPageTitleType?): EnumPageTitleAction? {
        return view?.onTitleBarAction(action) ?: null
    }
}