package com.mmrx.gymrec.ui.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.mmrx.gymrec.R
import com.mmrx.gymrec.bean.model.TrainRecBean
import com.mmrx.gymrec.db.GymDbHelper
import com.mmrx.gymrec.ui.framework.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.page_first_page.view.*

/**
 * page_first_page.xml
 * Created by mmrx on 17/12/16.
 */
class PageFirstPage :LinearLayout , IPageContent{

    val adapter: TrainRecAdapter by lazy { TrainRecAdapter(mutableListOf<TrainRecBean>()) }
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        listView.layoutManager = LinearLayoutManager(context)
        listView.adapter = adapter
    }

    override fun onForground() {
//        var tempList = mutableListOf<TrainRecBean>()
//        for(i in 1 .. 10){
//            tempList.add(TrainRecBean(1,"测试文本测试文本","2017年12月20日19:36:14",88+i, listOf()))
//        }
        val tempList = GymDbHelper.getInstance(context).getTrainRecData()
        adapter.updateDataList(tempList)
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

    override fun buildTitleBar(): PageTitleStruct? {
        return PageTitleStruct(EnumPageTitleType.LEFT_PERSON_CENTER
                ,resources.getString(R.string.app_title)
                ,null)
    }

    override fun onTitleBarAction(action: EnumPageTitleType?): EnumPageTitleAction? {
        return EnumPageTitleAction.PAGE_TITLE_ACTION_PERSON_CENTER
    }

    inner class TrainRecAdapter:RecyclerView.Adapter<RecViewHolder>{
        private val dataList: MutableList<TrainRecBean> = mutableListOf()

        constructor(dataList: List<TrainRecBean>) : super(){
            this.dataList.addAll(dataList)
        }

        override fun getItemCount(): Int {
            return dataList.size
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecViewHolder {
            val view = View.inflate(this@PageFirstPage.context,R.layout.view_first_page_list_item,null);
            return RecViewHolder(view)
        }
        override fun onBindViewHolder(holder: RecViewHolder?, position: Int) {
            if(dataList.size > position)
                return
            val bean = dataList[position]
            holder?.trainRecTitle?.text = bean.title
            holder?.trainRecTime?.text = bean.dataTime
            holder?.trainMarking?.text = bean.marking.toString()
            for(i in bean.items){

            }
        }

        fun updateDataList(dataList: List<TrainRecBean>){
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
        var trainItemsLayout: LinearLayout? = null
        constructor(view: View):super(view){
            this.view = view
            trainRecIcon = view.findViewById(R.id.trainRecItemIcon)
            trainRecTitle = view.findViewById(R.id.trainRecItemTitle)
            trainRecTime = view.findViewById(R.id.trainRecItemTime)
            trainMarking = view.findViewById(R.id.trainRecItemMarking)
            trainItemsLayout = view.findViewById(R.id.trainItemsLayout)
        }
    }
}