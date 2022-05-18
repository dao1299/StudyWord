package com.example.testkotlin

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kltnstudyword.model.TopicModel
import com.example.kltnstudyword.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ListTopicAdapterRecycleView(private val listTopic: List<TopicModel>, private var onItemClick : onItemClick, private var context: Context) : RecyclerView.Adapter<ListTopicAdapterRecycleView.ViewHolder>(){
    var listofTopic = ArrayList<TopicModel>(listTopic)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgTopic : ImageView = itemView.findViewById(R.id.imgTopic)
        val txtNameTopic : TextView = itemView.findViewById(R.id.txtNameTopic)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.item_of_list_post,parent,false);
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val storageFirebase = Firebase.storage

//        storageFirebase.getReference("gs://kltn-8636d.appspot.com/image" + listofTopic.get(position).imgTopic)

//        storageFirebase.getReferenceFromUrl("gs://kltn-8636d.appspot.com/image" + listofTopic.get(position).imgTopic).downloadUrl
//            .addOnSuccessListener { urlImage ->
//                run { Glide.with(context).load(urlImage).into(holder.imgTopic) }
//            }.addOnFailureListener {
//                Log.d("loadImg","${it.printStackTrace()}")
//            }

        holder.txtNameTopic.text=listofTopic.get(position).nameTopic
//        Glide
//            .with(context)
//            .load("gs://kltn-8636d.appspot.com/image"+listofTopic.get(position).imgTopic+"")
//            .centerCrop()
//            .into(holder.imgTopic)


      val storageRef = Firebase.storage.reference
        val url = storageRef.child(listTopic[position].imgTopic)
        url.downloadUrl.addOnSuccessListener {
            val imgUrl = it.toString()
            Glide.with(context).load(imgUrl).into(holder.imgTopic)
        }



//        Glide.with(context).load(listTopic[position].imgTopic).into(holder.imgTopic)


//        Log.d("url","$url")


        holder.itemView.setOnTouchListener(View.OnTouchListener { v, event ->
            when (event.action) {
                ACTION_DOWN -> {
//                    v.performClick()
                    v.setBackgroundResource(R.drawable.background_shadow_button_touched)
                }
                ACTION_UP -> {
                    v.performClick()
                    v.setBackgroundResource(R.drawable.background_shadow_button)
                }
            }
            false
        })

        holder.itemView.setOnClickListener {
            onItemClick.onOneClickItem(position)
        }


    }


    override fun getItemCount(): Int {
        return listTopic?.size
    }





}