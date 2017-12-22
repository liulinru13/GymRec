package com.mmrx.gymrec.db

/**
 * 用于监听数据库变化
 * Created by liulinru on 2017/12/22.
 */
interface IDBActiveListener {
    fun dbUpdate(tableName: String)
}