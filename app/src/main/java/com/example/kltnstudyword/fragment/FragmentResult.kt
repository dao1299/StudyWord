package com.example.kltnstudyword.fragment

import android.os.Bundle
import android.text.TextUtils
import android.text.TextUtils.replace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.kltnstudyword.R
import com.example.kltnstudyword.fragment.learn.FragmentLearnByCard
import kotlinx.android.synthetic.main.fragment_result_answer.*

private const val ARG_PARAM1 = "param1"
class FragmentResult:Fragment() {
    companion object {
        fun newInstance(param1: String) =
            FragmentResult().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_result_answer, container, false)
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        btnNextInResult.setOnClickListener {
//            setFragment(FragmentLearnByCard.newInstance("hello"),"learn_by_card")
//            finishFragment(this)
//        }
    }
    private fun setFragment( fragment: Fragment, tag: String){
        val fragmentManager = (activity as FragmentActivity).supportFragmentManager
        fragmentManager.beginTransaction().apply {
            setCustomAnimations(R.anim.fab_slide_in_from_right, R.anim.fab_slide_out_to_left)
            replace(R.id.containerStudy, fragment, tag)
            commit()
        }
    }
    private fun finishFragment( fragment: Fragment){
        val fragmentManager = (activity as FragmentActivity).supportFragmentManager
        fragmentManager.beginTransaction().apply {
            remove(fragment)
            commit()
        }
    }

}