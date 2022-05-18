package com.example.kltnstudyword

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.state_internet.*

class CheckNetwork :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.state_internet)
        val networkConnection  = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer {
            if (it){
                layoutConnected.visibility= View.VISIBLE
                layoutDisconnected.visibility=View.GONE

            }else{
                layoutConnected.visibility= View.GONE
                layoutDisconnected.visibility=View.VISIBLE
            }
        })
    }
}