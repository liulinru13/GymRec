package com.mmrx.gymrec.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.mmrx.gymrec.ui.framework.IPageContent
import com.mmrx.gymrec.ui.framework.Page

/**
 * Created by mmrx on 17/12/16.
 */
class PageFirstPage :LinearLayout , IPageContent{

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
    }

    override fun onForground() {
    }

    override fun <T : Any> receiveParam(map: HashMap<String, T>) {
    }

    override fun onBackGround() {
    }

    override fun onRemove() {
    }

    override fun getContentView(): View {
        return this
    }

    override fun getPageLevel(): Int {
        return Page.getPageLevelByName(Page.PAGE_KEY_FIRST_PAGE)
    }

    override fun floatingButtonVisiable(): Boolean {
        return true
    }
}