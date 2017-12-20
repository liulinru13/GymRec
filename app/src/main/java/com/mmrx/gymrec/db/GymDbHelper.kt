package com.mmrx.gymrec.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.mmrx.gymrec.R
import com.mmrx.gymrec.bean.model.TrainBean
import com.mmrx.gymrec.bean.model.TrainRecBean
import com.mmrx.gymrec.bean.table.MuscleTable
import com.mmrx.gymrec.bean.table.RecordTable
import com.mmrx.gymrec.bean.table.TrainTable
import com.mmrx.gymrec.ui.view.PageFirstPage
import org.jetbrains.anko.db.*

/**
 * Created by liulinru on 2017/11/22.
 */
class GymDbHelper (context : Context) : ManagedSQLiteOpenHelper(context,"gymRecDb",null,1){

    companion object {
        private var instance : GymDbHelper? = null

        @Synchronized
        fun getInstance(context : Context) : GymDbHelper{
            if(instance == null){
                instance = GymDbHelper(context.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        //muscle
        db.createTable(MuscleTable.NAME,true,
                MuscleTable.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                MuscleTable.MUSCLE_GROUP to TEXT,
                MuscleTable.MUSCLE_ICON to INTEGER)
        //train
        db.createTable(TrainTable.NAME,true,
                TrainTable.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                TrainTable.TRAIN_DATE to TEXT + NOT_NULL,
                TrainTable.TRAIN_SUBJECT to TEXT + NOT_NULL,
                TrainTable.ADVICE to TEXT,
                TrainTable.TRAIN_MARKING to INTEGER)
        //record
        db.createTable(RecordTable.NAME,true,
                RecordTable.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                RecordTable.ITEM to TEXT + NOT_NULL,
                RecordTable.WEIGHT to INTEGER,
                RecordTable.WEIGHT_UNIT to TEXT + DEFAULT("kg"),
                RecordTable.REPEAT_TIMES to INTEGER,
                RecordTable.REPEAT_ARRAYS to INTEGER,
                RecordTable.BREAK_TIME to INTEGER,
                RecordTable.TRAIN_ID to INTEGER,
                RecordTable.MUSCLE_GROUP_ID to INTEGER,
                FOREIGN_KEY(RecordTable.TRAIN_ID, TrainTable.NAME, TrainTable.ID),
                FOREIGN_KEY(RecordTable.MUSCLE_GROUP_ID, MuscleTable.NAME, MuscleTable.ID))

        val muscleArr = listOf("腿","胸","肩膀","肱二头","肱三头","手臂","背")
        for(item in muscleArr){
            val row = ContentValues()
            row.put(MuscleTable.MUSCLE_GROUP,item)
            db.insert(MuscleTable.NAME,null,row)
        }

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(RecordTable.NAME,true)
        db.dropTable(TrainTable.NAME,true)
        db.dropTable(MuscleTable.NAME,true)
        onCreate(db)
    }

    fun getTrainRecData():List<TrainRecBean>{
        val list = mutableListOf<TrainRecBean>()
        instance?.use{
            //表 train
            val trainRowParser = classParser<TrainBean>()
            val trainTableResult = select(TrainTable.NAME)
            val trainList = trainTableResult.parseList(trainRowParser)
            for(temp in trainList){
                var marking = temp.train_marking
                if(marking <60)
                    marking = 60

                var itemsList = createRankIconResIdList((marking-60)%10)
                list.add(TrainRecBean(R.drawable.default_image,temp.train_subject,temp.train_date,temp.train_marking,itemsList))
            }
        }
        return list
    }
    private fun createRankIconResIdList(num: Int):List<Int>{
        val list = mutableListOf<Int>()
        for(i in 0..num){
            list.add(R.drawable.abc_ic_arrow_drop_right_black_24dp)
        }
        return list
    }
}