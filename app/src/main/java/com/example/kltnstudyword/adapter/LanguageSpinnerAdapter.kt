package com.example.kltnstudyword.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.kltnstudyword.R
import com.example.kltnstudyword.inventory.LanguageModel


class LanguageSpinnerAdapter constructor(
    var context: Context,
    var LanguageModelList: List<LanguageModel>
): BaseAdapter() {
//    private var context: Context? = null
//    private var LanguageModelList: List<LanguageModel>? = null
//
//    fun LanguageSpinnerAdapter(context: Context?, LanguageModelList: List<LanguageModel>?) {
//        this.context = context
//        this.LanguageModelList = LanguageModelList
//    }
//
    override fun getCount(): Int {
        return LanguageModelList!!.size
    }

    override fun getItem(i: Int): Any? {
        return i
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup?): View? {
        val rootView: View = LayoutInflater.from(context)
            .inflate(R.layout.item_language_model, viewGroup, false)
        val txtName = rootView.findViewById<TextView>(R.id.name)
        val image: ImageView = rootView.findViewById(R.id.image)
        txtName.text = LanguageModelList[i].language
        image.setImageResource(LanguageModelList[i].image)
        return rootView
    }
}