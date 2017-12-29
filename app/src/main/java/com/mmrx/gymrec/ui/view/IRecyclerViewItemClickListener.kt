package com.mmrx.gymrec.ui.view

import android.view.View

/**
 * Created by mmrx on 17/12/29.
 */
interface IRecyclerViewItemClickListener {
    fun onItemClicked(view: View, position: Int)
    fun onItemLongClicked(view: View,position: Int)
}