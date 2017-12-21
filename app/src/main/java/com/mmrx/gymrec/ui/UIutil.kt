package com.mmrx.gymrec.ui

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.mmrx.gymrec.R
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

fun getStringByResId(context: Context,id: Int):String{
    return context.resources.getString(id)
}

interface IutilDialogCallBack{
    fun <T:Any>onClick(t:T)
}
fun createDialogWithEdit(context: Context,title: String,
                         positiveCallBack: IutilDialogCallBack?): MaterialDialog{
    val editLayout = createViewByLayouytId(R.layout.view_edit_tv, context)
    return MaterialDialog.Builder(context)
            .title(title)
            .customView(editLayout,false)
            .positiveText(R.string.ok)
            .negativeText(R.string.cancel)
            .onPositive({dialog,_ ->
                    val content = editLayout.findViewById<EditText>(R.id.viewEditText).text.toString()
                    if(!TextUtils.isEmpty(content)){
                        positiveCallBack?.onClick(content)
                        dialog.dismiss()
                    }
            })
            .onNegative({dialog, _ ->dialog.dismiss() })
            .build()
}

