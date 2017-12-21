package com.mmrx.gymrec.ui.framework

import android.content.Context
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.ViewGroup
import com.mmrx.gymrec.R
import java.util.*
import kotlin.collections.HashMap

/**
 * 管理页面队列
 * Created by mmrx on 17/12/16.
 */
class PageQueue constructor(val context: Context,val rootView: ViewGroup) : IPageManager {

    constructor(context: Context,rootView: ViewGroup,floatListener: IFloatingButtonListener
                ,toolBar: Toolbar )
            :this(context, rootView){
        this.floatListener = floatListener
        this.toolBar = toolBar
    }
    //页面栈
    val pageQueue: Stack<IPage> = Stack()
    var floatListener: IFloatingButtonListener? = null
    var toolBar: Toolbar? = null

    override fun goBack(): Boolean{
        var topPage: IPage? = null
        var showPage: IPage? = null
        if(pageQueue.size  <= 1) {
            return false
        }
        topPage = pageQueue.pop();
        showPage = pageQueue.peek();

        topPage.onBackGround()
        topPage.onRemove()
        rootView.removeAllViews()
        if(showPage.getContentView().parent != null){
            (showPage.getContentView().parent as ViewGroup)
                    .removeView(showPage.getContentView())
        }
        floatListener?.setVisiable(showPage.floatingButtonVisiable())
        buildTitleBar(showPage.buildTitleBar())
        rootView.addView(showPage.getContentView())
        showPage.onForground()
        return true
    }

    override fun goBackTo(pageId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun gotoPage(pageLayoutId: Int, map: HashMap<String, *>?) {
        var topPage: IPage? = null
        var newPage: IPage = Page(context, pageLayoutId)
        if(pageQueue.size > 0){
            topPage = pageQueue.peek()
        }
        topPage?.onBackGround();
        val topPageLevel = topPage?.getPageLevel() ?: Page.PAGE_LEVEL_INVALID
        //新增页面的栈等级大于栈顶页面
        //或者相等，栈顶页弹栈
        if(newPage.getPageLevel() >= topPageLevel) {
            topPage?.onRemove()
        }else if(pageQueue.size > 0){
            pageQueue.pop()
            topPage?.onRemove()
            //反复弹栈，直到栈顶页等级小于待压栈页
            while (pageQueue.size > 0){
                pageQueue.peek().onRemove()
            }
        }
        floatListener?.setVisiable(newPage.floatingButtonVisiable())
        //压栈
        pageQueue.push(newPage)
        rootView.removeAllViews()
        buildTitleBar(newPage.buildTitleBar())
        rootView.addView(newPage.getContentView())
        if(map != null)
            newPage.receiveParam(map)
        newPage.onForground()
    }

    fun buildTitleBar(struct: PageTitleStruct?){
        struct?.let {
            //左侧
            when(it.leftType) {
                EnumPageTitleType.LEFT_BACK ->
                    toolBar?.setNavigationIcon(R.drawable.arrow_white_left_18dp)
                EnumPageTitleType.LEFT_PERSON_CENTER ->
                    toolBar?.setNavigationIcon(R.drawable.person_center_white_18dp)
                else ->
                    toolBar?.setNavigationIcon(R.drawable.person_center_white_18dp)
            }
            //右侧
//            when(it.rightType) {
//            }
            if(!TextUtils.isEmpty(it.title)){
                toolBar?.title = it.title
            }
        }
    }

    override fun onTitleBarAction(action: EnumPageTitleType?): Boolean{
        var topPage: IPage? = null
        if(pageQueue.size > 0){
            topPage = pageQueue.peek()
        }
        val action = topPage?.onTitleBarAction(action) ?: null
        return when(action){
            EnumPageTitleAction.PAGE_TITLE_ACTION_GO_BACK -> goBack()
            EnumPageTitleAction.PAGE_TITLE_ACTION_PERSON_CENTER -> true
            else -> false
        }
    }

    interface IFloatingButtonListener{
        fun setVisiable(visiable: Boolean)
    }
}