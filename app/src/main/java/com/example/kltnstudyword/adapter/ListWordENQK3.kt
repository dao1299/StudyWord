package com.example.kltnstudyword.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kltnstudyword.R
import com.example.kltnstudyword.model.WordModel
import com.example.testkotlin.onItemClick
import kotlin.collections.ArrayList

class ListWordENQK3(private val listEN: List<WordModel>, private var onItemClick : onItemClick): RecyclerView.Adapter<ListWordENQK3.ViewHolder>(){
    var listofWordENQK3 = ArrayList<WordModel>(listEN)

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtWordQuestionKind3 : TextView = itemView.findViewById(R.id.txtWordQuestionKind3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_of_list_question_kind_3,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtWordQuestionKind3.text= listofWordENQK3[position].word
        holder.txtWordQuestionKind3.setOnClickListener {
            onItemClick.onClickItemWordModel(listofWordENQK3[position],it)
            it.setBackgroundResource(R.drawable.background_textview_question_kind_3_clicked)
        }
    }

    override fun getItemCount():Int{
        return listofWordENQK3.size
    }
}