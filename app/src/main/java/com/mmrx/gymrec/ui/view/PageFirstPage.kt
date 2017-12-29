package com.mmrx.gymrec.ui.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.daimajia.swipe.SimpleSwipeListener
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.adapters.BaseSwipeAdapter
import com.mmrx.gymrec.R
import com.mmrx.gymrec.bean.model.TrainRecBean
import com.mmrx.gymrec.bean.table.TrainTable
import com.mmrx.gymrec.db.GymDbHelper
import com.mmrx.gymrec.db.IDBActiveListener
import com.mmrx.gymrec.ui.framework.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.page_first_page.view.*
import kotlinx.android.synthetic.main.view_first_page_list_item.view.*

/**
 * page_first_page.xml
 * Created by mmrx on 17/12/16.
 */
class PageFirstPage :PageContentImp, IDBActiveListener,AdapterView.OnItemClickListener {
    private val adapter: TrainRecAdapter by lazy { TrainRecAdapter(mutableListOf()) }

    constructor(context: Context, layoutId: Int) : super(context, layoutId)

    override fun init() {
        rootView.listView.adapter = adapter
        rootView.listView.onItemClickListener = this
    }

    override fun onForground() {
        GymDbHelper.getInstance(context).addDBActiveListener(this)
        val tempList = GymDbHelper.getInstance(context).getTrainRecData()
        adapter.updateDataList(tempList)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(adapter.count > position){
            var item = adapter.getItemBeanByIndex(position)
            if(item != null){
                val map = HashMap<String,Int>()
                map.put(TrainTable.ID,item.id)
                manager?.gotoPage(R.layout.page_train_subject,map)
            }
        }
    }




    override fun dbUpdate(tableName: String) {
        if(TrainTable.NAME == tableName){
            val tempList = GymDbHelper.getInstance(context).getTrainRecData()
            adapter.updateDataList(tempList)
        }
    }

    override fun <T : Any> receiveParam(map: HashMap<String, T>) {
    }

    override fun onBackGround() {
        GymDbHelper.getInstance(context).removeDBActiveListener(this)
    }

    override fun onRemove() {
    }


    override fun getPageLevel(): Int {
        return Page.getPageLevelByName(Page.PAGE_KEY_FIRST_PAGE)
    }

    override fun floatingButtonVisiable(): Boolean {
        return true
    }

    override fun buildTitleBar(): PageTitleStruct? {
        return PageTitleStruct(EnumPageTitleType.LEFT_PERSON_CENTER
                ,getContentView().context.getString(R.string.app_title)
                ,null)
    }

    override fun onTitleBarAction(action: EnumPageTitleType?): EnumPageTitleAction? {
        return EnumPageTitleAction.PAGE_TITLE_ACTION_PERSON_CENTER
    }

    inner class TrainRecAdapter:BaseSwipeAdapter{
        private val dataList: MutableList<TrainRecBean> = mutableListOf()
        constructor(dataList: List<TrainRecBean>) : super(){
            this.dataList.addAll(dataList)
        }

        override fun getItem(position: Int): Any? {
            if(dataList.size > position)
                return dataList[position]
            return null
        }

        override fun getSwipeLayoutResourceId(position: Int): Int {
            return R.id.swipeView
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return dataList.size
        }

        override fun generateView(position: Int, parent: ViewGroup?): View {
            val view = LayoutInflater.from(context)
                    .inflate(R.layout.view_swipe_list_item_first_page,null)
            if(view is SwipeLayout){
                view.addSwipeListener(object : SimpleSwipeListener(){
                    override fun onOpen(layout: SwipeLayout?) {
                        YoYo.with(Techniques.Tada).duration(500)
                                .delay(100).playOn(layout?.findViewById(R.id.swipeViewDeleteBn))
                    }
                })
            }
            val delBn = view.findViewById<View>(R.id.swipeViewDeleteBn)
            delBn.setOnClickListener({_->
                //删除
                if(position != -1 && adapter.count > position){
                    var item = adapter.getItemBeanByIndex(position)
                    item.let {
                        GymDbHelper.getInstance(context).deleteTrainRec(item!!.id)
                    }
                }
            })
            val holder = RecViewHolder(view)
            view.tag = holder
            return view
        }

        override fun fillValues(position: Int, convertView: View?) {
            if(dataList.size < position)
                return
            val bean = dataList[position]
            val dateTime = bean.dataTime.split("+")
            var showTime: String = ""
            if(dateTime.size != 2){
                showTime = ""
            }else{
                showTime = dateTime[0] + " " + dateTime[1]
            }
            val holder = convertView?.tag as RecViewHolder

            holder.trainRecTitle?.text = bean.title
            holder.trainRecTime?.text = showTime
            holder.trainMarking?.text = bean.marking.toString()
            if(bean.recIcon != null && bean.recIcon != -1)
                holder.trainRecIcon?.setImageResource(bean.recIcon)
            bean.items.let {
                for (i in bean.items!!) {

                }
            }
        }

        fun getItemBeanByIndex(index: Int):TrainRecBean?{
            if(dataList.size > index)
                return dataList[index]
            return null
        }


        fun updateDataList(dataList: List<TrainRecBean>){
            this.dataList.clear()
            this.dataList.addAll(dataList)
            notifyDataSetChanged()
        }
    }

    class RecViewHolder{
        var trainRecIcon: CircleImageView? = null
        var trainRecTitle: TextView? = null
        var trainRecTime: TextView? = null
        var trainMarking: TextView? = null
        constructor(view: View){
            trainRecIcon = view.trainRecItemIcon
            trainRecTitle = view.trainRecItemTitle
            trainRecTime = view.trainRecItemTime
            trainMarking = view.trainRecItemMarking
        }
    }
}