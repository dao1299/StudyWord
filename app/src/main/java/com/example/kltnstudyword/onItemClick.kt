package com.example.testkotlin

import com.example.kltnstudyword.model.WordModel
import android.view.View

interface onItemClick {
    fun onOneClickItem(position: Int)
    fun onLongClickItem(position: Int)
    fun onClickItemWordModel(objects: WordModel, view: View)

}