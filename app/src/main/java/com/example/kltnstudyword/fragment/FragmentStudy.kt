package com.example.kltnstudyword.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kltnstudyword.model.TopicModel
import com.example.testkotlin.ListTopicAdapterRecycleView
import com.example.testkotlin.onItemClick
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_study.*
import android.content.Context
import android.content.Intent
import com.example.kltnstudyword.ActivityLearnWord
import com.example.kltnstudyword.R
import com.example.kltnstudyword.model.WordModel
import com.example.kltnstudyword.roomdatabase.AppDatabase
import kotlin.collections.ArrayList


private const val ARG_PARAM1 = "param1"

class FragmentStudy : Fragment(),onItemClick{
    var fragmentView : View? = null
    private var param1: String? = null
//    private var listTopic = ArrayList<TopicModel>
    private var listTopic = ArrayList<TopicModel>()
    private lateinit var topicAdapter : ListTopicAdapterRecycleView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var database: DatabaseReference

//    private val job = GlobalScope.launch {
//        rcvListTopic.apply {
//            layoutManager=LinearLayoutManager(requireActivity().applicationContext)
//            topicAdapter = ListTopicAdapterRecycleView(listTopic,this@FragmentStudy,requireActivity().applicationContext)
//            adapter=topicAdapter
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            listTopic = it.getSerializable(ARG_PARAM1) as ArrayList<TopicModel>
        }
    }
    companion object {
        @JvmStatic
        fun newInstance(list : ArrayList<TopicModel>) =
            FragmentStudy().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
                    putSerializable(ARG_PARAM1,list)
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("process","on attach")

    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Log.d("process","on activity created")


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView = inflater.inflate(R.layout.fragment_study, container, false)
        Log.d("process","on create view")
        if (savedInstanceState!=null){
            Log.d("process","value int saved: ${savedInstanceState.getInt("intSaved")}}")
        }else{
            Log.d("process","no value saved")
        }


//        if (savedInstanceState!=null){
//            listTopic= savedInstanceState.getSerializable("ArrayListTopic") as ArrayList<TopicModel>
//        }else{
//            database = FirebaseDatabase.getInstance().reference.child("topic")
//            database.addValueEventListener(object: ValueEventListener{
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    for (data in snapshot.children){
//                        //val
//                        listTopic.add(TopicModel(data.key.toString().toLong(),data.child("nameTopic").value.toString(),data.child("urlImg").value.toString()))
//                        Log.d("valueElement","${listTopic[listTopic.size-1]}");
//                    }
//                    rcvListTopic.apply {
//                        layoutManager=LinearLayoutManager(requireActivity().applicationContext)
//                        topicAdapter = ListTopicAdapterRecycleView(listTopic,this@FragmentStudy,requireActivity().applicationContext)
//                        adapter=topicAdapter
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//            })
//        }
//            rcvListTopic.apply {
//                layoutManager=LinearLayoutManager(requireActivity().applicationContext)
//                topicAdapter = ListTopicAdapterRecycleView(listTopic,this@FragmentStudy,requireActivity().applicationContext)
//                adapter=topicAdapter
//            }

        getContentFromRoom(){
                rcvListTopic.apply {
                layoutManager=LinearLayoutManager(requireActivity().applicationContext)
                topicAdapter = ListTopicAdapterRecycleView(it,this@FragmentStudy,requireActivity().applicationContext)
                adapter=topicAdapter
            }
        }
        return fragmentView
    }

    private fun getContentFromRoom(callBack: (List<TopicModel>)->Unit) {
        val mDb = AppDatabase.getInMemoryDatabase(requireActivity())
        listTopic = mDb?.roomDao()?.findAllTopicSync() as ArrayList<TopicModel>
        callBack(listTopic)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("process","on view created")
    }


    override fun onOneClickItem(position: Int) {
        Log.d("Study","one click $position")
        var listWord = ArrayList<WordModel>()
        database = FirebaseDatabase.getInstance().reference.child("topic/$position/listWord")
        database.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children){
                    val wordModel = data.getValue(WordModel::class.java)
                    wordModel?.let { listWord.add(it) }
                    Log.d("slick","${listWord.last()}")
                }
                val intent = Intent(requireActivity(),ActivityLearnWord::class.java)
                intent.putExtra("listStudy",listWord)
                startActivity(intent)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

//        Log.d("slick","${listWord.size}")

    }

    override fun onLongClickItem(position: Int) {
        Log.d("Study","long click $position")
    }

    override fun onClickItemWordModel(objects: WordModel, view : View) {
        TODO("Not yet implemented")
    }

    override fun onPause() {
        super.onPause()
        Log.d("process","on Pause")
        val outState = Bundle()
        outState.putSerializable("ArrayListTopic",listTopic)
        onSaveInstanceState(outState)
//        outState.putParcelableArrayList("ArrayListTopic",listTopic)
//        onSaveInstanceState(outState)
//        job.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("process","on destroy")
    }


}