package com.example.kltnstudyword.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kltnstudyword.ActivityLogin
import com.example.kltnstudyword.MainActivity
import com.example.kltnstudyword.R
import com.example.kltnstudyword.fragment.login.FragmentSignIn
import com.example.kltnstudyword.fragment.login.FragmentSignUp
import kotlinx.android.synthetic.main.fragment_login.*

private const val ARG_PARAM1 = "param1"
class FragmentLogin: Fragment() {
    companion object {
        fun newInstance(param1: String) =
            FragmentLogin().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addEventTouch()
    }

    private fun addEventTouch() {
        btnSignIn.setOnClickListener {
            val activity = (activity as ActivityLogin)
            activity.setFragment(FragmentSignIn.newInstance("s"), "s")
        }
        btnSignUp.setOnClickListener {
            val activity = (activity as ActivityLogin)
            activity.setFragment(FragmentSignUp.newInstance("s"), "s")
        }
    }
}