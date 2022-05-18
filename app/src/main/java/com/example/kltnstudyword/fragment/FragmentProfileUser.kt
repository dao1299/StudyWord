package com.example.kltnstudyword.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kltnstudyword.R
import kotlinx.android.synthetic.main.fragment_change_profile_user.*

private const val ARG_PARAM1 = "param1"
class FragmentProfileUser : Fragment(){
    var stateImageExpand = 0;
    companion object {
        fun newInstance(param1: String) =
            FragmentProfileUser().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_change_profile_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addEvent()
    }

    private fun addEvent() {
                btnShowMore.setOnClickListener {
            if (stateImageExpand == 0) {
                btnShowMore.setImageResource(R.drawable.expand_less)
                stateImageExpand = 1
                container_change_password.apply {
                    visibility = View.VISIBLE
                }
            } else {
                btnShowMore.setImageResource(R.drawable.expand_more)
                stateImageExpand = 0
                container_change_password.visibility = View.INVISIBLE
            }
        }
    }
}