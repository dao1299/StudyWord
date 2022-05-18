package com.example.kltnstudyword.adapter

import android.graphics.Typeface
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kltnstudyword.R
import com.example.kltnstudyword.model.WordModel

class ListWordResultQK3(private val listResult: List<WordModel>) : RecyclerView.Adapter<ListWordResultQK3.ViewHolder>() {
    var listofWordResultQK3 = ArrayList<WordModel>(listResult)
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtWordQuestionKind3: TextView = itemView.findViewById(R.id.txtWordQuestionKind3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_of_list_question_kind_3, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtWordQuestionKind3.apply {
            text = listofWordResultQK3[position].word
            setTextSize(TypedValue.COMPLEX_UNIT_SP,22f)
            typeface = Typeface.DEFAULT_BOLD
        }
    }

    override fun getItemCount() = listofWordResultQK3.size

}