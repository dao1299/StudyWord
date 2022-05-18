package com.example.kltnstudyword.adapter

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kltnstudyword.R
import com.example.kltnstudyword.model.TopicModel
import com.example.testkotlin.onItemClick
import kotlinx.android.synthetic.main.item_of_list_post.view.*

class RecyclerViewTopicAdapter(private var onItemClick : onItemClick, private var context: Context): RecyclerView.Adapter<RecyclerViewTopicAdapter.MyViewHolder>() {

    var items  = ArrayList<TopicModel>()

    fun setListData(data: ArrayList<TopicModel>) {
        this.items = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewTopicAdapter.MyViewHolder {
       val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_of_list_post, parent, false)
        return MyViewHolder(inflater,context)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerViewTopicAdapter.MyViewHolder, position: Int){
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            onItemClick.onOneClickItem(position)
        }
    }

    class MyViewHolder(view: View,context: Context): RecyclerView.ViewHolder(view) {

        val imgTopic : ImageView = itemView.findViewById(R.id.imgTopic)
        val txtNameTopic : TextView = itemView.findViewById(R.id.txtNameTopic)


        fun bind(data: TopicModel) {
            txtNameTopic.setTextColor(Color.parseColor("#ffffff"))
            if (data.isDownloaded==1){
                txtNameTopic.setTextColor(Color.parseColor("#59d18c"))
            }
            txtNameTopic.text = data.nameTopic
            imgTopic.setImageURI((Uri.parse(data.imgTopic)))

//            tvName.text = data.name
//
//            tvEmail.text = data.email
//
//
//            tvPhone.text = data.phone
//
//            deleteUserID.setOnClickListener {
//                listener.onDeleteUserClickListener(data)
//            }
        }
    }

//    interface RowClickListener{
//        fun onDeleteUserClickListener(user: UserEntity)
//        fun onItemClickListener(user: UserEntity)
//    }
}