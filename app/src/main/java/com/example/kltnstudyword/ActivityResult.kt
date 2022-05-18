package com.example.kltnstudyword

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kltnstudyword.adapter.AdapterRecyclerViewListWordResult
import com.example.kltnstudyword.fragment.learn.FragmentLearnByCard
import com.example.kltnstudyword.model.WordModel
import com.example.kltnstudyword.model.WordResultModel
import com.example.testkotlin.onItemClick
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlinx.android.synthetic.main.activity_result.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

private const val KEY_VERSION_DATE = "date"
class ActivityResult : AppCompatActivity(),onItemClick {
    var listWordResult = ArrayList<WordResultModel>()
    private lateinit var listWordResultAdapter : AdapterRecyclerViewListWordResult
    var percent = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        receiveData()
        addEventButton()

    }

    private fun addEventButton() {
        btnResult2Main.setOnTouchListener(View.OnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> v.setBackgroundResource(R.drawable.background_shadow_button_touched)
                MotionEvent.ACTION_UP -> v.setBackgroundResource(R.drawable.background_shadow_button)
            }
            false
        })
        btnResult2Main.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun displayRecyclerView() {
        recyclerViewListWordInResult.apply {
            layoutManager= LinearLayoutManager(this@ActivityResult)
            listWordResultAdapter = AdapterRecyclerViewListWordResult(listWordResult,this@ActivityResult)
            adapter=listWordResultAdapter
        }
    }

    private fun receiveData() {
        val intent = intent
        percent = intent.getFloatExtra("score", 0f)
        if (intent.hasExtra("listResult")){
            listWordResult = intent?.getSerializableExtra("listResult") as ArrayList<WordResultModel>
        }else{
//            listWordResult.add(WordResultModel(WordModel("Hello", "Xin chao"), true))
//            listWordResult.add(WordResultModel(WordModel("Bye", "Tam biet"), false))
//            listWordResult.add(WordResultModel(WordModel("Window", "Cua so"), true))
//            listWordResult.add(WordResultModel(WordModel("Empty", "Trong rong"), false))
//            listWordResult.add(WordResultModel(WordModel("Computer", "May tinh"), false))
//            listWordResult.add(WordResultModel(WordModel("Button", "Nut"), true))
        }

        displayRecyclerView()
        if (listWordResult.size>0){
            val sharedPreferences = getSharedPreferences("PREF", Context.MODE_PRIVATE)
            val date = sharedPreferences.getLong("date",0L)
            val calendar = Calendar.getInstance()
            val today = calendar.time
            var countDate = sharedPreferences.getInt("count_date",0)
            if (date == 0L || (date<today.time && Date(date).day!=today.day)){
                sharedPreferences.edit().putLong("date",today.time).apply()
                sharedPreferences.edit().putInt("count_date",countDate+1).apply()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        circularProgressBar.apply {
            setProgressWithAnimation(percent, 3000L)
            progressMax = 100f
            backgroundProgressBarColor = Color.GRAY
            progressBarWidth = 40f
            backgroundProgressBarWidth = 30f
            roundBorder = true
            startAngle = 0f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }
        count_animation_textView
            .setAnimationDuration(3000L)
            .countAnimation(0, percent.roundToInt())
    }

    override fun onOneClickItem(position: Int) {

        val intent = Intent(this,ActivityLearnWord::class.java)
        intent.putExtra("listWordAgain",listWordResult)
        intent.putExtra("positionAgain",position)
        startActivity(intent)

    }

    override fun onLongClickItem(position: Int) {
        imgCloseDetailWord.setOnClickListener {
            container_detail_word.visibility=View.GONE
        }
        container_detail_word.visibility=View.VISIBLE
        setFragment(FragmentLearnByCard.newInstance(listWordResult[position].wordModel),"")
    }
    private fun setFragment(fragment: Fragment, tag: String) =
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(R.anim.fab_slide_in_from_right, R.anim.fab_slide_out_to_left)
            replace(R.id.infoWordInResult, fragment, tag)
            commit()
        }

    override fun onClickItemWordModel(objects: WordModel, view: View) {
//        val intent = Intent(this,ActivityLearnWord::class.java)
//        intent.putExtra("wordAgain",objects)
//        intent.putExtra("listWordAgain",listWordResult)
//        for (x in listWordResult){
//            if (x.wordModel==objects){
//
//            }
//        }
//        startActivity(intent)
//        Log.d("Again",objects.toString())
    }





}