package com.example.kltnstudyword.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kltnstudyword.R
import com.example.kltnstudyword.model.WordModel
import com.example.testkotlin.onItemClick

class ListWordVNQK3(private val listVN: List<WordModel>, private var onItemClick: onItemClick) : RecyclerView.Adapter<ListWordVNQK3.ViewHolder>() {
    var listofWordVNQK3 = ArrayList<WordModel>(listVN)
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtWordQuestionKind3: TextView = itemView.findViewById(R.id.txtWordQuestionKind3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_of_list_question_kind_3, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtWordQuestionKind3.text = listofWordVNQK3[position].partOfSpeech
        holder.txtWordQuestionKind3.setOnClickListener {
            it.setBackgroundResource(R.drawable.background_textview_question_kind_3_clicked)
            onItemClick.onClickItemWordModel(listofWordVNQK3[position],it)
//            it.setBackgroundResource(R.drawable.background_textview_question_kind_3_clicked)
        }
    }

    override fun getItemCount() = listofWordVNQK3.size

}