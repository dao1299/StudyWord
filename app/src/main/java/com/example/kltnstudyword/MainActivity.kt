package com.example.kltnstudyword

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.kltnstudyword.fragment.*
import com.example.kltnstudyword.model.TopicModel
import com.example.kltnstudyword.roomdatabase.AppDatabase
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

private const val KEY_VERSION_TOPIC = "versionTopic"
private const val KEY_VERSION_COUNT_DATE = "count_date"
class MainActivity : AppCompatActivity() {

    var  tt=0
    private lateinit var database: DatabaseReference
    lateinit var sharedPreferences : SharedPreferences
    private var listTopic = ArrayList<TopicModel>()
    private lateinit var versionTopic : String
    private var countDate : Int = 0
    private val storageRef = Firebase.storage.reference
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var valueVersion = retrieveVersionTopic()
        setContentView(R.layout.activity_main)
        sharedPreferences = getSharedPreferences("PREF", Context.MODE_PRIVATE)
        versionTopic = sharedPreferences.getString(KEY_VERSION_TOPIC,null).toString()

        if (versionTopic=="null" || versionTopic!=valueVersion) {
            val result = sharedPreferences.edit().putString(KEY_VERSION_TOPIC, valueVersion).apply()
                retrieveContent()

        }


        addEventClickItemNaviBottom()
        addEventClick()
        setFragment(FragmentHome.newInstance("hello"),"home_fragment")
    }

    override fun onResume() {
        super.onResume()

    }

    fun retrieveContent() {
        progressDialog = ProgressDialog.show(this, "Tải dữ liệu","Đợi tí tẹo thôi mờ (>\"<)", true);
        timerDelayRemoveDialog(10000L,progressDialog)
        val mDb = AppDatabase.getInMemoryDatabase(this)
        FirebaseDatabase.getInstance().reference.child("topic").get().addOnSuccessListener {
            var size = it.childrenCount.toInt()
            for (data in it.children){
                downloadImageFromFB(data.child("urlImg").value.toString()){
                        urlR -> run{
                            val topicModel = TopicModel(data.key.toString().toLong()+1,data.child("nameTopic").value.toString(),urlR,0)
                            listTopic.add(topicModel)
                            Log.d("listTopicRetrieve","${listTopic.last()}")
                            mDb?.roomDao()?.insertTopic(topicModel)
                            size--;
                            if (size==0) {
                                progressDialog.dismiss()
                            }
                    }
                }
            }
        }
    }
    fun timerDelayRemoveDialog(time: Long, d: Dialog) {
        Handler().postDelayed(Runnable { d.dismiss() }, time)
    }

    private fun downloadImageFromFB(path: String,callBack: (String)->Unit) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val maxDownloadSize = 5L * 1024 * 1024
            val bytes = storageRef.child(path).getBytes(maxDownloadSize).await()
            val bmp = BitmapFactory.decodeByteArray(bytes,0,bytes.size)
            val uri = saveImageToInternalStorage(bmp)
            Log.d("downloadImage",uri.toString())
            callBack(uri.toString())
//            withContext(Dispatchers.Main){
//                Glide.with(this@MainActivity).load(uri.toString()).into(imgLoad)
//            }
        }catch (e: java.lang.Exception){
            withContext(Dispatchers.Main){
                Log.d("DownloadImage",e.message.toString())
            }
        }
    }
    private fun saveImageToInternalStorage(bitmapImage: Bitmap): Uri {
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException){
            e.printStackTrace()
        }
        return Uri.parse(file.absolutePath)
    }

    private fun retrieveVersionTopic():String {
        var valueResult : String =""
        database = FirebaseDatabase.getInstance().reference.child("versionTopic")
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                valueResult=snapshot.value.toString()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        return valueResult
    }

    private fun addEventClick() {

    }

    private fun addEventClickItemNaviBottom() {
        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu_home -> setFragment(FragmentHome.newInstance("hello"),"home_fragment")
                R.id.menu_study -> setFragment(FragmentStudyTest.newInstance("1"),"study_fragment")
                R.id.menu_practice -> setFragment(FragmentPractice.newInstance("hello"),"practice_fragment")
                R.id.menu_setting -> setFragment(FragmentSetting.newInstance("hello"),"setting_fragment")
//                R.id.menu_practice -> setFragment(FragmentQuestionKind1.newInstance(),"practice_fragment")
            }
            true
        }
    }

    fun setFragment(fragment : Fragment, tag : String) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container_main,fragment,tag)
            addToBackStack("tag")
            commit()
        }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount>0) {
            super.onBackPressed()
        }
        else if (tt==0){
            Toast.makeText(applicationContext,"One more click",Toast.LENGTH_SHORT).show()
            tt++
        }
        finish()
    }

}