package com.example.kltnstudyword.fragment

import android.app.ProgressDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kltnstudyword.ActivityLearnWord
import com.example.kltnstudyword.R
import com.example.kltnstudyword.adapter.RecyclerViewTopicAdapter
import com.example.kltnstudyword.model.TopicModel
import com.example.kltnstudyword.model.WordModel
import com.example.kltnstudyword.roomdatabase.AppDatabase
import com.example.kltnstudyword.viewmodel.FragmentStudyViewModel
import com.example.testkotlin.onItemClick
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_study.*
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


private const val ARG_PARAM1 = "param1"

class FragmentStudyTest() : Fragment(),onItemClick {
    var fragmentView: View? = null
    private var param1: String? = null
    var listWord = ArrayList<WordModel>()


    //    private var listTopic = ArrayList<TopicModel>
//    private var listTopic = ArrayList<TopicModel>()
    private lateinit var topicAdapter: RecyclerViewTopicAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var database: DatabaseReference
    lateinit var viewModel: FragmentStudyViewModel
    private val storageRef = Firebase.storage.reference

//    private val job = GlobalScope.launch {
//        rcvListTopic.apply {
//            layoutManager=LinearLayoutManager(requireActivity().applicationContext)
//            topicAdapter = ListTopicAdapterRecycleView(listTopic,this@FragmentStudy,requireActivity().applicationContext)
//            adapter=topicAdapter
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            FragmentStudyTest().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
//                    putSerializable(ARG_PARAM1,list)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView = inflater.inflate(R.layout.fragment_study, container, false)
        return fragmentView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("process", "on view created")

        viewModel = ViewModelProviders.of(this).get(FragmentStudyViewModel::class.java)
        viewModel.getAllUsersObservers().observe(viewLifecycleOwner, Observer { list ->
            topicAdapter.setListData(ArrayList(list))
            topicAdapter.notifyDataSetChanged()
        })
        rcvListTopic.apply {
            layoutManager = LinearLayoutManager(requireActivity().applicationContext)
            topicAdapter = RecyclerViewTopicAdapter(
                this@FragmentStudyTest,
                requireActivity().applicationContext
            )
            adapter = topicAdapter
        }
    }


    override fun onOneClickItem(position: Int) {
        val mDb = AppDatabase.getInMemoryDatabase(requireActivity().applicationContext)

        val topicModel = mDb?.roomDao()?.findTopic(position+1)
        topicModel?.let {
            if (topicModel.isDownloaded==0){
                retrieveContent(topicModel)
            }else{
                changeUI(position+1)
            }
        }

    }

    override fun onLongClickItem(position: Int) {
        Log.d("Study", "long click $position")
    }

    override fun onClickItemWordModel(objects: WordModel, view: View) {

    }



     fun retrieveContent(topicModel: TopicModel){
        val progressDialog = ProgressDialog(activity)
         val index = topicModel.topicId
         val nameTopic = topicModel.nameTopic

         progressDialog.setTitle("Downloading")
         progressDialog.show()
        var sizeTopic: Long? = 0;
         var count : Long = 0;

         val mDb = AppDatabase.getInMemoryDatabase(requireActivity().applicationContext)


        FirebaseDatabase.getInstance().reference.child("topic/${index-1}/listWord").get().addOnSuccessListener {
            sizeTopic = it.childrenCount;
            for (data in it.children){
                DownloadImage(data.child("srcImg").value.toString()){
                        srcImg -> run{
                        downloadAudio(data.child("srcAudio").value.toString()){
                            srcAudio -> run{
                            val wordModel = data.getValue(WordModel::class.java)
                            wordModel?.let { wordM ->
                                wordM.srcAudioFb=wordM.srcAudio
                                wordM.srcImgFb=wordM.srcImg
                                wordM.srcImg=srcImg
                                wordM.srcAudio=srcAudio
                                wordM.topicId=index
                                wordM.level=0
//                                wordM.topicId=index
                                listWord.add(wordM)
                                mDb?.roomDao()?.insertWordModel(wordM)
                                count++
                                if (count==sizeTopic){
//                                    progressBar.isIndeterminate=false
                                    progressDialog.dismiss()
                                    mDb?.roomDao()?.updateTopic(TopicModel(index,nameTopic,topicModel.imgTopic,1))
                                    changeUI(index.toInt())
                                }
                            }
                        }
                        }

                    }
                }
            }

        }

    }

    private fun changeUI(position: Int) {
        val mDb = AppDatabase.getInMemoryDatabase(requireActivity().applicationContext)
        Log.d("checkdata","$position")
        val size = mDb?.roomDao()?.getNumber(position)

        var listWordIntent = ArrayList<WordModel>()
        size?.let {
            Log.d("checkdata","size: $size")
            listWordIntent = mDb?.roomDao()?.findAllWordSync(position) as ArrayList<WordModel>
            (listWordIntent.size==size).let {
                val intent = Intent(requireActivity(),ActivityLearnWord::class.java)
                intent.putExtra("topicId",listWordIntent)
                startActivity(intent)
            }
        }
    }


    private fun downloadImageFromFB(path: String, callBack: (String) -> Unit) = CoroutineScope(
        Dispatchers.IO
    ).launch {
        try {
            val maxDownloadSize = 5L * 1024 * 1024
            val bytes = storageRef.child(path).getBytes(maxDownloadSize).await()
            val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            val uri = saveImageToInternalStorage(bmp)
            Log.d("downloadImage", uri.toString())
            callBack(uri.toString())

        } catch (e: java.lang.Exception) {
            withContext(Dispatchers.Main) {
                Log.d("DownloadImage", e.message.toString())
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

    private fun saveImageToInternalStorage(bitmapImage: Bitmap): Uri {
        val wrapper = ContextWrapper(requireActivity().applicationContext)
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()

        }
        return Uri.parse(file.absolutePath)
    }
}