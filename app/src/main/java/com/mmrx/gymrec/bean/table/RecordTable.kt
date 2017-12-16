package com.mmrx.gymrec.bean.table

/**
 * Created by mmrx on 17/12/8.
 */
object RecordTable {
    val NAME = "record"
    val ID = "_id"
    val ITEM = "item"
    val WEIGHT = "weight"//重量
    val WEIGHT_UNIT = "weight_unit"//重量单位 kg lbs
    val REPEAT_TIMES = "repeat_times"//重复次数
    val REPEAT_ARRAYS = "repeat_arrays"//组数
    val BREAK_TIME = "break_time"//组间间隔
    val TRAIN_ID = "train_id"//所属锻炼记录,外键，连接表 train _id
    val MUSCLE_GROUP_ID = "muscle_group_id"//所属锻炼肌肉群，外键，连接表 muscle _id
}