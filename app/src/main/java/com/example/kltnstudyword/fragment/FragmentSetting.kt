package com.example.kltnstudyword.fragment

import android.app.ProgressDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kltnstudyword.ActivityLogin
import com.example.kltnstudyword.MainActivity
import com.example.kltnstudyword.R
import com.example.kltnstudyword.adapter.LanguageSpinnerAdapter
import com.example.kltnstudyword.custom.CustomSpinner
import com.example.kltnstudyword.custom.spinnerLanguage
import com.example.kltnstudyword.fragment.login.FragmentSignIn
import com.example.kltnstudyword.inventory.LanguageData
import com.example.kltnstudyword.model.TopicModel
import com.example.kltnstudyword.model.WordModel
import com.example.kltnstudyword.roomdatabase.AppDatabase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_fragment_setting.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


private const val ARG_PARAM1 = "param1"

private const val KEY_VERSION_COUNT_DATE = "count_date"
class FragmentSetting : Fragment(),CustomSpinner.OnSpinnerEventsListener{
    var stateImageExpand = 0;
    lateinit var spinnerCustom : CustomSpinner
    lateinit var adapter : LanguageSpinnerAdapter
    lateinit var sharedPreferences : SharedPreferences
    val user = Firebase.auth.currentUser
    private val storageRef = Firebase.storage.reference

    companion object {
        fun newInstance(param1: String) =
            FragmentSetting().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addEventTouch()
        initSpinner(view)
        displayData()
    }

    private fun displayData() {

        if (user==null){
            txtNameUser.text="Please login!"
            txtLogout.visibility=View.INVISIBLE
            containerProfileUser.setOnClickListener {
                val intent = Intent(requireActivity(),ActivityLogin::class.java)
                startActivity(intent)
            }
        }else{
            txtNameUser.text=user.displayName
            txtLogout.visibility=View.VISIBLE
            // setting profile

        }
    }

    private fun initSpinner(view : View) {
        spnLanguage.setSpinnerEventsListener(this)

        adapter = LanguageSpinnerAdapter(requireActivity().applicationContext, LanguageData().getLanguageModelList())
        spnLanguage.adapter=adapter
    }

    private fun addEventTouch() {
        txtLogout.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(requireActivity(),ActivityLogin::class.java)
            startActivity(intent)
        }
        linearLayout6.setOnClickListener {
            restore()
        }
        linearLayout5.setOnClickListener {
            backup()
        }
        containerProfileUser.setOnClickListener {
            val activity = (activity as MainActivity)
            activity.setFragment(FragmentProfileUser.newInstance("s"), "s")
        }
    }



    private fun backup() {
        val mDb = AppDatabase.getInMemoryDatabase(requireContext())
        val sizeListWord = mDb?.roomDao()?.countWord()
        val listWord = ArrayList<WordModel>(mDb?.roomDao()?.findAllWord() as ArrayList<WordModel>)
        (sizeListWord==listWord.size).let {
            val ref = FirebaseDatabase.getInstance().reference.child("user").child(user!!.uid)
            ref.child("listWord").setValue(listWord).addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(requireContext(),"Backup done",Toast.LENGTH_SHORT).show()
                }else if(it.isCanceled){
                    Toast.makeText(requireContext(),"Backup have trouble",Toast.LENGTH_SHORT).show()
                }
            }
        }
        sharedPreferences = requireActivity().getSharedPreferences("PREF", Context.MODE_PRIVATE)
        sharedPreferences.getInt(KEY_VERSION_COUNT_DATE,0).let{
            FirebaseDatabase.getInstance().reference.child("user").child(user!!.uid).child("count_date").setValue("$it")
        }
        sharedPreferences.getLong("date",0).let{
            FirebaseDatabase.getInstance().reference.child("user").child(user!!.uid).child("date").setValue("$it")
        }



    }

    private fun restore() {
        val progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Downloading")
        progressDialog.show()
        val mDb = AppDatabase.getInMemoryDatabase(requireContext())
        var count : Int = 0;
        mDb?.roomDao()?.deleteAllWord()
        val ref = FirebaseDatabase.getInstance().reference.child("user").child(user!!.uid)
        val listWord = ArrayList<WordModel>()
        CoroutineScope(Dispatchers.IO).launch {
            ref.child("listWord").get().addOnSuccessListener {
                var size = it.childrenCount.toInt()
                for (data in it.children){
                    DownloadImage(data.child("srcImgFb").value.toString()){
                            srcImg -> run{
                        downloadAudio(data.child("srcAudioFb").value.toString()){
                                srcAudio -> run{
                            val wordModel = data.getValue(WordModel::class.java)
                            wordModel?.let { wordM ->
                                wordM.srcImg=srcImg
                                wordM.srcAudio=srcAudio
//                                wordM.topicId=index
                                listWord.add(wordM)
                                mDb?.roomDao()?.insertWordModel(wordM)
                                count++
                                if (count==size){
//                                    progressBar.isIndeterminate=false
                                    progressDialog.dismiss()
                                    val listTopicLearned: ArrayList<Int> = mDb?.roomDao()?.findTopicLearned() as ArrayList<Int>
                                    Log.d("element","${listTopicLearned.size}")
                                    for (element in listTopicLearned){
//                                        Toast.makeText(requireContext(),"element: $element",Toast.LENGTH_SHORT).show()
                                        mDb.roomDao()?.updateDownloadTopic(element,1)
                                    }
//                                    mDb?.roomDao()?.insertWordModel(wordM)
//                                    changeUI(index.toInt())
                                }
                            }
                        }
                        }

                    }
                    }
                }
            }
//            ref.child("date").get().result.value
//            sharedPreferences.edit().putLong("date",ref.child("date").get().result.value.toString().toLong()).apply()
//            sharedPreferences.edit().putInt("count_date",ref.child("count_date").get().result.value.toString().toInt()).apply()

        }



    }

    private fun DownloadImage(path: String,callBack: (String) -> Unit)= CoroutineScope(
        Dispatchers.IO
    ).launch {
        try {
            val wrapper = ContextWrapper(requireActivity().application)
            var file = wrapper.getDir("images", Context.MODE_PRIVATE)
            file = File(file, "${UUID.randomUUID()}.jpg")
            val down = storageRef.child(path)
            down.getFile(file).await()
            callBack(file.absolutePath)
        }catch (e: Exception){
            withContext(Dispatchers.Main){
                Log.d("Download",e.message.toString())
                callBack("null")
            }
        }

    }

    private fun downloadAudio(path: String,callBack: (String) -> Unit)= CoroutineScope(
        Dispatchers.IO
    ).launch {


        try {
            val wrapper = ContextWrapper(requireActivity().application)
            var file = wrapper.getDir("audio", Context.MODE_PRIVATE)
            file = File(file, "${UUID.randomUUID()}.mp3")
            val down = storageRef.child(path)
            down.getFile(file).await()
            callBack(file.absolutePath)
        }catch (e: Exception){
            withContext(Dispatchers.Main){
                Log.d("Download",e.message.toString())
                callBack("null")
            }
        }
    }

    override fun onPopupWindowOpened(spinner: Spinner?) {

    }

    override fun onPopupWindowClosed(spinner: Spinner?) {

    }


}