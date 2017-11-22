package com.mmrx.gymrec.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper

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

    override fun onCreate(db: SQLiteDatabase?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}