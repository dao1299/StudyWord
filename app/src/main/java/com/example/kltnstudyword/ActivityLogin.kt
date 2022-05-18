package com.example.kltnstudyword

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.kltnstudyword.fragment.FragmentLogin

class ActivityLogin: AppCompatActivity() {
    var  tt=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_login)
        setFragment(FragmentLogin.newInstance("1"),"123")
    }
    fun setFragment(fragment : Fragment, tag : String) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.containerLogin,fragment,tag)
            addToBackStack("tag")
            commit()
        }
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount>0) {
            super.onBackPressed()
        }
        else if (tt==0){
            Toast.makeText(applicationContext,"One more click", Toast.LENGTH_SHORT).show()
            tt++
        }else{
            finish()
        }
    }
}