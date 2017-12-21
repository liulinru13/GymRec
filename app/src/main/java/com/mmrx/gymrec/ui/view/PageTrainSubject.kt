package com.mmrx.gymrec.ui.view

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.mmrx.gymrec.R
import com.mmrx.gymrec.ui.IutilDialogCallBack
import com.mmrx.gymrec.ui.createDialogWithEdit
import com.mmrx.gymrec.ui.createViewByLayouytId
import com.mmrx.gymrec.ui.framework.*
import com.mmrx.gymrec.ui.getStringByResId
import kotlinx.android.synthetic.main.page_train_subject.view.*
import kotlinx.android.synthetic.main.view_edit_tv.view.*

/**
 * Created by mmrx on 17/12/17.
 */
class PageTrainSubject : PageContentImp,View.OnClickListener{

    private var dialog: MaterialDialog? = null
    private var popSaveNewDialog = false //在新建计划情况下，且没有新增训练项目，在后退时需要弹出对话框确认是否要保存

    constructor(context: Context, layoutId: Int) : super(context, layoutId)

    override fun init() {
        rootView.trainSubjectName.setOnClickListener(this)
        rootView.trainSubjectNameIcon.setOnClickListener(this)
        rootView.trainSubjectDate.setOnClickListener(this)
        rootView.trainSubjectTime.setOnClickListener(this)
        rootView.trainSubjectAddNewItem.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            rootView.trainSubjectName.id -> trainSubjectNameDialogLogic()
            rootView.trainSubjectNameIcon.id -> trainSubjectNameDialogLogic()

        }
    }

    private fun trainSubjectNameDialogLogic(){
        dialog = createDialogWithEdit(context,
                getStringByResId(context,R.string.train_subj_name_dialog_title),object :IutilDialogCallBack{
            override fun <T : Any> onClick(t: T) {
                if(t is String)
                    rootView.trainSubjectName.text = t
            }
        })
        dialog?.show()
    }

    override fun onForground() {
    }

    override fun <T : Any> receiveParam(map: HashMap<String, T>) {
    }

    override fun onBackGround() {
    }

    override fun onRemove() {
    }

    override fun getPageLevel(): Int {
        return Page.getPageLevelByName(Page.PAGE_KEY_TRAIN_SUBJECT)
    }

    override fun floatingButtonVisiable(): Boolean {
        return false
    }

    override fun buildTitleBar(): PageTitleStruct? {
        return PageTitleStruct(EnumPageTitleType.LEFT_BACK
                ,rootView.resources.getString(R.string.train_subj_page_title)
                ,null)
    }
    override fun onTitleBarAction(action: EnumPageTitleType?): EnumPageTitleAction? {
        return EnumPageTitleAction.PAGE_TITLE_ACTION_GO_BACK
    }
}