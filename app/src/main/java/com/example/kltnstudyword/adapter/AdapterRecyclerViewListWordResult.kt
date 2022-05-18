package com.example.kltnstudyword.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kltnstudyword.R
import com.example.kltnstudyword.model.WordResultModel
import com.example.testkotlin.onItemClick

class AdapterRecyclerViewListWordResult(
    private val listWordResult: List<WordResultModel>,
    private var onItemClick: onItemClick
) : RecyclerView.Adapter<AdapterRecyclerViewListWordResult.ViewHolder>() {
    var arrayListWordResult = ArrayList<WordResultModel>(listWordResult)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgResult: ImageView = itemView.findViewById(R.id.imgResult)
        val txtWordEnInResult: TextView = itemView.findViewById(R.id.txtWordEnInResult)
        val txtWordVnInResult: TextView = itemView.findViewById(R.id.txtWordVnInResult)
        val imgAgain: ImageView = itemView.findViewById(R.id.imgAgain)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_word_ressult, parent, false);
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (arrayListWordResult[position].result) {
            holder.imgResult.setImageResource(R.drawable.correct)
        } else {
            holder.imgResult.setImageResource(R.drawable.incorrect)
        }
        holder.txtWordEnInResult.text = arrayListWordResult[position].wordModel.word
        holder.txtWordVnInResult.text = arrayListWordResult[position].wordModel.partOfSpeech
        holder.imgAgain.setOnClickListener {
//            onItemClick.onClickItemWordModel(arrayListWordResult[position].wordModel, it)
            onItemClick.onOneClickItem(position)
        }
        holder.itemView.setOnLongClickListener {
            onItemClick.onLongClickItem(position)
            true
        }
    }

    override fun getItemCount() = arrayListWordResult.size
}