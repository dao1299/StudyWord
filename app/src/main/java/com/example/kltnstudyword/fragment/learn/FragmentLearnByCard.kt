package com.example.kltnstudyword.fragment.learn

import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.kltnstudyword.R
import com.example.kltnstudyword.model.WordModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_learn_by_card.*


private const val ARG_PARAM1 = "param1"
class FragmentLearnByCard: Fragment() {
    var wordReceive = WordModel()
//    lateinit var wordReceive : WordModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            wordReceive = it.getSerializable(ARG_PARAM1) as WordModel
        }
        val animatedSlideIn : Animation = AnimationUtils.loadAnimation(requireContext().applicationContext, R.anim.fab_slide_in_from_right);
        val view = inflater.inflate(R.layout.fragment_learn_by_card, container, false)
        view.animation = animatedSlideIn
        return view
    }

    companion object {
        fun newInstance(param1: WordModel) =
            FragmentLearnByCard().apply {
                arguments = Bundle().apply {
                    println(param1)
                    putSerializable(ARG_PARAM1,param1)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtWordInBackCard.text=wordReceive.word
        txtWordInFrontCard.text=wordReceive.word
        txtDefinitionInBackCard.text=wordReceive.partOfSpeech
        txtExampleENInBackCard.text=wordReceive.exampleEN
        txtExampleVNInBackCard.text=wordReceive.exampleVN
        txtSynonymsInBackCard.text=""

//        displayImage()
        if (wordReceive.srcImg!="null")
            imgWordInFrontCard.setImageURI(Uri.parse(wordReceive.srcImg))
//        Glide.with(requireActivity()).load(wordReceive.srcImg).into(imgWordInFrontCard)
        if (wordReceive.synonyms.isNotEmpty()){
            if (wordReceive.synonyms.length>=20){
                txtSynonymsInBackCard.text=wordReceive.synonyms.substring(0,wordReceive.synonyms.indexOf(", ",20))
            }else{
                txtSynonymsInBackCard.text=wordReceive.synonyms
            }
        }

        txtPhoneticInFrontCard.text=wordReceive.phonetic
        btnSpeak.setOnClickListener {
            playAudio()
        }
    }



    private fun playAudio() {
        val mediaPlayer = MediaPlayer()
        if (wordReceive.srcAudio=="null"){
            Toast.makeText(activity,"Error media",Toast.LENGTH_SHORT).show()
        }else{
            mediaPlayer.setDataSource(wordReceive.srcAudio)
            mediaPlayer.setOnPreparedListener { player ->
                player.start()
            }
            mediaPlayer.prepareAsync()
        }
    }
}