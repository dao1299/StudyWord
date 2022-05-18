package com.example.kltnstudyword.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.kltnstudyword.ActivityPractice
import com.example.kltnstudyword.R
import com.example.kltnstudyword.model.WordModel
import com.example.kltnstudyword.roomdatabase.AppDatabase
import com.example.testkotlin.onItemClick
import kotlinx.android.synthetic.main.fragment_practice.*
import java.util.*

private const val ARG_PARAM1 = "param1"
class FragmentPractice : Fragment(), onItemClick {
    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_practice, container, false)
        return view
    }

    companion object {
        fun newInstance(param1: String) =
            FragmentPractice().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayData()
        addEventButton()



    }

    private fun displayData() {
        val calendar = Calendar.getInstance()
        val mDb = AppDatabase.getInMemoryDatabase(requireActivity().applicationContext)
        var numTopicLearned = mDb?.roomDao()?.countTopicLearned()
        numTopicLearned?.let {
            txtNumTopicLearned.text = "$it"
        }
        mDb?.roomDao()?.countNumLevel(1).let {
            txtWordLevel1.text="$it"
        }
        mDb?.roomDao()?.countNumLevel(2).let {
            txtWordLevel2.text="$it"
        }
        mDb?.roomDao()?.countNumLevel(3).let {
            txtWordLevel3.text="$it"
        }
        mDb?.roomDao()?.countNumLevel(4).let {
            txtWordLevel4.text="$it"
        }
        mDb?.roomDao()?.countWordLearned().let {
            txtNumWordLearned.text="$it"
        }
        mDb?.roomDao()?.countWordPractice(calendar.time).let {
            txtCountWordPractice.text="$it"
            if (it!!>0){
                btnStartPractice.isEnabled = true
                btnStartPractice.setBackgroundResource(R.drawable.background_shadow_button)
            }
        }
    }

    private fun addEventButton() {
        btnStartPractice.setOnTouchListener(View.OnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> v.setBackgroundResource(R.drawable.background_shadow_button_touched)
                MotionEvent.ACTION_UP -> v.setBackgroundResource(R.drawable.background_shadow_button)
            }
            false
        })
        btnStartPractice.setOnClickListener {

            val sizeList =  txtCountWordPractice.text.toString().toInt()
            if (sizeList==0){
                Toast.makeText(requireActivity(),"Not this time!",Toast.LENGTH_SHORT).show()
            }else{
                val calendar = Calendar.getInstance()
                val mDb = AppDatabase.getInMemoryDatabase(requireActivity().applicationContext)
                val intent = Intent(requireActivity(),ActivityPractice::class.java)
                var listNeedPractice = ArrayList<WordModel>()

                listNeedPractice = mDb?.roomDao()?.findListPractice(calendar.time) as ArrayList<WordModel>
                (listNeedPractice.size==sizeList).let {
                    intent.putExtra("listPractice",listNeedPractice)
                    startActivity(intent)
                }
            }



        }
    }


    override fun onOneClickItem(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onLongClickItem(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onClickItemWordModel(objects: WordModel, view: View) {
        TODO("Not yet implemented")
    }
}