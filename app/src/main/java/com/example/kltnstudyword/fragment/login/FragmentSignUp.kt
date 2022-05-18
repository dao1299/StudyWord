package com.example.kltnstudyword.fragment.login

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kltnstudyword.ActivityLogin
import com.example.kltnstudyword.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_signup.*


private const val ARG_PARAM1 = "param1"
class FragmentSignUp: Fragment(){
    lateinit var progressDialog: ProgressDialog
    private lateinit var auth: FirebaseAuth
    companion object {
        fun newInstance(param1: String) =
            FragmentSignUp().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        val currentUser = auth.currentUser
        auth.signOut()
        if(currentUser != null){
            reload()
        }
        addEventTouch()
    }

    private fun reload() {

    }

    private fun addEventTouch() {
        btnBackSignUp.setOnClickListener {
            val activity = (activity as ActivityLogin)
            activity.onBackPressed()
        }
        btnSignUp.setOnClickListener {
            if (edtCPWSignUp.text.toString().trim() == edtPWSignUp.text.toString().trim())
            {
//                createAccount(edtEmailSignUp.text.toString().trim(),edtPWSignUp.text.toString().trim())
                progressDialog = ProgressDialog.show(activity, "Đăng ký","Đợi tí tẹo thôi mờ (>\"<)", true);
                val name = edtNameUserSignUp.text.toString()
                addEventSignup(name)
                val activity = (activity as ActivityLogin)
                activity.setFragment(FragmentSignIn.newInstance("1"),"")
            }
//            Toast.makeText(requireContext(),"Edit: ${}",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
    }





    private fun updateUI(user: FirebaseUser?, name: String) {
            if (user!=null){
//                Log.d("editText","Edit: ${edtNameUserSignUp.text.toString()}")
                val profileUpdates = userProfileChangeRequest {
                    displayName = name
//            photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
                }

                user.updateProfile(profileUpdates)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val ref = FirebaseDatabase.getInstance().reference.child("user").child(user.uid)
                            ref.child("email").setValue(user.email)
                            ref.child("name").setValue(user.displayName)
                            Log.d("Login", "User profile updated.")
                        }
                    }

            }


    }

    private fun addEventSignup(name: String) {
        val firebaseAuth = FirebaseAuth.getInstance()
        if (!validateForm(
                edtEmailSignUp.text.toString(),
                edtPWSignUp.text.toString()
            )
        ) return
        firebaseAuth.createUserWithEmailAndPassword(
            edtEmailSignUp.text.toString(),
            edtPWSignUp.text.toString()
        )
            .addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        Log.d("Login","Đăng ký Đăng ký thành công roài nè!\nĐăng nhập đi nhó")
                        var user = auth.currentUser
                        updateUI(user,name)
                        Toast.makeText(requireContext(),"Success",Toast.LENGTH_SHORT).show()

                    } else {
                        Log.d("Login","Có cái gì đó khum đúng rồi í.")
                        Toast.makeText(requireContext(),"UnSuccess",Toast.LENGTH_SHORT).show()
                    }
                    progressDialog.dismiss()

                }
    }
    private fun validateForm(email: String, pw: String): Boolean {
        var valid = true
        if (email.isEmpty()) valid = false
        if (pw.isEmpty() || pw.length < 6) valid = false
        return valid
    }
}