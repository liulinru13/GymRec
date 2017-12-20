package com.mmrx.gymrec

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.mmrx.gymrec.bean.model.MuscleBean
import com.mmrx.gymrec.bean.table.MuscleTable
import com.mmrx.gymrec.db.GymDbHelper
import com.mmrx.gymrec.ui.framework.EnumPageTitleType
import com.mmrx.gymrec.ui.framework.IPageManager
import com.mmrx.gymrec.ui.framework.PageQueue

import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select

class MainActivity : AppCompatActivity() ,PageQueue.IFloatingButtonListener{

    private var pageManager: IPageManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener({_ -> pageManager?.onTitleBarAction(EnumPageTitleType.LEFT_NULL)})
        val rootView: ViewGroup = findViewById(R.id.root_view)
        pageManager = PageQueue(this, rootView,this,toolbar)
        pageManager?.gotoPage(R.layout.page_first_page,null)

        val dbHelper = GymDbHelper.getInstance(this)
        fab.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                pageManager?.gotoPage(R.layout.page_train_subject,null)
//                dbHelper.use{
//                    val rowParser = classParser<MuscleBean>()
//                    val result = select(MuscleTable.NAME)
//                    val list = result.parseList(rowParser)
////                    val arr = emptyArray<Any?>()
////                    rowParser.parseRow(arr)
//                    Snackbar.make(p0!!, list.get(0).toString(), Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//                }
            }
        })
    }

    override fun setVisiable(visiable: Boolean) {
        fab.setVisibility(if(visiable) View.VISIBLE else View.INVISIBLE)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        //物理返回键
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(pageManager?.goBack()!!){
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var isInnerAction = false
        when (item.itemId) {
            R.id.action_settings -> {
                isInnerAction = pageManager?.onTitleBarAction(EnumPageTitleType.RIGHT_SETTING) ?: false
            }
            else -> null
        }
        return if(isInnerAction) true else super.onOptionsItemSelected(item)
    }
}
