package com.mmrx.gymrec.ui

import android.content.Context
import android.text.TextUtils
import android.text.format.Time
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.mmrx.gymrec.R
import com.mmrx.gymrec.ui.framework.IPageContent
import kotlinx.android.synthetic.main.view_date_picker.view.*
import kotlinx.android.synthetic.main.view_time_picker.view.*
import java.util.*

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
    fun <T:Any>onClick(t:T?)
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

fun getSystemDate():Triple<Int,Int,Int>{
    val c = Calendar.getInstance()
    return Triple(c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH))
}

fun getSystemTime():Pair<Int,Int>{
    val t = Time()
    t.setToNow()
    return Pair(t.hour,t.minute)
}

fun createDialogNormal(context: Context,title: String,
                       content: String,positiveCallBack: IutilDialogCallBack,
                       negativeCallBack: IutilDialogCallBack): MaterialDialog{
    return MaterialDialog.Builder(context)
            .title(title)
            .content(content)
            .positiveText(android.R.string.ok)
            .negativeText(android.R.string.cancel)
            .onPositive { dialog, which ->
                dialog.dismiss()
                positiveCallBack.onClick(null)
            }
            .onNegative { dialog, which ->
                dialog.dismiss()
                negativeCallBack.onClick(null)
            }
            .build()
}

fun createDialogWithDatePicker(context: Context,title: String,
                               positiveCallBack: IutilDialogCallBack?,
                               year: Int = -1,month: Int = -1,day: Int = -1): MaterialDialog{
    val datePickerLayout = createViewByLayouytId(R.layout.view_date_picker, context)
    if(year == -1 || month == -1 || day == -1){
        val (y,m,d) = getSystemDate()
        datePickerLayout.datePicker.init(y,m,d, null)
    }else{
        datePickerLayout.datePicker.init(year,month-1,day, null)
    }
    return MaterialDialog.Builder(context)
            .title(title)
            .customView(datePickerLayout, false)
            .positiveText(android.R.string.ok)
            .negativeText(android.R.string.cancel)
            .onPositive({dialog, which ->
                val year = datePickerLayout.datePicker.year
                val month = datePickerLayout.datePicker.month +1
                val day = datePickerLayout.datePicker.dayOfMonth
                positiveCallBack?.onClick("${year}-${month}-${day}")
                dialog.dismiss()
            })
            .onNegative({dialog, _ ->dialog.dismiss() })
            .build()
}

fun createDialogWithTimePicker(context: Context,title: String,
                               positiveCallBack: IutilDialogCallBack?,
                               hour: Int = -1,minute: Int = -1): MaterialDialog{
    val timePickerLayout = createViewByLayouytId(R.layout.view_time_picker, context)
    timePickerLayout.timePicker.setIs24HourView(true)
    if(hour == -1 || minute == -1){
        val (h,m) = getSystemTime()
        timePickerLayout.timePicker.currentHour = h
        timePickerLayout.timePicker.currentMinute = m
    }else{
        timePickerLayout.timePicker.currentHour = hour
        timePickerLayout.timePicker.currentMinute = minute
    }
    return MaterialDialog.Builder(context)
            .title(title)
            .customView(timePickerLayout, false)
            .positiveText(android.R.string.ok)
            .negativeText(android.R.string.cancel)
            .onPositive({dialog, which ->
                val hour = timePickerLayout.timePicker.currentHour
                val minute = timePickerLayout.timePicker.currentMinute
                positiveCallBack?.onClick("${hour}:${minute}")
                dialog.dismiss()
            })
            .onNegative({dialog, _ ->dialog.dismiss() })
            .build()
}

