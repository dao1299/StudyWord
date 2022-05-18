package com.example.kltnstudyword.fragment.quiz

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.kltnstudyword.R
import com.example.kltnstudyword.model.WordModel
import kotlinx.android.synthetic.main.fragment_question_choose_picture.*
import java.util.stream.IntStream

private const val ARG_PARAM1 = "param1"

class FragmentQuestionChoosePicture : Fragment() {
    lateinit var viewClicked: View
    var listWordReceive = ArrayList<WordModel>()
    var count = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            listWordReceive = it.getSerializable(ARG_PARAM1) as ArrayList<WordModel>
        }
        return inflater.inflate(R.layout.fragment_question_choose_picture, container, false)
    }

    companion object {
        fun newInstance(param1: ArrayList<WordModel>) =
            FragmentQuestionChoosePicture().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val animatedSlideIn: Animation = AnimationUtils.loadAnimation(
            requireContext().applicationContext,
            R.anim.abc_slide_in_bottom
        )
        super.onViewCreated(view, savedInstanceState)

        var array = intArrayOf(0, 1, 2, 3)
        array.shuffle()
        val index = IntStream.of(*array).anyMatch { n -> n == 0 }

        txtKeyWordChoosePicture.text = "''${listWordReceive[0].partOfSpeech}''"

        Glide.with(requireActivity()).load(listWordReceive[array[0]].srcImg).into(imgAnswer1)
        Glide.with(requireActivity()).load(listWordReceive[array[1]].srcImg).into(imgAnswer2)
        Glide.with(requireActivity()).load(listWordReceive[array[2]].srcImg).into(imgAnswer3)
        Glide.with(requireActivity()).load(listWordReceive[array[3]].srcImg).into(imgAnswer4)



        imgAnswer1.setOnClickListener {
            changeBackgroundClicked(it, 0 == array[0])
        }
        imgAnswer2.setOnClickListener {
            changeBackgroundClicked(it, 0 == array[1])
        }
        imgAnswer3.setOnClickListener {
            changeBackgroundClicked(it, 0 == array[2])
        }
        imgAnswer4.setOnClickListener {
            changeBackgroundClicked(it, 0 == array[3])
        }



    }


    private fun changeBackgroundClicked(view: View, state: Boolean) {
        val button = requireActivity().findViewById<View>(R.id.btnNext)
        val txtResult = requireActivity().findViewById<View>(R.id.txtResult) as TextView
        when {
            count == 0 -> {
                viewClicked = view
//                viewClicked.setBackgroundResource(R.drawable.background_image_choose_picture_clicked)
//                viewClicked.parent
                button.isEnabled = true
            }
            viewClicked == view -> {
                //count>0
                count = -1
//                viewClicked.setBackgroundResource(R.drawable.background_image_choose_picture)
                button.isEnabled = false
            }
            else -> {
//                viewClicked.setBackgroundResource(R.drawable.background_image_choose_picture)
                viewClicked = view
//                viewClicked.setBackgroundResource(R.drawable.background_image_choose_picture_clicked)
                button.isEnabled = true
            }
        }
        if (!button.isEnabled) button.setBackgroundResource(R.drawable.background_shadow_button_disable)
        else{
            button.setBackgroundResource(R.drawable.background_shadow_button)
        }
        count++
        txtResult.text = state.toString()
        Log.d("resultObjective", txtResult.text.toString())
    }

}