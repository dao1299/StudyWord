package com.example.kltnstudyword.fragment.quiz

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kltnstudyword.R
import com.example.kltnstudyword.adapter.ListWordENQK3
import com.example.kltnstudyword.adapter.ListWordResultQK3
import com.example.kltnstudyword.adapter.ListWordVNQK3
import com.example.kltnstudyword.model.WordModel
import com.example.testkotlin.onItemClick
import kotlinx.android.synthetic.main.activity_study_word.*
import kotlinx.android.synthetic.main.fragment_question_kind_3.*

private const val ARG_PARAM1 = "param1"

class FragmentQuestionKind3 : Fragment(),onItemClick {


    var countClick = 0
    var countScore = 0
    lateinit var viewClicked1 : View
    lateinit var viewClicked2 : View
    lateinit var objectClicked1 : WordModel
    lateinit var objectClicked2 : WordModel
    var listEN= ArrayList<WordModel>()
    var listVN= ArrayList<WordModel>()
    var listResult = ArrayList<WordModel>()
    private lateinit var listWordENQK3: ListWordENQK3
    private lateinit var listWordVNQK3: ListWordVNQK3
    private lateinit var listWordResultQK3: ListWordResultQK3
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            listEN = it.getSerializable(ARG_PARAM1) as ArrayList<WordModel>
            listVN.addAll(listEN)
            listVN.shuffle()
        }
        return inflater.inflate(R.layout.fragment_question_kind_3, container, false)
    }

    companion object {
        fun newInstance(param1: ArrayList<WordModel>) =
            FragmentQuestionKind3().apply {
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
        initListView()
    }

    private fun initListView() {
//        listEN.add(WordModel("Hello","Xin chao"))
//        listVN.add(WordModel("Hello","Xin chao"))
//        listEN.add(WordModel("Morning","Buoi sang"))
//        listVN.add(WordModel("Morning","Buoi sang"))
//        listEN.add(WordModel("Night","Buoi toi"))
//        listVN.add(WordModel("Night","Buoi toi"))
//        listVN.shuffle()

        recyclerViewWordEN.apply{
            layoutManager= LinearLayoutManager(requireActivity())
            listWordENQK3 = ListWordENQK3(listEN,this@FragmentQuestionKind3)
            adapter=listWordENQK3
        }
        recyclerViewWordVN.apply{
            layoutManager= LinearLayoutManager(requireActivity())
            listWordVNQK3 = ListWordVNQK3(listVN,this@FragmentQuestionKind3)
            adapter=listWordVNQK3
        }
    }

    override fun onOneClickItem(position: Int) {
        println(listEN[position])
    }

    override fun onLongClickItem(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onClickItemWordModel(objects: WordModel, view: View) {
        println(objects)
//        view.visibility=View.INVISIBLE
        view.setBackgroundResource(R.drawable.background_textview_question_kind_3_clicked)

        when (countClick){
            0->{
                viewClicked1=view
                objectClicked1=objects
                countClick++
            }
            1->{
                viewClicked2=view
                objectClicked2=objects
                countClick++
            }
        }
        if (countClick==2){
            checkConnectWord()
        }
    }

    private fun checkConnectWord() {
        if (objectClicked1 == objectClicked2 && viewClicked1!=viewClicked2){
            viewClicked1.visibility=View.GONE
            viewClicked2.visibility=View.GONE
            listResult.add(objectClicked1)
            recyclerViewWordResult.apply{
                visibility=View.VISIBLE
                layoutManager= LinearLayoutManager(requireActivity())
                listWordResultQK3 = ListWordResultQK3(listResult)
                adapter=listWordResultQK3
            }
            if (listResult.size==listEN.size) {
                val button = requireActivity().findViewById<View>(R.id.btnNext)
                val txtResult = requireActivity().findViewById<TextView>(R.id.txtResult)
                button.isEnabled=true
                if (!button.isEnabled) button.setBackgroundResource(R.drawable.background_shadow_button_disable)
                else{
                    button.setBackgroundResource(R.drawable.background_shadow_button)
                }
                txtResult.text = "true"

            }
        }else{
            manageBlinkEffect(viewClicked1)
            manageBlinkEffect(viewClicked2)
        }
        countClick=0
    }
    private fun manageBlinkEffect(view:View) {
//        val anim = ObjectAnimator.ofInt(view, "backgroundColor", Color.RED)
        val argbEvaluator = ArgbEvaluator()
//        val anim = ObjectAnimator.ofObject(view,"backgroundColor",argbEvaluator,Color.WHITE,Color.RED)
        val anim = ObjectAnimator.ofFloat(view, "rotation", 0f, 10f, 0f, -10f, 0f)
        anim.repeatCount=1
        anim.duration = 300
//        anim.setEvaluator(ArgbEvaluator())
        anim.start()
        anim.addListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(p0: Animator?) {
                TODO("Not yet implemented")
            }

            override fun onAnimationEnd(p0: Animator?) {
                view.setBackgroundResource(R.drawable.background_textview_question_kind_3)
            }

            override fun onAnimationCancel(p0: Animator?) {
                TODO("Not yet implemented")
            }

            override fun onAnimationRepeat(p0: Animator?) {

            }

        })
    }

}