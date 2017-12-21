package com.mmrx.gymrec.ui.framework

import android.content.Context
import android.view.View
import com.mmrx.gymrec.R
import com.mmrx.gymrec.ui.view.PageFirstPage
import com.mmrx.gymrec.ui.view.PageTrainSubject

/**
 * 页面管理类，一个对象表示一个页面
 * Created by mmrx on 17/12/16.
 */
class Page constructor(val context: Context,val layoutResId: Int) : IPage {


    companion object {
        //页面名称和栈等级映射关系
        private val pageLevelMap: HashMap<String,Int> = HashMap()
        private val pageMap: HashMap<Int,(Context, Int) -> PageContentImp> = HashMap()
        val PAGE_LEVEL_INVALID = -1 // 非法页面等级
        val PAGE_KEY_FIRST_PAGE = "first_page"//首页
        val PAGE_KEY_TRAIN_SUBJECT = "train_subject_page"//训练计划页

        fun getPageLevelByName(key: String):Int{
            return pageLevelMap[key] ?: PAGE_LEVEL_INVALID
        }
        fun getPageByResId(key: Int):((Context, Int) -> PageContentImp )?{
            if(!pageMap.containsKey(key))
                throw ExceptionInInitializerError("页面初始化异常")
            return pageMap[key]
        }
    }
    init {
        pageLevelMap.put(PAGE_KEY_FIRST_PAGE,5)
        pageLevelMap.put(PAGE_KEY_TRAIN_SUBJECT,10)

        pageMap.put(R.layout.page_first_page,::PageFirstPage)
        pageMap.put(R.layout.page_train_subject,::PageTrainSubject)
    }
    private val view : PageContentImp

    init {
        val kclass = getPageByResId(layoutResId)
        view = newPage(context,layoutResId,kclass!!)
    }

    fun newPage(context: Context,layoutId: Int, factory: (Context, Int) -> PageContentImp) : PageContentImp{
        return factory(context,layoutId)
    }

    override fun onForground() {
        view.init()
        view.onForground()
    }

    override fun <T : Any> receiveParam(map: HashMap<String, T>) {
        view.receiveParam(map)
    }

    override fun onBackGround() {
        view.onBackGround()
    }

    override fun onRemove() {
        view.onRemove()
    }

    override fun getContentView(): View {
        return view.getContentView()
    }

    override fun getPageLevel(): Int {
        return view.getPageLevel()
    }

    override fun floatingButtonVisiable(): Boolean {
        return view.floatingButtonVisiable()
    }

    override fun buildTitleBar(): PageTitleStruct? {
        return view.buildTitleBar()
    }

    override fun onTitleBarAction(action: EnumPageTitleType?): EnumPageTitleAction? {
        return view.onTitleBarAction(action)
    }
}