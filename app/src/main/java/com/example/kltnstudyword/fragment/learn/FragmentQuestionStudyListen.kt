package com.example.kltnstudyword.fragment.learn

import android.media.MediaPlayer
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.kltnstudyword.R
import com.example.kltnstudyword.model.WordModel
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_question_study_kind_1.*

private const val ARG_PARAM1 = "param1"
class FragmentQuestionStudyListen: Fragment() {
    lateinit var wordReceive: WordModel
    lateinit var key : String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            wordReceive=it.getSerializable(ARG_PARAM1) as WordModel
            key=wordReceive.word
        }
        return inflater.inflate(R.layout.fragment_question_study_kind_1, container, false)
    }

    companion object {
        fun newInstance(param1: WordModel) =
            FragmentQuestionStudyListen().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val animatedSlideIn : Animation = AnimationUtils.loadAnimation(requireContext().applicationContext, R.anim.abc_slide_in_bottom)
        super.onViewCreated(view, savedInstanceState)
        val button = requireActivity().findViewById<View>(R.id.btnNext)

        addEventTouch()

//        edtAnswerQuestionStudyListen.setOnClickListener {
////            val insetsController = ViewCompat.getWindowInsetsController(view)
////            insetsController?.show(WindowInsetsCompat.Type.ime())
////
//
//            button.isEnabled=true
//        }

        edtAnswerQuestionStudyListen.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                val txtResult = requireActivity().findViewById<View>(R.id.txtResult) as TextView
                button.isEnabled = p0.toString().isNotEmpty()
                if (!button.isEnabled) button.setBackgroundResource(R.drawable.background_shadow_button_disable)
                else{
                    button.setBackgroundResource(R.drawable.background_shadow_button)
                }
//                if (key.uppercase() == p0.toString().uppercase()){
//                    txtResult.text="true"
//                }else{
//                    txtResult.text="false"
//                }

                txtResult.text=(key.uppercase() == p0.toString().uppercase()).toString()
            }

        })

        containerFragmentListen.setOnClickListener { hideSoftKeyboard(requireActivity()!!) }

//        btnSubmitAnswer.setOnClickListener {
//            containerResultKind1.apply {
//                visibility=View.VISIBLE
//                bringToFront()
//                startAnimation(animatedSlideIn)
//            }
//            btnSubmitAnswer.visibility=View.INVISIBLE
//        }
    }

    private fun addEventTouch() {
        btnAudioQuestionStudyListenSlow.setOnTouchListener(View.OnTouchListener {v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> v.setBackgroundResource(R.drawable.background_shadow_button_touched)
                MotionEvent.ACTION_UP -> v.setBackgroundResource(R.drawable.background_shadow_button)
            }
            false
        })
        btnAudioQuestionStudyListen.setOnTouchListener(View.OnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> v.setBackgroundResource(R.drawable.background_shadow_button_touched)
                MotionEvent.ACTION_UP -> v.setBackgroundResource(R.drawable.background_shadow_button)
            }
            false
        })
        btnAudioQuestionStudyListen.setOnClickListener {
            if (wordReceive.srcAudio=="null"){
                Toast.makeText(activity,"Error media", Toast.LENGTH_SHORT).show()
            }else{
                playAudio(1f)
            }
        }
        btnAudioQuestionStudyListenSlow.setOnClickListener {
            if (wordReceive.srcAudio=="null"){
                Toast.makeText(activity,"Error media", Toast.LENGTH_SHORT).show()
            }else{
                playAudio(0.65f)
            }
        }
    }

    private fun playAudio(speed: Float) {
//        val firebaseStorage = FirebaseStorage.getInstance()
//        firebaseStorage.reference.child(wordReceive.srcAudio).downloadUrl.addOnSuccessListener {
//            val mediaPlayer = MediaPlayer()
//            mediaPlayer.setDataSource(it.toString())
//            mediaPlayer.setOnPreparedListener { player ->
//                player.start()
//            }
//            mediaPlayer.prepareAsync()
//            mediaPlayer.playbackParams = mediaPlayer.playbackParams.setSpeed(speed)
//        }
        val mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(wordReceive.srcAudio)
        mediaPlayer.setOnPreparedListener { player ->
            player.start()
        }
        mediaPlayer.prepareAsync()
        mediaPlayer.playbackParams = mediaPlayer.playbackParams.setSpeed(speed)
    }
    private fun hideSoftKeyboard(requireActivity: FragmentActivity) {
        requireActivity.currentFocus?.let {
            val inputMethodManager = ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java)!!
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

}