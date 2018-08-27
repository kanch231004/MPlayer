package com.kanch786.musicapp.base

import android.support.v7.widget.helper.ItemTouchHelper
import android.support.v7.widget.RecyclerView



class RecyclerItemTouchHelper(private val intDragDir : Int, swipeDir: Int, private var listener: RecyclerItemTouchHelperListener) : ItemTouchHelper.SimpleCallback(intDragDir,swipeDir) {


    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
        listener.onSwiped(viewHolder!!, direction, viewHolder.adapterPosition)
    }




    interface RecyclerItemTouchHelperListener {

        fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int)
    }
}