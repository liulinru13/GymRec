package com.mmrx.gymrec.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.mmrx.gymrec.ui.framework.IPageContent

/**
 * Created by mmrx on 17/12/16.
 */

/**
 * 根据 layoutId 生成view
 */
fun createViewByLayouytId(layoutId: Int,context: Context): View {
    var inflater = LayoutInflater.from(context)
    val view = inflater.inflate(layoutId,null)
    return view
}

/**
 * 根据 layoutId 生成 IPageContent 对象
 */
fun createIPageContentByLayouytId(layoutId: Int,context: Context): IPageContent?{
    val view = createViewByLayouytId(layoutId, context)
    if (view is IPageContent)
        return view
    return null
}

