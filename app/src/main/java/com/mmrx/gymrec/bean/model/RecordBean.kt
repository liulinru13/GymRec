package com.mmrx.gymrec.bean.model

/**
 * Created by mmrx on 17/12/8.
 */
 data class RecordBean (val _id: Int,var item: String,var weight: Int,
                        var weight_unit: Int,var repeat_times: Int,
                        var repeat_arrays: Int,var break_time: Int,
                        var train_id: Int,var muscle_group_id: Int)