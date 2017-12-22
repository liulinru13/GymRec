package com.mmrx.gymrec.ui.view

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.mmrx.gymrec.R
import com.mmrx.gymrec.bean.model.TrainBean
import com.mmrx.gymrec.bean.table.TrainTable
import com.mmrx.gymrec.db.GymDbHelper
import com.mmrx.gymrec.ui.*
import com.mmrx.gymrec.ui.framework.*
import kotlinx.android.synthetic.main.page_train_subject.view.*

/**
 * Created by mmrx on 17/12/17.
 */
class PageTrainSubject : PageContentImp,View.OnClickListener{

    private var dialog: MaterialDialog? = null
    private var popSaveNewDialog = false //在新建计划情况下，且没有新增训练项目，在后退时需要弹出对话框确认是否要保存
    private var trainRecBean = TrainBean(-1,"","","",-1,-1)
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
            rootView.trainSubjectDate.id -> trainSubjectDateDialogLogic()
            rootView.trainSubjectTime.id -> trainSubjectTimeDialogLogic()
        }
    }

    private fun trainSubjectDateDialogLogic(){
        val (year,month,day) = splitDate(rootView.trainSubjectDate.text.toString())
        dialog = createDialogWithDatePicker(context,
                getStringByResId(context,R.string.train_subj_date_dialog_title),
                object :IutilDialogCallBack{
                    override fun <T : Any> onClick(t: T?) {
                        if(t is String)
                            rootView.trainSubjectDate.text = t
                    }
                },year,month, day)
        dialog?.show()
    }

    private fun trainSubjectTimeDialogLogic(){
        val (hour,minute) = splitTime(rootView.trainSubjectTime.text.toString())
        dialog = createDialogWithTimePicker(context,
                getStringByResId(context,R.string.train_subj_date_dialog_title),
                object :IutilDialogCallBack{
                    override fun <T : Any> onClick(t: T?) {
                        if(t is String)
                            rootView.trainSubjectTime.text = t
                    }
                },hour,minute)
        dialog?.show()
    }

    private fun trainSubjectNameDialogLogic(){
        dialog = createDialogWithEdit(context,
                getStringByResId(context,R.string.train_subj_name_dialog_title),object :IutilDialogCallBack{
            override fun <T : Any> onClick(t: T?) {
                if(t is String)
                    rootView.trainSubjectName.text = t
            }
        })
        dialog?.show()
    }

    override fun onForground() {
        var (year,month,day) = getSystemDate()
        var (hour,minute) = getSystemTime()
        var date = "$year-$month-$day"
        var time = "$hour:$minute"
        if(trainRecBean._id == -1){
            //新建的，需要在退出时候弹出对话框
            popSaveNewDialog = true
            trainRecBean.train_date = date + "+" +time
            trainRecBean._id = GymDbHelper.getInstance(context)
                    .insertNewTrainRec(trainRecBean.train_date,"")
        }else{
            popSaveNewDialog = false
            val dateTime = trainRecBean.train_date.split("+")
            if(dateTime.size == 2) {
                date = dateTime[0]
                time = dateTime[1]
            }
            updateList()
        }
        //更新ui
        if(!TextUtils.isEmpty(trainRecBean.train_subject))
            rootView.trainSubjectName.text = trainRecBean.train_subject
        rootView.trainSubjectDate.text = date
        rootView.trainSubjectTime.text = time
        if(!TextUtils.isEmpty(trainRecBean.advice))
            rootView.trainSubjectAdviceEdit.setText(trainRecBean.advice)
    }

    private fun updateList(){}

    private fun splitTime(time: String):Pair<Int,Int>{
        if(time.contains(":")) {
            var (hour,minute) = time.split(":")
            return Pair(hour.toInt(),minute.toInt())
        }
        return getSystemTime()
    }

    private fun splitDate(date: String):Triple<Int,Int,Int>{
        if(date.contains("-")) {
            var (year, month, day) = date.split("-")
            return Triple(year.toInt(),month.toInt(),day.toInt())
        }
        return getSystemDate()
    }

    override fun <T : Any> receiveParam(map: HashMap<String, T>) {
        if(map[TrainTable.ID] != null) {
            trainRecBean._id = map[TrainTable.ID] as Int
            var temp = GymDbHelper.getInstance(context).queryTrainResById(trainRecBean._id)
            if(temp != null){
                trainRecBean = temp
            }else{
                trainRecBean._id = -1
                Toast.makeText(context,"${map[TrainTable.ID]}在表中未找到",Toast.LENGTH_SHORT)
            }
        }
    }

    override fun onBackGround() {
        saveTrainInfo()
        popSaveNewDialog = false
    }

    override fun onRemove() {
        if(popSaveNewDialog) saveTrainInfo()
    }

    private fun saveTrainInfo(){
        Thread({
            trainRecBean.advice = rootView.trainSubjectAdviceEdit.text.toString()
            trainRecBean.train_date = rootView.trainSubjectDate.text.toString() +
                    "+" + rootView.trainSubjectTime.text.toString()
            trainRecBean.train_subject = rootView.trainSubjectTime.text.toString()
            trainRecBean.train_marking = 80
            trainRecBean.train_icon = R.drawable.person_center
            GymDbHelper.getInstance(context).updateTrainRec(trainRecBean)
        }).start()
    }

    override fun innerBack(): Boolean {
        if(dialog?.isShowing ?: false){
            dialog?.dismiss()
            return true
        }
        if(popSaveNewDialog){
            dialog = createDialogNormal(context,
                    getStringByResId(context,R.string.notice),
                    getStringByResId(context,R.string.train_subj_save_dialog_content),
                    object: IutilDialogCallBack{
                        override fun <T : Any> onClick(t: T?) {
                            saveTrainInfo()
                            popSaveNewDialog = false
                        }
                    })
            dialog?.show()
            return true
        }
        return false
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