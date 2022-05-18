package com.example.kltnstudyword.fragment.login

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.kltnstudyword.ActivityLogin
import com.example.kltnstudyword.MainActivity
import com.example.kltnstudyword.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_signin.*
import kotlinx.android.synthetic.main.fragment_signup.*


private const val ARG_PARAM1 = "param1"
class FragmentSignIn: Fragment(){
    private lateinit var auth: FirebaseAuth
    companion object {
        fun newInstance(param1: String) =
            FragmentSignIn().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        return inflater.inflate(R.layout.fragment_signin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload()
        }
        addEventTouch()
    }

    private fun reload() {

    }

    private fun signIn(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    Toast.makeText(requireActivity(), "Login success!",
                        Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(requireActivity(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?): Unit {
        val intent = Intent(activity,MainActivity::class.java)
        startActivity(intent)
    }

    private fun addEventTouch() {
        btnBackSignIn.setOnClickListener {
            val activity = (activity as ActivityLogin)
            activity.onBackPressed()
        }
        btnSignin.setOnClickListener {
            signIn(edtEmailSignin.text.toString(),edtPWSignin.text.toString())
        }
    }

    private fun validateForm(email: String, pw: String): Boolean {
        var valid = true
        if (email.isEmpty()) valid = false
        if (pw.isEmpty() || pw.length < 6) valid = false
        return valid
    }



}