package com.mmrx.gymrec.ui.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mmrx.gymrec.R
import com.mmrx.gymrec.bean.model.TrainRecBean
import com.mmrx.gymrec.bean.table.TrainTable
import com.mmrx.gymrec.db.GymDbHelper
import com.mmrx.gymrec.db.IDBActiveListener
import com.mmrx.gymrec.ui.framework.*
import com.yanzhenjie.recyclerview.swipe.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.page_first_page.view.*
import kotlinx.android.synthetic.main.view_first_page_list_item.view.*

/**
 * page_first_page.xml
 * Created by mmrx on 17/12/16.
 */
class PageFirstPage :PageContentImp,SwipeItemClickListener,SwipeMenuItemClickListener, IDBActiveListener {
    private val adapter: TrainRecAdapter by lazy { TrainRecAdapter(mutableListOf()) }

    constructor(context: Context, layoutId: Int) : super(context, layoutId)

    override fun init() {
        rootView.listView.layoutManager = LinearLayoutManager(context)
        rootView.listView.setSwipeItemClickListener(this)
        rootView.listView.setSwipeMenuItemClickListener(this)
        rootView.listView.setSwipeMenuCreator(MenuCreator())
        rootView.listView.adapter = adapter
    }

    override fun onForground() {
//        adapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver(){
//            override fun onChanged() {
//                super.onChanged()
//                if(rootView.listView != null && rootView.listView.adapter == null){
//                    rootView.listView.adapter = adapter
//                }
//            }
//        })
        GymDbHelper.getInstance(context).addDBActiveListener(this)
        val tempList = GymDbHelper.getInstance(context).getTrainRecData()
        adapter.updateDataList(tempList)
    }

    //删除
    override fun onItemClick(menuBridge: SwipeMenuBridge?) {
        val position = menuBridge?.position ?: -1
        if(position != -1 && adapter.itemCount > position){
            var item = adapter.getItemBeanByIndex(position)
            item.let {
                GymDbHelper.getInstance(context).deleteTrainRec(item!!.id)
            }
        }
    }

    //列表项点击
    override fun onItemClick(itemView: View?, position: Int) {
        if(adapter.itemCount > position){
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

    inner class MenuCreator: SwipeMenuCreator{
        override fun onCreateMenu(swipeLeftMenu: SwipeMenu?, swipeRightMenu: SwipeMenu?, viewType: Int) {
            val item = SwipeMenuItem(context)
            item.height = ViewGroup.LayoutParams.MATCH_PARENT
            item.width = context.resources.getDimensionPixelOffset(R.dimen.global_80dp)
            item.setBackgroundColor(context.resources.getColor(R.color.red))
            item.setImage(R.drawable.ic_delete_sweep_white_24dp)
            swipeRightMenu?.addMenuItem(item)
        }
    }

    inner class TrainRecAdapter:RecyclerView.Adapter<RecViewHolder>{
        private val dataList: MutableList<TrainRecBean> = mutableListOf()

        constructor(dataList: List<TrainRecBean>) : super(){
            this.dataList.addAll(dataList)
        }

        override fun getItemCount(): Int {
            return dataList.size
        }

        fun getItemBeanByIndex(index: Int):TrainRecBean?{
            if(dataList.size > index)
                return dataList[index]
            return null
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecViewHolder {
            val view = View.inflate(this@PageFirstPage.context,R.layout.view_first_page_list_item,null)
            return RecViewHolder(view)
        }
        override fun onBindViewHolder(holder: RecViewHolder?, position: Int) {
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
            holder?.trainRecTitle?.text = bean.title
            holder?.trainRecTime?.text = showTime
            holder?.trainMarking?.text = bean.marking.toString()
            if(bean.recIcon != null && bean.recIcon != -1)
                holder?.trainRecIcon?.setImageResource(bean.recIcon)
            bean.items.let {
                for (i in bean.items!!) {

                }
            }
        }

        fun updateDataList(dataList: List<TrainRecBean>){
            this.dataList.clear()
            this.dataList.addAll(dataList)
            notifyDataSetChanged()
        }
    }

    class RecViewHolder:RecyclerView.ViewHolder{
        var view: View? = null
        var trainRecIcon: CircleImageView? = null
        var trainRecTitle: TextView? = null
        var trainRecTime: TextView? = null
        var trainMarking: TextView? = null
        constructor(view: View):super(view){
            this.view = view
            trainRecIcon = view.trainRecItemIcon
            trainRecTitle = view.trainRecItemTitle
            trainRecTime = view.trainRecItemTime
            trainMarking = view.trainRecItemMarking
        }
    }
}