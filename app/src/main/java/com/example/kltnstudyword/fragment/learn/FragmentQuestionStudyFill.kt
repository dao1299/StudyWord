package com.example.kltnstudyword.fragment.learn

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.kltnstudyword.R
import com.example.kltnstudyword.model.WordModel
import kotlinx.android.synthetic.main.fragment_question_kind_1.*
import androidx.fragment.app.FragmentActivity


private const val ARG_PARAM1 = "param1"
class FragmentQuestionStudyFill: Fragment(){
    lateinit var wordReceive: WordModel
    lateinit var key : String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            wordReceive = it.getSerializable(ARG_PARAM1) as WordModel
        }
        return inflater.inflate(R.layout.fragment_question_kind_1, container, false)
    }

    companion object {
        fun newInstance(param1: WordModel) =
            FragmentQuestionStudyFill().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1,param1)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val animatedSlideIn : Animation = AnimationUtils.loadAnimation(requireContext().applicationContext, R.anim.abc_slide_in_bottom)
        super.onViewCreated(view, savedInstanceState)
        txtDefinitionWord.text=wordReceive.partOfSpeech
        key = wordReceive.word
        val button = requireActivity().findViewById<View>(R.id.btnNext)

//        edtAnswer.setOnClickListener {
//            val insetsController = ViewCompat.getWindowInsetsController(view)
//            insetsController?.show(WindowInsetsCompat.Type.ime())
//            val button = requireActivity().findViewById<View>(R.id.btnNext)
//            button.isEnabled=true
//            edtResult.beginBatchEdit()
//        }
        edtResult.addTextChangedListener(object :TextWatcher{
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
//                if (key.uppercase() == p0.toString().uppercase().trim()){
//                    txtResult.text="true"
//                }else{
//                    txtResult.text="false"
//                }
                txtResult.text=(key.uppercase() == p0.toString().uppercase().trim()).toString()
            }

        })
        containerFragmentQK1.setOnClickListener {
            hideSoftKeyboard(requireActivity()!!)
        }

//        btnSubmitAnswer.setOnClickListener {
//            containerResultKind1.apply {
//                visibility=View.VISIBLE
//                bringToFront()
//                startAnimation(animatedSlideIn)
//            }
//            btnSubmitAnswer.visibility=View.INVISIBLE
//        }
    }

    private fun hideSoftKeyboard(requireActivity: FragmentActivity) {
        requireActivity.currentFocus?.let {
            val inputMethodManager = ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java)!!
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    fun Activity.hideSoftKeyboard() {

    }
}