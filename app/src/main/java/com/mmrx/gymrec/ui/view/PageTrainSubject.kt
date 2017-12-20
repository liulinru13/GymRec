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
import com.mmrx.gymrec.ui.createViewByLayouytId
import com.mmrx.gymrec.ui.framework.*
import kotlinx.android.synthetic.main.page_train_subject.view.*
import kotlinx.android.synthetic.main.view_edit_tv.view.*

/**
 * Created by mmrx on 17/12/17.
 */
class PageTrainSubject : RelativeLayout,IPageContent,View.OnClickListener{

    var dialog: MaterialDialog? = null
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        trainSubjectNameTv.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            trainSubjectNameTv.id -> {
                var title: String? = null
                val editLayout = createViewByLayouytId(R.layout.view_edit_tv, context)
                dialog = MaterialDialog.Builder(context)
                        .title(R.string.train_subj_name_dialog_title)
                        .customView(editLayout,false)
                        .positiveText(R.string.ok)
                        .negativeText(R.string.cancel)
                        .onPositive(object : MaterialDialog.SingleButtonCallback{
                            override fun onClick(dialog: MaterialDialog, which: DialogAction) {
                                title = editLayout.findViewById<EditText>(R.id.viewEditText).text.toString()
                                if(!TextUtils.isEmpty(title)){
                                    trainSubjectNameTv.setText(title)
                                    dialog.dismiss()
                                }
                            }
                        })
                        .onNegative(object :MaterialDialog.SingleButtonCallback{
                            override fun onClick(dialog: MaterialDialog, which: DialogAction) {
                                dialog.dismiss()
                            }
                        })
                        .build()
                dialog?.show()
            }
        }
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
        return Page.getPageLevelByName(Page.PAGE_KEY_TRAIN_SUBJECT)
    }

    override fun floatingButtonVisiable(): Boolean {
        return false
    }

    override fun buildTitleBar(): PageTitleStruct? {
        return PageTitleStruct(EnumPageTitleType.LEFT_BACK
                ,resources.getString(R.string.train_subj_page_title)
                ,null)
    }
    override fun onTitleBarAction(action: EnumPageTitleType?): EnumPageTitleAction? {
        return EnumPageTitleAction.PAGE_TITLE_ACTION_GO_BACK
    }
}