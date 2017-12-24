package com.mmrx.gymrec.ui.framework

import android.content.Context
import android.view.View
import com.mmrx.gymrec.ui.createViewByLayouytId

/**
 * 实现类，为了减少页面无用接口实现过多
 * Created by liulinru on 2017/12/21.
 */
abstract class PageContentImp(val context: Context,val layoutId: Int):IPageContent {

    protected val rootView: View
    protected var manager: IPageManager? = null
    init{
        rootView = createViewByLayouytId(layoutId, context)
    }

    abstract fun init()
    override fun onForground(){}
    override fun <T : Any> receiveParam(map: HashMap<String, T>) {}
    override fun onBackGround() {}
    override fun onRemove() {}
    override fun getContentView(): View {
        return rootView
    }

    override fun setIPageManager(manager: IPageManager) {
        this.manager = manager
    }

    override fun innerBack(): Boolean {
        return false
    }

    override fun floatingButtonVisiable(): Boolean {
        return false
    }

    override fun buildTitleBar(): PageTitleStruct? {
        return null
    }

    override fun onTitleBarAction(action: EnumPageTitleType?): EnumPageTitleAction? {
        return null
    }
}