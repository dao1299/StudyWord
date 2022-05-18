package com.example.kltnstudyword.fragment.quiz

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.kltnstudyword.R
import com.example.kltnstudyword.model.WordModel
import kotlinx.android.synthetic.main.fragment_question_kind_objective_fill_sentence.*
import java.util.stream.IntStream

private const val ARG_PARAM1 = "param1"


class FragmentQuestionKindObjectiveFillSentence : Fragment() {

    lateinit var viewClicked : View
    var listWordReceive = ArrayList<WordModel>()
    var count = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            listWordReceive = it.getSerializable(ARG_PARAM1) as ArrayList<WordModel>
        }
        return inflater.inflate(R.layout.fragment_question_kind_objective_fill_sentence, container, false)
    }

    companion object {
        fun newInstance(param1: ArrayList<WordModel>) =
            FragmentQuestionKindObjectiveFillSentence().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1,param1)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val animatedSlideIn: Animation = AnimationUtils.loadAnimation(
            requireContext().applicationContext,
            R.anim.abc_slide_in_bottom
        )
        super.onViewCreated(view, savedInstanceState)

        var array = intArrayOf(0,1,2,3)
        array.shuffle()
        val index = IntStream.of(*array).anyMatch { n -> n == 0 }

        txtQuestion.text=listWordReceive[0].exampleEN
        txtDefinitionQuestion.text=listWordReceive[0].exampleVN



        val wordKey = listWordReceive[0].word

        val sizeOfWordKey = wordKey.length
        val indexOfWordKey = listWordReceive[0].exampleEN.lowercase().indexOf(wordKey.lowercase())
        Log.d("testObjective","$wordKey    $indexOfWordKey")

        val indexEnd = listWordReceive[0].exampleEN.indexOf(" ",indexOfWordKey+sizeOfWordKey)




        val stringStart = listWordReceive[0].exampleEN.substring(0,indexOfWordKey)

        var stringEnd=""
        if (indexEnd>0) stringEnd = listWordReceive[0].exampleEN.substring(indexEnd)
        else stringEnd = listWordReceive[0].exampleEN.substring(indexOfWordKey+sizeOfWordKey)


        val stringQuestion ="<font color=#000000>${stringStart}</font><font color=blue size=1> _ _ _ _ _ _</font> <font color=#000000>${stringEnd}</font>";

        txtQuestion.text=Html.fromHtml(stringQuestion)

        btnAnswer1.text= listWordReceive[array[0]].word
        btnAnswer2.text= listWordReceive[array[1]].word
        btnAnswer3.text= listWordReceive[array[2]].word
        btnAnswer4.text= listWordReceive[array[3]].word

//        btnAnswer1.setOnTouchListener(View.OnTouchListener { v, event ->
//            when (event.action) {
//                MotionEvent.ACTION_DOWN -> v.setBackgroundResource(R.drawable.background_shadow_answer_fill_sentence_clicked)
//                MotionEvent.ACTION_HOVER_EXIT -> v.setBackgroundResource(R.drawable.background_shadow_answer_fill_sentence)
////                MotionEvent.ACTION_UP -> v.setBackgroundResource(R.drawable.background_shadow_answer_fill_sentence)
////                MotionEvent.ACTION_BUTTON_PRESS -> v.setBackgroundResource(R.drawable.background_shadow_answer_fill_sentence_clicked)
//            }
//            false
//        })
//        btnAnswer2.setOnTouchListener(View.OnTouchListener { v, event ->
//            when (event.action) {
//                MotionEvent.ACTION_DOWN -> v.setBackgroundResource(R.drawable.background_shadow_answer_fill_sentence_clicked)
//                MotionEvent.ACTION_UP -> v.setBackgroundResource(R.drawable.background_shadow_answer_fill_sentence)
//            }
//            false
//        })
//        btnAnswer3.setOnTouchListener(View.OnTouchListener { v, event ->
//            when (event.action) {
//                MotionEvent.ACTION_DOWN -> v.setBackgroundResource(R.drawable.background_shadow_answer_fill_sentence_clicked)
//                MotionEvent.ACTION_UP -> v.setBackgroundResource(R.drawable.background_shadow_answer_fill_sentence)
//            }
//            false
//        })
//        btnAnswer4.setOnTouchListener(View.OnTouchListener { v, event ->
//            when (event.action) {
//                MotionEvent.ACTION_DOWN -> v.setBackgroundResource(R.drawable.background_shadow_answer_fill_sentence_clicked)
//                MotionEvent.ACTION_UP -> v.setBackgroundResource(R.drawable.background_shadow_answer_fill_sentence)
//            }
//            false
//        })

        btnAnswer1.setOnClickListener {
            changeBackgroundClicked(it,0==array[0])
        }
        btnAnswer2.setOnClickListener {
            changeBackgroundClicked(it,0==array[1])
        }
        btnAnswer3.setOnClickListener {
            changeBackgroundClicked(it,0==array[2])
        }
        btnAnswer4.setOnClickListener {
            changeBackgroundClicked(it,0==array[3])
        }

    }
    private fun changeBackgroundClicked(view : View,state : Boolean){
        val button = requireActivity().findViewById<View>(R.id.btnNext)
        val txtResult = requireActivity().findViewById<View>(R.id.txtResult) as TextView
        when {
            count==0 -> {
                viewClicked=view
                viewClicked.setBackgroundResource(R.drawable.background_shadow_answer_fill_sentence_clicked)
                button.isEnabled=true
            }
            viewClicked==view -> {
                //count>0
                count=-1
                viewClicked.setBackgroundResource(R.drawable.background_shadow_answer_fill_sentence)
                button.isEnabled=false
            }
            else -> {
                viewClicked.setBackgroundResource(R.drawable.background_shadow_answer_fill_sentence)
                viewClicked=view
                viewClicked.setBackgroundResource(R.drawable.background_shadow_answer_fill_sentence_clicked)
                button.isEnabled=true
            }
        }
        if (!button.isEnabled) button.setBackgroundResource(R.drawable.background_shadow_button_disable)
        else{
            button.setBackgroundResource(R.drawable.background_shadow_button)
        }
        count++
        txtResult.text=state.toString()
        Log.d("resultObjective",txtResult.text.toString())
    }

}

